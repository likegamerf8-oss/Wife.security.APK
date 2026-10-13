package com.example.data.repository

import com.example.adapter.ConnectionTestResult
import com.example.adapter.IntegrationCapabilities
import com.example.adapter.IntegrationHealth
import com.example.adapter.RouterAdapterRegistry
import com.example.data.local.AppDatabase
import com.example.data.local.entity.ApprovalRequestEntity
import com.example.data.local.entity.CompatibilityModelEntity
import com.example.data.local.entity.CompatibilityRequestEntity
import com.example.data.local.entity.DeviceEntity
import com.example.data.local.entity.GuestPassEntity
import com.example.data.local.entity.NetworkEntity
import com.example.data.local.entity.RouterEntity
import com.example.data.local.entity.SecurityEventEntity
import com.example.data.local.entity.SecurityPolicyEntity
import com.example.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID

class WifiSecurityRepository(private val database: AppDatabase) {

    val currentUser: Flow<UserEntity?> = database.userDao().getCurrentUser()
    val activeNetwork: Flow<NetworkEntity?> = database.networkDao().getActiveNetwork()
    val activeRouter: Flow<RouterEntity?> = database.routerDao().getActiveRouter()
    val allRouters: Flow<List<RouterEntity>> = database.routerDao().getAllRouters()
    val allDevices: Flow<List<DeviceEntity>> = database.deviceDao().getAllDevices()
    val pendingApprovalRequests: Flow<List<ApprovalRequestEntity>> = database.approvalRequestDao().getPendingRequests()
    val allApprovalRequests: Flow<List<ApprovalRequestEntity>> = database.approvalRequestDao().getAllRequests()
    val securityEvents: Flow<List<SecurityEventEntity>> = database.securityEventDao().getAllEvents()
    val compatibilityModels: Flow<List<CompatibilityModelEntity>> = database.compatibilityDao().getAllModels()
    val securityPolicy: Flow<SecurityPolicyEntity?> = database.securityPolicyDao().getPolicy()
    val guestPasses: Flow<List<GuestPassEntity>> = database.guestPassDao().getAllGuestPasses()
    val compatibilityRequests: Flow<List<CompatibilityRequestEntity>> = database.compatibilityDao().getAllCompatibilityRequests()
    val ispOrganizations: Flow<List<com.example.data.local.entity.IspOrganizationEntity>> = database.ispDao().getAllOrganizations()
    val allIspCustomers: Flow<List<com.example.data.local.entity.IspCustomerEntity>> = database.ispDao().getAllCustomers()
    val allParentalProfiles: Flow<List<com.example.data.local.entity.ParentalProfileEntity>> = database.parentalProfileDao().getAllProfiles()
    val allAuditFindings: Flow<List<com.example.data.local.entity.SecurityAuditFindingEntity>> = database.securityAuditDao().getAllFindings()

    suspend fun login(email: String, name: String, role: String = "OWNER") {
        val user = UserEntity(
            id = "usr_" + UUID.randomUUID().toString().take(8),
            email = email,
            displayName = name,
            role = role,
            isAuthenticated = true,
            lastLoginTimestamp = System.currentTimeMillis()
        )
        database.userDao().insertUser(user)
        logEvent(
            type = "ADMIN_ACTION",
            title = "User Authenticated",
            description = "Logged in as $name ($email, Role: $role)",
            severity = "INFO"
        )
    }

    suspend fun logout() {
        database.userDao().clearUser()
    }

    suspend fun saveNetworkOwnershipConsent(network: NetworkEntity) {
        database.networkDao().insertNetwork(network.copy(ownershipConsentSigned = true, isOwnerVerified = true))
        logEvent(
            type = "ADMIN_ACTION",
            title = "Ownership Verified & Consent Signed",
            description = "Network ${network.ssid} ownership verified under Bangladesh BTRC cybersecurity compliance guidelines.",
            severity = "INFO"
        )
    }

