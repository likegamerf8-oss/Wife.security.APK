package com.example.adapter

enum class CapabilityState {
    SUPPORTED,
    PARTIALLY_SUPPORTED,
    UNSUPPORTED,
    AUTHENTICATION_REQUIRED,
    PERMISSION_REQUIRED,
    CONNECTION_FAILED,
    NOT_VERIFIED
}

data class RouterCapability(
    val brand: String,
    val model: String,
    val hardwareRevision: String? = null,
    val firmwareVersion: String? = null,
    val localManagementAddress: String = "192.168.0.1",
    val authenticationStatus: String = "NOT_AUTHENTICATED", // AUTHENTICATED, NOT_AUTHENTICATED, INVALID_CREDENTIALS, READ_ONLY
    val managementProtocol: String = "HTTP_SESSION", // OPENWRT_UBUS, MIKROTIK_REST, TPLINK_HTTP, HUAWEI_WEB, TR069_ACS
    val overallState: CapabilityState = CapabilityState.NOT_VERIFIED,
    val supportedCapabilities: List<String> = emptyList(),
    val unsupportedCapabilities: List<String> = emptyList(),
    val lastVerificationTimestamp: Long = System.currentTimeMillis(),
    val errorOrLimitationDetails: String? = null
)
