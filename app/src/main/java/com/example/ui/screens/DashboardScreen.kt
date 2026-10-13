package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.DeviceEntity
import com.example.data.local.entity.NetworkEntity
import com.example.data.local.entity.RouterEntity
import com.example.data.local.entity.SecurityEventEntity
import com.example.data.local.entity.SecurityPolicyEntity
import com.example.localization.LanguageManager
import com.example.network.AutoLinkStatus
import com.example.network.DiscoveredRouterSignature
import com.example.network.WifiNetworkInfo
import com.example.ui.components.HonestCapabilityItem
import com.example.ui.components.MetricStatCard
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusBadge
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DeepNavyDark
import com.example.ui.theme.EmeraldTeal
import com.example.ui.theme.MidnightSurface
import com.example.ui.theme.SurfaceVariantDark
import com.example.ui.theme.WarningAmber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
    network: NetworkEntity?,
    router: RouterEntity?,
    policy: SecurityPolicyEntity?,
    devices: List<DeviceEntity>,
    pendingCount: Int,
    recentEvents: List<SecurityEventEntity>,
    wifiInfo: WifiNetworkInfo,
    autoLinkStatus: AutoLinkStatus,
    activeAuthSignature: DiscoveredRouterSignature?,
    onRefreshWifi: () -> Unit,
    onAuthorizeRouter: (DiscoveredRouterSignature, String, String) -> Unit,
    onDismissAuthDialog: () -> Unit,
    onNavigateToRouterWizard: () -> Unit,
    onNavigateToDevices: () -> Unit,
    onNavigateToPendingApprovals: () -> Unit,
    onNavigateToSecurityPolicy: () -> Unit,
    onNavigateToGuestAccess: () -> Unit,
    onNavigateToActivityHistory: () -> Unit,
    onNavigateToDiagnostics: () -> Unit,
    onNavigateToCompatibilityCatalog: () -> Unit,
    onTriggerSimulatedNotification: () -> Unit,
    onNavigateToAiAssistant: () -> Unit = {},
    onNavigateToIspPortal: () -> Unit = {},
    onNavigateToMultipleRouters: () -> Unit = {},
    onNavigateToNetworkHealth: () -> Unit = {},
    onNavigateToParentalControls: () -> Unit = {},
    onNavigateToSecurityAudit: () -> Unit = {}
) {
    val authorizedCount = devices.count { it.authorizationState == "AUTHORIZED" }
    val unknownCount = devices.count { it.authorizationState == "UNKNOWN" || it.authorizationState == "PENDING" }
    val blockedCount = devices.count { it.authorizationState == "BLOCKED" || it.authorizationState == "REVOKED" }

    var authUsername by remember { mutableStateOf("root") }
    var authPassword by remember { mutableStateOf("") }
    var showManualAuthDialog by remember { mutableStateOf(false) }

    // Auto Authorization Modal when router is discovered
    if (activeAuthSignature != null || showManualAuthDialog) {
        val signature = activeAuthSignature ?: DiscoveredRouterSignature(
            gatewayIp = wifiInfo.gatewayIp,
            port = 80,
            useHttps = false,
            manufacturer = "OpenWrt / Router",
            modelGuess = "Gateway at ${wifiInfo.gatewayIp}",
            connectionType = "OPENWRT_RPC",
            banner = "Local Gateway",
            isSupported = true,
            isPreAccessSupported = true,
            supportedCapabilitiesSummary = "Pre-Access MAC allowlist & station telemetry"
        )

        AlertDialog(
            onDismissRequest = {
                showManualAuthDialog = false
                onDismissAuthDialog()
            },
            containerColor = MidnightSurface,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = EmeraldTeal)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Authorize Router Linking",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = "Discovered: ${signature.manufacturer} ${signature.modelGuess}",
                        fontWeight = FontWeight.Bold,
                        color = CyberCyan,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "Gateway: ${signature.gatewayIp}:${signature.port} • ${signature.banner}",
                        fontSize = 11.sp,
                        color = Color(0xFFCBD5E1)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = signature.supportedCapabilitiesSummary,
                        fontSize = 10.sp,
                        color = Color(0xFF94A3B8)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Enter admin credentials to authorize secure management. Passwords are never sent off-device or stored unencrypted.",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = authUsername,
                        onValueChange = { authUsername = it },
                        label = { Text("Admin Username (root/admin)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldTeal,
                            unfocusedBorderColor = Color(0xFF334155),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = authPassword,
                        onValueChange = { authPassword = it },
                        label = { Text("Password or API Token") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldTeal,
                            unfocusedBorderColor = Color(0xFF334155),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onAuthorizeRouter(signature, authUsername, authPassword)
                        showManualAuthDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal)
                ) {
                    Text("Authorize & Link", color = Color(0xFF022C22), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = {
                    showManualAuthDialog = false
                    onDismissAuthDialog()
                }) {
                    Text("Cancel", color = Color.White)
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyDark)
            .padding(horizontal = 20.dp)
            .testTag("dashboard_screen")
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            // Automatic Wi-Fi Linking & Detection Banner
            AutoWifiLinkingHeader(
                wifiInfo = wifiInfo,
                autoLinkStatus = autoLinkStatus,
                onRefresh = onRefreshWifi,
                onOpenAuth = { showManualAuthDialog = true }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Top Status Bar: Network Name & Notification Test
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (wifiInfo.isConnected) wifiInfo.ssid else (network?.ssid ?: "No Wi-Fi Connected"),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Gateway: ${wifiInfo.gatewayIp} • Subnet: ${wifiInfo.subnetMask}",
                        fontSize = 12.sp,
                        color = CyberCyan
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (pendingCount > 0) {
                        Surface(
                            shape = CircleShape,
                            color = AlertRed,
                            modifier = Modifier
                                .clickable { onNavigateToPendingApprovals() }
                                .padding(end = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("$pendingCount PENDING", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    IconButton(
                        onClick = onTriggerSimulatedNotification,
                        modifier = Modifier.testTag("simulate_notification_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Test Notification",
                            tint = Color(0xFF94A3B8)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main Security & Router Enclave Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MidnightSurface),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF1E293B),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Router,
                                        contentDescription = null,
                                        tint = EmeraldTeal,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = router?.name ?: "Router Not Linked Yet",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "${router?.manufacturer ?: "Auto-detecting"} • ${router?.model ?: "Probing gateway"}",
                                    fontSize = 12.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            }
                        }
                        StatusBadge(status = router?.status ?: "SETUP_REQUIRED")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Honest Capability Checklist
                    Text(
                        text = "Hardware Access-Control Capabilities:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    HonestCapabilityItem(
                        title = "Pre-Access Station Approval",
                        supported = router?.isPreAccessSupported ?: false,
                        note = if (router?.isPreAccessSupported == true) "802.11w PMF & dynamic access-list enforced" else "Hardware lacks pre-association quarantine; post-connection MAC block enforced instead"
                    )

                    HonestCapabilityItem(
                        title = "Hardware MAC Blacklisting / Revocation",
                        supported = router?.isMacFilterSupported ?: true,
                        note = "Enforces instant disassociation upon revocation"
                    )

                    HonestCapabilityItem(
                        title = "Isolated Guest Subnet Support",
                        supported = router?.isGuestNetworkSupported ?: true,
                        note = "Isolates guest stations from private LAN"
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Latency: ${router?.latencyMs ?: 0}ms • ${router?.ipAddress ?: wifiInfo.gatewayIp}",
                            fontSize = 11.sp,
                            color = CyberCyan
                        )

                        Text(
                            text = "Diagnostics",
                            color = CyberCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .clickable { onNavigateToDiagnostics() }
                                .padding(4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Metric Counters Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricStatCard(
                    title = "Authorized",
                    value = authorizedCount.toString(),
                    icon = Icons.Default.CheckCircle,
                    color = EmeraldTeal,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToDevices
                )

                MetricStatCard(
                    title = "Pending",
                    value = pendingCount.toString(),
                    icon = Icons.Default.Warning,
                    color = if (pendingCount > 0) WarningAmber else Color(0xFF64748B),
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToPendingApprovals
                )

                MetricStatCard(
                    title = "Blocked",
                    value = blockedCount.toString(),
                    icon = Icons.Default.Shield,
                    color = if (blockedCount > 0) AlertRed else Color(0xFF64748B),
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToDevices
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Actions Bar
            SectionHeader(title = "Primary Management")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    color = MidnightSurface,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToRouterWizard() }
                        .testTag("action_connect_router")
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Router, contentDescription = null, tint = EmeraldTeal, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Connect Router", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                Surface(
                    color = MidnightSurface,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToGuestAccess() }
                        .testTag("action_guest_access")
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.QrCode, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Guest Access", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                Surface(
                    color = MidnightSurface,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToSecurityPolicy() }
                        .testTag("action_security_policy")
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Policy Mode", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                Surface(
                    color = MidnightSurface,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToCompatibilityCatalog() }
                        .testTag("action_compatibility")
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Devices, contentDescription = null, tint = Color(0xFFA78BFA), modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("BD Routers", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Advanced Security & Diagnostics Row
            SectionHeader(title = "Advanced Security Suite")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    color = MidnightSurface,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToAiAssistant() }
                        .testTag("action_ai_assistant")
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("AI Security", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                Surface(
                    color = MidnightSurface,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToSecurityAudit() }
                        .testTag("action_security_audit")
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = AlertRed, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Vulnerability", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                Surface(
                    color = MidnightSurface,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToNetworkHealth() }
                        .testTag("action_network_health")
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Speed, contentDescription = null, tint = EmeraldTeal, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Speed Test", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                Surface(
                    color = MidnightSurface,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToParentalControls() }
                        .testTag("action_parental_controls")
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.ChildCare, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Curfew & Kids", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Recent Security Events Section
            SectionHeader(
                title = "Recent Security Activity",
                actionText = "View All",
                onActionClick = onNavigateToActivityHistory
            )
        }

        if (recentEvents.isEmpty()) {
            item {
                Text(
                    text = "No security events recorded yet.",
                    color = Color(0xFF64748B),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        } else {
            items(recentEvents.take(4)) { event ->
                val timeStr = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(event.timestamp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = MidnightSurface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val iconColor = when (event.severity) {
                            "CRITICAL" -> AlertRed
                            "WARNING" -> WarningAmber
                            else -> EmeraldTeal
                        }
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(iconColor)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = event.title,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                            Text(
                                text = event.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF94A3B8),
                                maxLines = 1
                            )
                        }
                        Text(
                            text = timeStr,
                            fontSize = 10.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
fun AutoWifiLinkingHeader(
    wifiInfo: WifiNetworkInfo,
    autoLinkStatus: AutoLinkStatus,
    onRefresh: () -> Unit,
    onOpenAuth: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = when (autoLinkStatus) {
                is AutoLinkStatus.WifiNotConnected -> Color(0xFF451A03)
                is AutoLinkStatus.AuthRequired -> Color(0xFF0F2B3C)
                is AutoLinkStatus.Linked -> Color(0xFF064E3B)
                is AutoLinkStatus.Detecting -> Color(0xFF1E293B)
                is AutoLinkStatus.Error -> Color(0xFF7F1D1D)
                else -> Color(0xFF1E293B)
            }
        ),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (wifiInfo.isConnected) Icons.Default.Wifi else Icons.Default.WifiOff,
                        contentDescription = null,
                        tint = when (autoLinkStatus) {
                            is AutoLinkStatus.WifiNotConnected -> WarningAmber
                            is AutoLinkStatus.AuthRequired -> CyberCyan
                            is AutoLinkStatus.Linked -> EmeraldTeal
                            is AutoLinkStatus.Error -> AlertRed
                            else -> Color.White
                        },
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when (autoLinkStatus) {
                            is AutoLinkStatus.WifiNotConnected -> "Wi-Fi Disconnected"
                            is AutoLinkStatus.Detecting -> "Auto-Detecting Router..."
                            is AutoLinkStatus.AuthRequired -> "Router Discovered (Auth Required)"
                            is AutoLinkStatus.Linked -> "Router Linked & Protected"
                            is AutoLinkStatus.Error -> "Auto-Link Error"
                            else -> "Wi-Fi Connected: ${wifiInfo.ssid}"
                        },
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 13.sp
                    )
                }

                IconButton(
                    onClick = onRefresh,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color(0xFFCBD5E1), modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            when (autoLinkStatus) {
                is AutoLinkStatus.WifiNotConnected -> {
                    Text(
                        text = "Your mobile is not connected to a Wi-Fi network. Connect to your home or office Wi-Fi to monitor router security.",
                        fontSize = 11.sp,
                        color = Color(0xFFFDE68A)
                    )
                }
                is AutoLinkStatus.Detecting -> {
                    Text(
                        text = autoLinkStatus.step,
                        fontSize = 11.sp,
                        color = CyberCyan
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                        color = CyberCyan,
                        trackColor = Color(0xFF334155)
                    )
                }
                is AutoLinkStatus.AuthRequired -> {
                    Text(
                        text = "Identified ${autoLinkStatus.signature.manufacturer} gateway at ${autoLinkStatus.signature.gatewayIp}. Tap below to authorize secure control.",
                        fontSize = 11.sp,
                        color = Color(0xFFA5F3FC)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = onOpenAuth,
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF083344), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Authorize & Complete Link", color = Color(0xFF083344), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
                is AutoLinkStatus.Linked -> {
                    Text(
                        text = "${autoLinkStatus.message} • ${if (autoLinkStatus.isLimited) "Limited Features (WLAN Blacklist Mode)" else "Full Zero-Trust Protection"}",
                        fontSize = 11.sp,
                        color = Color(0xFFA7F3D0)
                    )
                }
                is AutoLinkStatus.Error -> {
                    Text(
                        text = autoLinkStatus.message,
                        fontSize = 11.sp,
                        color = Color(0xFFFECACA)
                    )
                }
                else -> {
                    Text(
                        text = "SSID: ${wifiInfo.ssid} • Gateway: ${wifiInfo.gatewayIp} • Link Speed: ${wifiInfo.linkSpeedMbps} Mbps",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }
        }
    }
}
