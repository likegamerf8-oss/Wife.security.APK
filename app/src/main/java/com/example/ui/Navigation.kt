package com.example.ui

sealed class Screen(val route: String) {
    // 20 Exact Screens (Parts 4-23)
    object Splash : Screen("screen_01_splash")                     // Screen 01: Splash Screen
    object Welcome : Screen("screen_02_welcome")                   // Screen 02: Welcome Screen
    object WifiDetection : Screen("screen_03_wifi_detection")       // Screen 03: Wi-Fi Detection
    object RouterLogin : Screen("screen_04_router_login")           // Screen 04: Router Login & Authorization
    object RouterConnected : Screen("screen_05_router_connected")   // Screen 05: Router Connected
    object Dashboard : Screen("screen_06_dashboard")               // Screen 06: Home Dashboard
    object Devices : Screen("screen_07_devices")                   // Screen 07: Connected Devices
    object DeviceDetail : Screen("screen_08_device_detail")         // Screen 08: Device Details
    object BlockDevice : Screen("screen_09_block_device")           // Screen 09: Block / Unblock Device
    object SecurityAudit : Screen("screen_10_security_audit")       // Screen 10: Security Audit
    object NetworkHealth : Screen("screen_11_network_health")       // Screen 11: Network Health
    object ActivityLog : Screen("screen_12_activity_log")           // Screen 12: Activity Log
    object MultipleRouters : Screen("screen_13_multiple_routers")   // Screen 13: Multiple Routers
    object GuestNetwork : Screen("screen_14_guest_network")         // Screen 14: Guest Network
    object ParentalControls : Screen("screen_15_parental_controls") // Screen 15: Parental Controls
    object AiAssistant : Screen("screen_16_ai_assistant")           // Screen 16: AI Security Assistant
    object IspManagement : Screen("screen_17_isp_management")       // Screen 17: ISP Management
    object Notifications : Screen("screen_18_notifications")       // Screen 18: Notifications
    object Settings : Screen("screen_19_settings")                 // Screen 19: Settings
    object OptionalAccount : Screen("screen_20_optional_account")   // Screen 20: Optional Account / Login Info

    // Compatibility aliases for existing screens
    object Onboarding : Screen("onboarding")
    object Auth : Screen("auth")
    object OwnershipConsent : Screen("ownership_consent")
    object RouterWizard : Screen("router_wizard")
    object CompatibilityCatalog : Screen("compatibility_catalog")
    object PendingApprovals : Screen("pending_approvals")
    object SecurityPolicy : Screen("security_policy")
    object GuestAccess : Screen("guest_access")
    object ActivityHistory : Screen("activity_history")
    object Diagnostics : Screen("diagnostics")
    object Profile : Screen("profile")
    object HelpTroubleshooting : Screen("help_troubleshooting")
    object UnsupportedRouter : Screen("unsupported_router")
    object ErrorRecovery : Screen("error_recovery")
    object AdminManagement : Screen("admin_management")
    object IspPortal : Screen("isp_portal")
}

