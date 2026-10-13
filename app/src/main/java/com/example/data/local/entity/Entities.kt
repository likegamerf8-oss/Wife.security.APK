package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val email: String,
    val displayName: String,
    val role: String, // OWNER, ADMIN, VIEWER, SUPPORT
    val isAuthenticated: Boolean = true,
    val lastLoginTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "networks")
data class NetworkEntity(
    @PrimaryKey val id: String,
    val ssid: String,
    val bssid: String,
    val securityType: String, // WPA2_PSK, WPA3_SAE, OPEN
    val routerId: String,
    val isOwnerVerified: Boolean = true,
    val ownershipConsentSigned: Boolean = true,
    val localSubnet: String = "192.168.1.0/24",
    val gatewayIp: String = "192.168.1.1",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "routers")
data class RouterEntity(
    @PrimaryKey val id: String,
    val name: String,
    val manufacturer: String,
    val model: String,
    val hardwareRevision: String,
    val firmwareVersion: String,
    val connectionType: String, // OPENWRT_RPC, MIKROTIK_REST, TPLINK_HTTP, HUAWEI_ONT, TEST_DOUBLE
    val ipAddress: String,
    val port: Int,
    val useHttps: Boolean,
    val username: String,
    val encryptedToken: String,
    val status: String, // CONNECTED, CONNECTED_LIMITED, OFFLINE, SETUP_REQUIRED, UNSUPPORTED, DEMO_MODE
    val lastSyncTimestamp: Long = System.currentTimeMillis(),
    val latencyMs: Long = 12L,
    val isPreAccessSupported: Boolean = true,
    val isMacFilterSupported: Boolean = true,
    val isGuestNetworkSupported: Boolean = true,
    val ispName: String = "Link3 Technologies (BD)",
    val isActiveRouter: Boolean = true
)

@Entity(tableName = "devices")
data class DeviceEntity(
    @PrimaryKey val id: String,
    val networkId: String,
    val macAddress: String,
    val ipAddress: String,
    val hostname: String,
    val vendor: String,
    val category: String, // PHONE, LAPTOP, IOT, SMART_TV, GUEST, UNKNOWN
    val authorizationState: String, // AUTHORIZED, PENDING, UNKNOWN, BLOCKED, REVOKED
    val isRandomizedMac: Boolean = false,
    val isTrusted: Boolean = false,
    val firstObservedTimestamp: Long = System.currentTimeMillis(),
    val lastObservedTimestamp: Long = System.currentTimeMillis(),
    val notes: String = ""
)

@Entity(tableName = "approval_requests")
data class ApprovalRequestEntity(
    @PrimaryKey val id: String,
    val deviceId: String,
    val networkId: String,
    val macAddress: String,
    val hostname: String,
    val ipAddress: String,
    val requestedTimestamp: Long = System.currentTimeMillis(),
    val expiresTimestamp: Long = System.currentTimeMillis() + 15 * 60 * 1000, // 15 mins default
    val status: String, // PENDING, APPROVED, REJECTED, EXPIRED
    val decisionReason: String = "",
    val processedByUserId: String = "",
    val processedTimestamp: Long = 0L
)

@Entity(tableName = "security_events")
data class SecurityEventEntity(
    @PrimaryKey val id: String,
    val networkId: String,
    val type: String, // ROUTER_CONNECTED, NEW_DEVICE_DETECTED, APPROVAL_REQUESTED, APPROVAL_GRANTED, APPROVAL_DENIED, ACCESS_REVOKED, POLICY_CHANGED, DIAGNOSTIC_RUN
    val title: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis(),
    val severity: String, // INFO, WARNING, CRITICAL
    val relatedDeviceId: String? = null
)

@Entity(tableName = "compatibility_models")
data class CompatibilityModelEntity(
    @PrimaryKey val id: String,
    val manufacturer: String,
    val modelName: String,
    val deviceCategory: String, // ROUTER, GPON_ONT, ACCESS_POINT, MESH
    val hardwareRevisions: String,
    val firmwareRequirements: String,
    val integrationMethod: String,
    val bangladeshIsps: String, // e.g. Link3, Carnival, Amber IT, Dot Internet, BTCL
    val supportLevel: String, // FULLY_VERIFIED, PARTIALLY_VERIFIED, RESEARCH_REQUIRED, DEV_REQUIRED, UNSUPPORTED
    val supportsPreAccess: Boolean,
    val supportsMacFilter: Boolean,
    val supportsGuestNetwork: Boolean,
    val officialDocUrl: String,
    val lastVerifiedDate: String,
    val knownLimitations: String
)

@Entity(tableName = "security_policies")
data class SecurityPolicyEntity(
    @PrimaryKey val id: String,
    val networkId: String,
    val mode: String, // DEFAULT_DENY_PRE_ACCESS, DETECT_AND_NOTIFY, QUARANTINE_POST_ACCESS
    val autoTrustApproved: Boolean = true,
    val guestAccessTimeoutMinutes: Int = 240, // 4 hours
    val alertOnUnknownDevices: Boolean = true,
    val notificationEnabled: Boolean = true
)

@Entity(tableName = "guest_passes")
data class GuestPassEntity(
    @PrimaryKey val id: String,
    val networkId: String,
    val guestName: String,
    val passCode: String,
    val createdAt: Long = System.currentTimeMillis(),
    val expiresAt: Long = System.currentTimeMillis() + 4 * 3600 * 1000L,
    val isActive: Boolean = true,
    val allowedDeviceCount: Int = 1
)

@Entity(tableName = "compatibility_requests")
data class CompatibilityRequestEntity(
    @PrimaryKey val id: String,
    val manufacturer: String,
    val model: String,
    val ispName: String,
    val userEmail: String,
    val notes: String,
    val createdAt: Long = System.currentTimeMillis(),
    val status: String = "UNDER_REVIEW" // UNDER_REVIEW, RESEARCHING, PLANNED, UNSUPPORTED
)

@Entity(tableName = "isp_organizations")
data class IspOrganizationEntity(
    @PrimaryKey val id: String,
    val name: String,
    val btrcLicenseNumber: String,
    val supportHotline: String,
    val coverageAreas: String,
    val tr069AcsUrl: String,
    val supportedOntTypes: String,
    val isActive: Boolean = true
)

@Entity(tableName = "isp_customers")
data class IspCustomerEntity(
    @PrimaryKey val id: String,
    val organizationId: String,
    val customerCode: String,
    val customerName: String,
    val phone: String,
    val address: String,
    val routerId: String,
    val pppoeUsername: String,
    val servicePackage: String,
    val ontOpticalPowerDbm: Float = -19.5f,
    val isOnline: Boolean = true
)

@Entity(tableName = "parental_profiles")
data class ParentalProfileEntity(
    @PrimaryKey val id: String,
    val routerId: String,
    val profileName: String,
    val assignedDeviceMacs: String, // comma-separated MAC addresses
    val bedTimeHour: Int = 22,
    val bedTimeMinute: Int = 0,
    val wakeUpHour: Int = 6,
    val isScheduleActive: Boolean = true,
    val isBlockedNow: Boolean = false
)

@Entity(tableName = "security_audit_findings")
data class SecurityAuditFindingEntity(
    @PrimaryKey val id: String,
    val routerId: String,
    val title: String,
    val severity: String, // INFORMATIONAL, LOW, MEDIUM, HIGH, CRITICAL
    val evidence: String,
    val explanation: String,
    val recommendedSolution: String,
    val isRemediated: Boolean = false,
    val isAutoRemediationSupported: Boolean = false
)