    suspend fun connectRouter(router: RouterEntity): Result<ConnectionTestResult> {
        val adapter = RouterAdapterRegistry.getAdapterForRouter(router)
        val testResult = adapter.testReadOnlyConnection()

        if (testResult.isSuccess && testResult.getOrNull()?.success == true) {
            val updatedRouter = router.copy(
                status = if (adapter.isTestDouble) "DEMO_MODE" else "CONNECTED",
                lastSyncTimestamp = System.currentTimeMillis(),
                latencyMs = testResult.getOrNull()?.latencyMs ?: 10L
            )
            database.routerDao().insertRouter(updatedRouter)
            logEvent(
                type = "ROUTER_CONNECTED",
                title = "Router Connected: ${router.name}",
                description = "Adapter ${adapter.adapterName} verified at ${router.ipAddress}:${router.port} (Latency: ${updatedRouter.latencyMs}ms)",
                severity = "INFO"
            )
            return testResult
        } else {
            val failureMsg = testResult.getOrNull()?.message ?: "Unreachable router host"
            logEvent(
                type = "DIAGNOSTIC_RUN",
                title = "Router Connection Attempt Failed",
                description = "Failed to connect to ${router.ipAddress}: $failureMsg",
                severity = "WARNING"
            )
            return Result.failure(Exception(failureMsg))
        }
    }

    suspend fun approvePendingRequest(requestId: String, reason: String = "Owner approved via app"): Result<Boolean> {
        val currentRequests = allApprovalRequests.firstOrNull() ?: emptyList()
        val request = currentRequests.find { it.id == requestId }
            ?: return Result.failure(Exception("Approval request not found"))

        if (request.status != "PENDING") {
            return Result.failure(Exception("Request is already in state: ${request.status}"))
        }

        if (System.currentTimeMillis() > request.expiresTimestamp) {
            database.approvalRequestDao().resolveRequest(requestId, "EXPIRED", System.currentTimeMillis(), "Request timed out")
            return Result.failure(Exception("Approval request expired"))
        }

        // Verify active router adapter
        val router = activeRouter.firstOrNull()
        if (router != null) {
            val adapter = RouterAdapterRegistry.getAdapterForRouter(router)
            val routerResult = adapter.approveDevice(request.macAddress, request.ipAddress)
            if (routerResult.isFailure) {
                return Result.failure(Exception("Hardware router command failed: ${routerResult.exceptionOrNull()?.message}"))
            }
        }

        // Update request & device in DB
        val now = System.currentTimeMillis()
        val user = currentUser.firstOrNull()
        val processedBy = user?.displayName ?: "Network Owner"

        database.approvalRequestDao().resolveRequest(requestId, "APPROVED", now, reason)
        database.deviceDao().updateDeviceState(request.deviceId, "AUTHORIZED")

        logEvent(
            type = "APPROVAL_GRANTED",
            title = "Access Approved for ${request.hostname}",
            description = "Owner granted network access to MAC ${request.macAddress} (${request.ipAddress})",
            severity = "INFO",
            relatedDeviceId = request.deviceId
        )

        return Result.success(true)
    }

    suspend fun rejectPendingRequest(requestId: String, reason: String = "Owner rejected access"): Result<Boolean> {
        val currentRequests = allApprovalRequests.firstOrNull() ?: emptyList()
        val request = currentRequests.find { it.id == requestId }
            ?: return Result.failure(Exception("Request not found"))

        val router = activeRouter.firstOrNull()
        if (router != null) {
            val adapter = RouterAdapterRegistry.getAdapterForRouter(router)
            adapter.denyDevice(request.macAddress)
        }

        val now = System.currentTimeMillis()
        database.approvalRequestDao().resolveRequest(requestId, "REJECTED", now, reason)
        database.deviceDao().updateDeviceState(request.deviceId, "BLOCKED")

        logEvent(
            type = "APPROVAL_DENIED",
            title = "Access Denied for ${request.hostname}",
            description = "Denied connection request for MAC ${request.macAddress}. Station quarantined or blocked.",
            severity = "WARNING",
            relatedDeviceId = request.deviceId
        )

        return Result.success(true)
    }

