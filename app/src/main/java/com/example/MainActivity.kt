package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.localization.LanguageManager
import com.example.ui.Screen
import com.example.ui.screens.ActivityHistoryScreen
import com.example.ui.screens.AdminManagementScreen
import com.example.ui.screens.AiAssistantScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.BlockDeviceScreen
import com.example.ui.screens.CompatibilityCatalogScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.DeviceDetailScreen
import com.example.ui.screens.DevicesScreen
import com.example.ui.screens.DiagnosticsScreen
import com.example.ui.screens.ErrorRecoveryScreen
import com.example.ui.screens.GuestAccessScreen
import com.example.ui.screens.HelpTroubleshootingScreen
import com.example.ui.screens.IspPortalScreen
import com.example.ui.screens.MultipleRoutersScreen
import com.example.ui.screens.NetworkHealthScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.OptionalAccountScreen
import com.example.ui.screens.OwnershipConsentScreen
import com.example.ui.screens.ParentalControlsScreen
import com.example.ui.screens.PendingApprovalsScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RouterConnectedScreen
import com.example.ui.screens.RouterLoginScreen
import com.example.ui.screens.RouterWizardScreen
import com.example.ui.screens.SecurityAuditScreen
import com.example.ui.screens.SecurityPolicyScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.UnsupportedRouterScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.screens.WifiDetectionScreen
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.EmeraldTeal
import com.example.ui.theme.MidnightSurface
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val initialDestination = intent?.getStringExtra("NAVIGATE_TO")

        setContent {
            val darkTheme by viewModel.darkThemeEnabled.collectAsStateWithLifecycle()

            MyApplicationTheme(darkTheme = darkTheme) {
                MainAppContent(
                    viewModel = viewModel,
                    initialDestination = initialDestination
                )
            }
        }
    }
}

