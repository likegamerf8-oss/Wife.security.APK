package com.example.adapter

import kotlinx.coroutines.flow.Flow

data class IntegrationCapabilities(
    val readRouterIdentity: CapabilityStatus,
    val readConnectedDevices: CapabilityStatus,
    val detectNewAssociations: CapabilityStatus,
    val preAccessApproval: CapabilityStatus,
    val postAccessRevocation: CapabilityStatus,
    val guestAccessControl: CapabilityStatus,
    val bandwidthThrottling: CapabilityStatus
)

enum class CapabilityStatus {
    SUPPORTED,
    UNSUPPORTED,
    REQUIRES_CONFIGURATION,
    NOT_VERIFIED,
    FAILED
}

data class RouterIdentity(
    val manufacturer: String,
    val model: String,
    val hardwareRevision: String,
    val serialNumber: String,
    val macAddress: String,
    val uptimeSeconds: Long
)

data class FirmwareInfo(
    val version: String,
    val kernelVersion: String,
    val buildDate: String,
    val patchLevel: String
)

data class RawRouterDevice(
    val macAddress: String,
    val ipAddress: String,
    val hostname: String,
    val signalDbm: Int,
    val rxRateMbps: Float,
    val txRateMbps: Float,
    val isAssociated: Boolean,
    val isAuthorized: Boolean,
    val connectionType: String // 2.4GHz, 5GHz, Ethernet
)

data class RawRouterEvent(
    val eventType: String, // NEW_STATION, DEAUTH, DHCP_LEASE, ACL_BLOCKED
    val macAddress: String,
    val timestamp: Long,
    val description: String
)

data class IntegrationHealth(
    val isReachable: Boolean,
    val latencyMs: Long,
    val lastSyncTimestamp: Long,
    val apiStatusCode: Int,
    val errorMessage: String? = null
)

data class ConnectionTestResult(
    val success: Boolean,
    val httpStatus: Int,
    val latencyMs: Long,
    val verifiedCapabilitiesCount: Int,
    val warnings: List<String> = emptyList(),
    val message: String
)

interface RouterAdapter {
    val adapterId: String
    val adapterName: String
    val isTestDouble: Boolean

    suspend fun discoverCapabilities(): IntegrationCapabilities
    suspend fun getRouterIdentity(): Result<RouterIdentity>
    suspend fun getFirmwareInformation(): Result<FirmwareInfo>
    suspend fun listConnectedDevices(): Result<List<RawRouterDevice>>
    suspend fun observeConnectionEvents(): Flow<RawRouterEvent>
    suspend fun approveDevice(mac: String, ip: String): Result<Boolean>
    suspend fun denyDevice(mac: String): Result<Boolean>
    suspend fun revokeDeviceAccess(mac: String): Result<Boolean>
    suspend fun getIntegrationHealth(): IntegrationHealth
    suspend fun testReadOnlyConnection(): Result<ConnectionTestResult>
}