    suspend fun revokeDeviceAccess(deviceId: String): Result<Boolean> {
        val devices = allDevices.firstOrNull() ?: emptyList()
        val device = devices.find { it.id == deviceId }
            ?: return Result.failure(Exception("Device not found"))

        val router = activeRouter.firstOrNull()
        if (router != null) {
            val adapter = RouterAdapterRegistry.getAdapterForRouter(router)
            adapter.revokeDeviceAccess(device.macAddress)
        }

        database.deviceDao().updateDeviceState(deviceId, "REVOKED")

        logEvent(
            type = "ACCESS_REVOKED",
            title = "Access Revoked: ${device.hostname}",
            description = "Revoked network authorization and blocked MAC ${device.macAddress}",
            severity = "CRITICAL",
            relatedDeviceId = deviceId
        )

        return Result.success(true)
    }

    suspend fun unblockDeviceAccess(deviceId: String): Result<Boolean> {
        val devices = allDevices.firstOrNull() ?: emptyList()
        val device = devices.find { it.id == deviceId }
            ?: return Result.failure(Exception("Device not found"))

        val router = activeRouter.firstOrNull()
        if (router != null) {
            val adapter = RouterAdapterRegistry.getAdapterForRouter(router)
            adapter.approveDevice(device.macAddress, device.ipAddress)
        }

        database.deviceDao().updateDeviceState(deviceId, "AUTHORIZED")

        logEvent(
            type = "ACCESS_RESTORED",
            title = "Access Restored: ${device.hostname}",
            description = "Unblocked MAC ${device.macAddress} and restored network access",
            severity = "INFO",
            relatedDeviceId = deviceId
        )

        return Result.success(true)
    }

    suspend fun disconnectActiveRouter() {
        val router = activeRouter.firstOrNull() ?: return
        database.routerDao().updateRouter(router.copy(status = "DISCONNECTED"))
        logEvent(
            type = "ADMIN_ACTION",
            title = "Router Disconnected",
            description = "Active router session disconnected by user.",
            severity = "INFO"
        )
    }

    suspend fun updateDeviceDetails(device: DeviceEntity) {
        database.deviceDao().updateDevice(device)
    }

    suspend fun updateLiveDevices(devices: List<DeviceEntity>) {
        database.deviceDao().insertDevices(devices)
    }

    suspend fun createGuestPass(guestName: String, durationHours: Int): GuestPassEntity {
        val pass = GuestPassEntity(
            id = "gst_" + UUID.randomUUID().toString().take(8),
            networkId = activeNetwork.firstOrNull()?.id ?: "net_default",
            guestName = guestName,
            passCode = "BD-PASS-" + (1000..9999).random(),
            createdAt = System.currentTimeMillis(),
            expiresAt = System.currentTimeMillis() + (durationHours * 3600 * 1000L),
            isActive = true
        )
        database.guestPassDao().insertGuestPass(pass)
        logEvent(
            type = "POLICY_CHANGED",
            title = "Temporary Guest Pass Created",
            description = "Pass for '$guestName' valid for $durationHours hours (Code: ${pass.passCode})",
            severity = "INFO"
        )
        return pass
    }

    suspend fun revokeGuestPass(passId: String) {
        database.guestPassDao().revokeGuestPass(passId)
        logEvent(
            type = "ACCESS_REVOKED",
            title = "Guest Pass Revoked",
            description = "Guest access token $passId deactivated immediately.",
            severity = "WARNING"
        )
    }

    suspend fun updateSecurityPolicy(policy: SecurityPolicyEntity) {
        database.securityPolicyDao().updatePolicy(policy)
        logEvent(
            type = "POLICY_CHANGED",
            title = "Security Policy Mode Changed",
            description = "Network mode set to ${policy.mode}. Pre-access approval: ${policy.mode == "DEFAULT_DENY_PRE_ACCESS"}",
            severity = "INFO"
        )
    }