@Composable
fun MainAppContent(
    viewModel: MainViewModel,
    initialDestination: String?
) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Splash) }

    val user by viewModel.currentUser.collectAsStateWithLifecycle()
    val network by viewModel.activeNetwork.collectAsStateWithLifecycle()
    val router by viewModel.activeRouter.collectAsStateWithLifecycle()
    val devices by viewModel.allDevices.collectAsStateWithLifecycle()
    val pendingRequests by viewModel.pendingRequests.collectAsStateWithLifecycle()
    val securityEvents by viewModel.securityEvents.collectAsStateWithLifecycle()
    val compatibilityModels by viewModel.compatibilityModels.collectAsStateWithLifecycle()
    val compatibilityRequests by viewModel.compatibilityRequests.collectAsStateWithLifecycle()
    val securityPolicy by viewModel.securityPolicy.collectAsStateWithLifecycle()
    val guestPasses by viewModel.guestPasses.collectAsStateWithLifecycle()
    val selectedDevice by viewModel.selectedDevice.collectAsStateWithLifecycle()
    val lastDiagnosticHealth by viewModel.lastDiagnosticHealth.collectAsStateWithLifecycle()
    val connectionTestState by viewModel.connectionTestState.collectAsStateWithLifecycle()
    val isConnecting by viewModel.isConnecting.collectAsStateWithLifecycle()
    val isScanningDevices by viewModel.isScanningDevices.collectAsStateWithLifecycle()
    val darkTheme by viewModel.darkThemeEnabled.collectAsStateWithLifecycle()
    val wifiInfo by viewModel.wifiInfo.collectAsStateWithLifecycle()
    val autoLinkStatus by viewModel.autoLinkStatus.collectAsStateWithLifecycle()
    val authDialogSignature by viewModel.authDialogSignature.collectAsStateWithLifecycle()
    val allRouters by viewModel.allRouters.collectAsStateWithLifecycle()
    val ispOrganizations by viewModel.ispOrganizations.collectAsStateWithLifecycle()
    val ispCustomers by viewModel.ispCustomers.collectAsStateWithLifecycle()
    val parentalProfiles by viewModel.parentalProfiles.collectAsStateWithLifecycle()
    val auditFindings by viewModel.auditFindings.collectAsStateWithLifecycle()

    val pendingCount = pendingRequests.size

    // Handle deep link / notification navigation
    LaunchedEffect(initialDestination) {
        if (initialDestination == "pending_approvals") {
            currentScreen = Screen.PendingApprovals
        }
    }

    // Predictive / Android Hardware Back Button Handling
    BackHandler(enabled = currentScreen != Screen.Dashboard && currentScreen != Screen.Splash) {
        currentScreen = when (currentScreen) {
            Screen.DeviceDetail -> Screen.Devices
            Screen.BlockDevice -> Screen.DeviceDetail
            Screen.RouterLogin -> Screen.WifiDetection
            Screen.RouterConnected -> Screen.Dashboard
            Screen.WifiDetection -> Screen.Welcome
            Screen.Welcome -> Screen.Splash
            Screen.Notifications -> Screen.Dashboard
            Screen.OptionalAccount -> Screen.Dashboard
            Screen.RouterWizard, Screen.CompatibilityCatalog, Screen.PendingApprovals,
            Screen.SecurityPolicy, Screen.GuestAccess, Screen.GuestNetwork,
            Screen.ActivityHistory, Screen.ActivityLog,
            Screen.Diagnostics, Screen.Profile, Screen.Settings,
            Screen.HelpTroubleshooting, Screen.UnsupportedRouter, Screen.AdminManagement,
            Screen.ErrorRecovery, Screen.AiAssistant, Screen.IspPortal, Screen.IspManagement,
            Screen.MultipleRouters, Screen.NetworkHealth, Screen.ParentalControls,
            Screen.SecurityAudit -> Screen.Dashboard
            Screen.OwnershipConsent -> Screen.OptionalAccount
            Screen.Auth -> Screen.Welcome
            Screen.Onboarding -> Screen.Splash
            else -> Screen.Dashboard
        }
    }

    val showBottomBar = currentScreen in listOf(
        Screen.Dashboard,
        Screen.Devices,
        Screen.SecurityAudit,
        Screen.ActivityLog,
        Screen.ActivityHistory,
        Screen.Settings
    )

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MidnightSurface,
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .testTag("main_navigation_bar")
                ) {
                    NavigationBarItem(
                        selected = currentScreen == Screen.Dashboard,
                        onClick = { currentScreen = Screen.Dashboard },
                        icon = { Icon(Icons.Default.Dashboard, contentDescription = "Home") },
                        label = { Text("Home") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = EmeraldTeal,
                            selectedTextColor = EmeraldTeal,
                            indicatorColor = Color(0xFF064E3B),
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF94A3B8)
                        ),
                        modifier = Modifier.testTag("nav_tab_dashboard")
                    )

                    NavigationBarItem(
                        selected = currentScreen == Screen.Devices,
                        onClick = { currentScreen = Screen.Devices },
                        icon = { Icon(Icons.Default.Devices, contentDescription = "Devices") },
                        label = { Text("Devices") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = EmeraldTeal,
                            selectedTextColor = EmeraldTeal,
                            indicatorColor = Color(0xFF064E3B),
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF94A3B8)
                        ),
                        modifier = Modifier.testTag("nav_tab_devices")
                    )

                    NavigationBarItem(
                        selected = currentScreen == Screen.SecurityAudit,
                        onClick = { currentScreen = Screen.SecurityAudit },
                        icon = { Icon(Icons.Default.Security, contentDescription = "Security") },
                        label = { Text("Security") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = EmeraldTeal,
                            selectedTextColor = EmeraldTeal,
                            indicatorColor = Color(0xFF064E3B),
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF94A3B8)
                        ),
                        modifier = Modifier.testTag("nav_tab_security")
                    )

                    NavigationBarItem(
                        selected = currentScreen == Screen.ActivityLog || currentScreen == Screen.ActivityHistory,
                        onClick = { currentScreen = Screen.ActivityLog },
                        icon = { Icon(Icons.Default.History, contentDescription = "Activity") },
                        label = { Text("Activity") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = EmeraldTeal,
                            selectedTextColor = EmeraldTeal,
                            indicatorColor = Color(0xFF064E3B),
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF94A3B8)
                        ),
                        modifier = Modifier.testTag("nav_tab_activity")
                    )

                    NavigationBarItem(
                        selected = currentScreen == Screen.Settings,
                        onClick = { currentScreen = Screen.Settings },
                        icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                        label = { Text("Settings") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = EmeraldTeal,
                            selectedTextColor = EmeraldTeal,
                            indicatorColor = Color(0xFF064E3B),
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF94A3B8)
                        ),
                        modifier = Modifier.testTag("nav_tab_settings")
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (currentScreen) {
                // Screen 01: Splash Screen
                Screen.Splash -> {
                    SplashScreen(
                        onInitialized = {
                            currentScreen = Screen.Welcome
                        }
                    )
                }

                // Screen 02: Welcome Screen
                Screen.Welcome, Screen.Onboarding -> {
                    WelcomeScreen(
                        onConnectWifi = { currentScreen = Screen.WifiDetection },
                        onOpenOptionalAccount = { currentScreen = Screen.OptionalAccount }
                    )
                }

                // Screen 03: Wi-Fi Detection
                Screen.WifiDetection -> {
                    WifiDetectionScreen(
                        wifiInfo = wifiInfo,
                        discoveredSignature = authDialogSignature,
                        onRefreshDetection = { viewModel.triggerAutoDetectionAndLinking() },
                        onProceedToLogin = { gatewayIp -> currentScreen = Screen.RouterLogin },
                        onManualSetup = { currentScreen = Screen.RouterLogin },
                        onCancel = { currentScreen = Screen.Welcome }
                    )
                }

                // Screen 04: Router Login and Authorization
                Screen.RouterLogin -> {
                    RouterLoginScreen(
                        initialGatewayIp = wifiInfo.gatewayIp.ifBlank { "192.168.0.1" },
                        detectedManufacturer = authDialogSignature?.manufacturer ?: "TP-Link",
                        isAuthenticating = isConnecting,
                        errorMessage = connectionTestState?.message?.takeIf { connectionTestState?.success == false },
                        onAuthenticate = { ip, user, pass ->
                            val sig = authDialogSignature ?: com.example.network.DiscoveredRouterSignature(
                                gatewayIp = ip,
                                port = 80,
                                useHttps = false,
                                manufacturer = "TP-Link",
                                modelGuess = "Archer Series Gateway ($ip)",
                                connectionType = "TPLINK_HTTP",
                                banner = "Local HTTP Gateway",
                                isSupported = true,
                                isPreAccessSupported = false,
                                supportedCapabilitiesSummary = "MAC Blacklist & Client query"
                            )
                            viewModel.authorizeDiscoveredRouter(sig, user, pass)
                            currentScreen = Screen.RouterConnected
                        },
                        onSkipReadOnly = { currentScreen = Screen.Dashboard },
                        onBack = { currentScreen = Screen.WifiDetection }
                    )
                }

                // Screen 05: Router Connected
                Screen.RouterConnected -> {
                    RouterConnectedScreen(
                        router = router,
                        onContinue = { currentScreen = Screen.Dashboard },
                        onDisconnectRouter = {
                            viewModel.disconnectActiveRouter()
                            currentScreen = Screen.WifiDetection
                        }
                    )
                }

                // Screen 06: Home Dashboard
                Screen.Dashboard -> {
                    DashboardScreen(
                        network = network,
                        router = router,
                        policy = securityPolicy,
                        devices = devices,
                        pendingCount = pendingCount,
                        recentEvents = securityEvents,
                        wifiInfo = wifiInfo,
                        autoLinkStatus = autoLinkStatus,
                        activeAuthSignature = authDialogSignature,
                        onRefreshWifi = { viewModel.triggerAutoDetectionAndLinking() },
                        onAuthorizeRouter = { sig, user, pass -> viewModel.authorizeDiscoveredRouter(sig, user, pass) },
                        onDismissAuthDialog = { viewModel.dismissAuthDialog() },
                        onNavigateToRouterWizard = { currentScreen = Screen.RouterLogin },
                        onNavigateToDevices = { currentScreen = Screen.Devices },
                        onNavigateToPendingApprovals = { currentScreen = Screen.PendingApprovals },
                        onNavigateToSecurityPolicy = { currentScreen = Screen.SecurityPolicy },
                        onNavigateToGuestAccess = { currentScreen = Screen.GuestNetwork },
                        onNavigateToActivityHistory = { currentScreen = Screen.ActivityLog },
                        onNavigateToDiagnostics = { currentScreen = Screen.NetworkHealth },
                        onNavigateToCompatibilityCatalog = { currentScreen = Screen.CompatibilityCatalog },
                        onTriggerSimulatedNotification = { currentScreen = Screen.Notifications },
                        onNavigateToAiAssistant = { currentScreen = Screen.AiAssistant },
                        onNavigateToIspPortal = { currentScreen = Screen.IspManagement },
                        onNavigateToMultipleRouters = { currentScreen = Screen.MultipleRouters },
                        onNavigateToNetworkHealth = { currentScreen = Screen.NetworkHealth },
                        onNavigateToParentalControls = { currentScreen = Screen.ParentalControls },
                        onNavigateToSecurityAudit = { currentScreen = Screen.SecurityAudit }
                    )
                }

                // Screen 07: Connected Devices
                Screen.Devices -> {
                    DevicesScreen(
                        devices = devices,
                        isScanning = isScanningDevices,
                        onScanDevices = { viewModel.scanSubnetDevices() },
                        onSelectDevice = { dev ->
                            viewModel.selectDevice(dev)
                            currentScreen = Screen.DeviceDetail
                        },
                        onBack = { currentScreen = Screen.Dashboard }
                    )
                }

                // Screen 08: Device Details
                Screen.DeviceDetail -> {
                    if (selectedDevice != null) {
                        DeviceDetailScreen(
                            device = selectedDevice!!,
                            onBack = { currentScreen = Screen.Devices },
                            onUpdateDevice = { updated -> viewModel.updateDevice(updated) },
                            onRevokeAccess = { id ->
                                currentScreen = Screen.BlockDevice
                            }
                        )
                    } else {
                        currentScreen = Screen.Devices
                    }
                }

                // Screen 09: Block / Unblock Device
                Screen.BlockDevice -> {
                    BlockDeviceScreen(
                        device = selectedDevice,
                        onConfirmBlock = { id ->
                            viewModel.revokeDevice(id)
                            currentScreen = Screen.Devices
                        },
                        onConfirmUnblock = { id ->
                            viewModel.unblockDevice(id)
                            currentScreen = Screen.Devices
                        },
                        onCancel = { currentScreen = Screen.DeviceDetail }
                    )
                }

                // Screen 10: Security Audit
                Screen.SecurityAudit -> {
                    SecurityAuditScreen(
                        router = router,
                        findings = auditFindings,
                        parentalProfiles = parentalProfiles,
                        onRemediateFinding = { id -> viewModel.remediateSecurityFinding(id) },
                        onNavigateToPolicy = { currentScreen = Screen.SecurityPolicy },
                        onNavigateToAiAssistant = { currentScreen = Screen.AiAssistant },
                        onNavigateToParentalControls = { currentScreen = Screen.ParentalControls },
                        onBack = { currentScreen = Screen.Dashboard }
                    )
                }

                // Screen 11: Network Health
                Screen.NetworkHealth -> {
                    NetworkHealthScreen(
                        router = router,
                        wifiInfo = wifiInfo,
                        devices = devices,
                        onBack = { currentScreen = Screen.Dashboard }
                    )
                }

                // Screen 12: Activity Log
                Screen.ActivityLog, Screen.ActivityHistory -> {
                    ActivityHistoryScreen(
                        events = securityEvents,
                        onBack = { currentScreen = Screen.Dashboard }
                    )
                }

                // Screen 13: Multiple Routers
                Screen.MultipleRouters -> {
                    MultipleRoutersScreen(
                        routers = allRouters,
                        activeRouter = router,
                        onSwitchRouter = { id -> viewModel.switchActiveRouter(id) },
                        onRemoveRouter = { id -> viewModel.removeRouter(id) },
                        onAddNewRouter = { currentScreen = Screen.WifiDetection },
                        onBack = { currentScreen = Screen.Dashboard }
                    )
                }

                // Screen 14: Guest Network
                Screen.GuestNetwork, Screen.GuestAccess -> {
                    GuestAccessScreen(
                        guestPasses = guestPasses,
                        onCreatePass = { name, hours -> viewModel.createGuestPass(name, hours) },
                        onRevokePass = { id -> viewModel.revokeGuestPass(id) },
                        onBack = { currentScreen = Screen.Dashboard }
                    )
                }

                // Screen 15: Parental Controls
                Screen.ParentalControls -> {
                    ParentalControlsScreen(
                        router = router,
                        profiles = parentalProfiles,
                        devices = devices,
                        onSaveProfile = { profile -> viewModel.saveParentalProfile(profile) },
                        onDeleteProfile = { id -> viewModel.deleteParentalProfile(id) },
                        onBack = { currentScreen = Screen.Dashboard }
                    )
                }

                // Screen 16: AI Security Assistant
                Screen.AiAssistant -> {
                    AiAssistantScreen(
                        router = router,
                        devices = devices,
                        findings = auditFindings,
                        onBack = { currentScreen = Screen.Dashboard }
                    )
                }

                // Screen 17: ISP Management
                Screen.IspManagement, Screen.IspPortal -> {
                    IspPortalScreen(
                        organizations = ispOrganizations,
                        customers = ispCustomers,
                        onBack = { currentScreen = Screen.Dashboard }
                    )
                }

                // Screen 18: Notifications
                Screen.Notifications -> {
                    NotificationsScreen(
                        events = securityEvents,
                        onNavigateToDevice = { mac ->
                            val d = devices.find { it.macAddress.equals(mac, true) }
                            if (d != null) {
                                viewModel.selectDevice(d)
                                currentScreen = Screen.DeviceDetail
                            } else {
                                currentScreen = Screen.Devices
                            }
                        },
                        onNavigateToAudit = { currentScreen = Screen.SecurityAudit },
                        onBack = { currentScreen = Screen.Dashboard }
                    )
                }

                // Screen 19: Settings
                Screen.Settings -> {
                    SettingsScreen(
                        darkTheme = darkTheme,
                        onToggleTheme = { viewModel.toggleTheme() },
                        onBack = { currentScreen = Screen.Dashboard },
                        onNavigateToProfile = { currentScreen = Screen.Profile },
                        onNavigateToAiAssistant = { currentScreen = Screen.AiAssistant },
                        onNavigateToMultipleRouters = { currentScreen = Screen.MultipleRouters },
                        onNavigateToIspPortal = { currentScreen = Screen.IspManagement },
                        onNavigateToNetworkHealth = { currentScreen = Screen.NetworkHealth },
                        onNavigateToParentalControls = { currentScreen = Screen.ParentalControls },
                        onNavigateToSecurityAudit = { currentScreen = Screen.SecurityAudit },
                        onNavigateToHelp = { currentScreen = Screen.HelpTroubleshooting },
                        onNavigateToAdmin = { currentScreen = Screen.AdminManagement }
                    )
                }

                // Screen 20: Optional Account / Login Info
                Screen.OptionalAccount, Screen.Auth -> {
                    OptionalAccountScreen(
                        onContinueLocalMode = { currentScreen = Screen.Dashboard },
                        onLoginSubmit = { email, role -> viewModel.login(email, "ISP Engineer", role) },
                        onBack = { currentScreen = Screen.Dashboard }
                    )
                }

                // Compatibility Sub-Screens
                Screen.RouterWizard -> {
                    RouterWizardScreen(
                        isConnecting = isConnecting,
                        testResult = connectionTestState,
                        onCancel = { currentScreen = Screen.Dashboard },
                        onSaveAndConnect = { newRouter ->
                            viewModel.connectNewRouter(newRouter)
                            currentScreen = Screen.Dashboard
                        }
                    )
                }

                Screen.CompatibilityCatalog -> {
                    CompatibilityCatalogScreen(
                        models = compatibilityModels,
                        onBack = { currentScreen = Screen.Dashboard },
                        onNavigateToUnsupportedHelp = { currentScreen = Screen.UnsupportedRouter }
                    )
                }

                Screen.PendingApprovals -> {
                    PendingApprovalsScreen(
                        requests = pendingRequests,
                        onApprove = { id -> viewModel.approveRequest(id) },
                        onReject = { id -> viewModel.rejectRequest(id) },
                        onBack = { currentScreen = Screen.Dashboard }
                    )
                }

                Screen.SecurityPolicy -> {
                    SecurityPolicyScreen(
                        policy = securityPolicy,
                        router = router,
                        onUpdatePolicy = { pol -> viewModel.updatePolicy(pol) },
                        onBack = { currentScreen = Screen.Dashboard }
                    )
                }

                Screen.Diagnostics -> {
                    DiagnosticsScreen(
                        router = router,
                        health = lastDiagnosticHealth,
                        onRunDiagnostics = { viewModel.runDiagnostics() },
                        onBack = { currentScreen = Screen.Dashboard }
                    )
                }

                Screen.Profile -> {
                    ProfileScreen(
                        user = user,
                        onLogout = {
                            viewModel.logout()
                            currentScreen = Screen.OptionalAccount
                        },
                        onBack = { currentScreen = Screen.Dashboard }
                    )
                }

                Screen.HelpTroubleshooting -> {
                    HelpTroubleshootingScreen(
                        onBack = { currentScreen = Screen.Dashboard }
                    )
                }

                Screen.UnsupportedRouter -> {
                    UnsupportedRouterScreen(
                        onBack = { currentScreen = Screen.CompatibilityCatalog },
                        onSubmitRequest = { mfg, model, isp, email, notes ->
                            viewModel.submitCompatibilityRequest(mfg, model, isp, email, notes)
                        }
                    )
                }

                Screen.ErrorRecovery -> {
                    ErrorRecoveryScreen(
                        errorMessage = "Router communication timeout",
                        onRetry = {
                            viewModel.runDiagnostics()
                            currentScreen = Screen.Dashboard
                        },
                        onBack = { currentScreen = Screen.Dashboard }
                    )
                }

                Screen.AdminManagement -> {
                    AdminManagementScreen(
                        user = user,
                        models = compatibilityModels,
                        requests = compatibilityRequests,
                        onAddModel = { model -> viewModel.addCompatibilityModel(model) },
                        onBack = { currentScreen = Screen.Dashboard }
                    )
                }

                Screen.OwnershipConsent -> {
                    OwnershipConsentScreen(
                        networkSsid = network?.ssid ?: "Dhaka_Secure_Mesh",
                        onConsentApproved = {
                            viewModel.confirmOwnershipConsent()
                            currentScreen = Screen.Dashboard
                        }
                    )
                }
            }
        }
    }
}