    suspend fun runRouterDiagnostics(): IntegrationHealth {
        val router = activeRouter.firstOrNull()
        if (router == null) {
            return IntegrationHealth(
                isReachable = false,
                latencyMs = -1L,
                lastSyncTimestamp = System.currentTimeMillis(),
                apiStatusCode = 0,
                errorMessage = "No router configured"
            )
        }
        val adapter = RouterAdapterRegistry.getAdapterForRouter(router)
        val health = adapter.getIntegrationHealth()
        logEvent(
            type = "DIAGNOSTIC_RUN",
            title = "Diagnostics Completed",
            description = "API Reachable: ${health.isReachable}, Latency: ${health.latencyMs}ms, Status: ${health.apiStatusCode}",
            severity = if (health.isReachable) "INFO" else "WARNING"
        )
        return health
    }

    suspend fun submitCompatibilityRequest(req: CompatibilityRequestEntity) {
        database.compatibilityDao().insertCompatibilityRequest(req)
        logEvent(
            type = "ADMIN_ACTION",
            title = "Compatibility Request Submitted",
            description = "Submitted request for ${req.manufacturer} ${req.model} (ISP: ${req.ispName})",
            severity = "INFO"
        )
    }

    suspend fun addCompatibilityModel(model: CompatibilityModelEntity) {
        database.compatibilityDao().insertModel(model)
        logEvent(
            type = "ADMIN_ACTION",
            title = "Catalog Updated",
            description = "Added router model ${model.manufacturer} ${model.modelName} to registry.",
            severity = "INFO"
        )
    }

    suspend fun switchActiveRouter(routerId: String) {
        database.routerDao().switchActiveRouter(routerId)
        val newActive = database.routerDao().getActiveRouter().firstOrNull()
        logEvent(
            type = "ADMIN_ACTION",
            title = "Active Router Switched",
            description = "Now managing router: ${newActive?.name ?: routerId}",
            severity = "INFO"
        )
    }

    suspend fun removeRouter(routerId: String) {
        database.routerDao().deleteRouter(routerId)
        logEvent(
            type = "ADMIN_ACTION",
            title = "Router Removed",
            description = "Removed router $routerId from local inventory.",
            severity = "WARNING"
        )
    }

    fun getParentalProfiles(routerId: String): Flow<List<com.example.data.local.entity.ParentalProfileEntity>> {
        return database.parentalProfileDao().getProfilesForRouter(routerId)
    }

    suspend fun saveParentalProfile(profile: com.example.data.local.entity.ParentalProfileEntity) {
        database.parentalProfileDao().insertProfile(profile)
        logEvent(
            type = "POLICY_CHANGED",
            title = "Parental Profile Updated: ${profile.profileName}",
            description = "Curfew schedule: ${profile.bedTimeHour}:00 - ${profile.wakeUpHour}:00",
            severity = "INFO"
        )
    }

    suspend fun deleteParentalProfile(id: String) {
        database.parentalProfileDao().deleteProfile(id)
    }

    fun getSecurityAuditFindings(routerId: String): Flow<List<com.example.data.local.entity.SecurityAuditFindingEntity>> {
        return database.securityAuditDao().getFindingsForRouter(routerId)
    }

    suspend fun remediateSecurityFinding(findingId: String) {
        database.securityAuditDao().markRemediated(findingId)
        logEvent(
            type = "POLICY_CHANGED",
            title = "Security Finding Remediated",
            description = "Remediated audit item $findingId.",
            severity = "INFO"
        )
    }

    fun getIspCustomers(orgId: String): Flow<List<com.example.data.local.entity.IspCustomerEntity>> {
        return database.ispDao().getCustomersByOrg(orgId)
    }

    suspend fun registerIspOrganization(org: com.example.data.local.entity.IspOrganizationEntity) {
        database.ispDao().insertOrganization(org)
    }

    private suspend fun logEvent(
        type: String,
        title: String,
        description: String,
        severity: String,
        relatedDeviceId: String? = null
    ) {
        val event = SecurityEventEntity(
            id = "evt_" + UUID.randomUUID().toString().take(8),
            networkId = activeNetwork.firstOrNull()?.id ?: "net_default",
            type = type,
            title = title,
            description = description,
            timestamp = System.currentTimeMillis(),
            severity = severity,
            relatedDeviceId = relatedDeviceId
        )
        database.securityEventDao().insertEvent(event)
    }
}
