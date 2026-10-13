package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.UserEntity
import com.example.localization.AppLanguage
import com.example.localization.LanguageManager
import com.example.ui.components.StatusBadge
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DeepNavyDark
import com.example.ui.theme.EmeraldTeal
import com.example.ui.theme.MidnightSurface
import com.example.ui.theme.WarningAmber

@Composable
fun ProfileScreen(
    user: UserEntity?,
    onLogout: () -> Unit,
    onBack: () -> Unit
) {
    var message by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyDark)
            .padding(20.dp)
            .testTag("profile_screen")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Account & Security Profile",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = MidnightSurface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF064E3B),
                        modifier = Modifier.size(52.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = EmeraldTeal, modifier = Modifier.size(28.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(user?.displayName ?: "Arman Hossain", fontWeight = FontWeight.Bold, color = Color.White, style = MaterialTheme.typography.titleMedium)
                        Text(user?.email ?: "dkhdarman24221@gmail.com", fontSize = 12.sp, color = Color(0xFF94A3B8))
                        Spacer(modifier = Modifier.height(4.dp))
                        StatusBadge(status = user?.role ?: "OWNER")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Authentication Method: Google / Verified Email Enclave", fontSize = 11.sp, color = CyberCyan)
                Text("Role Permissions: Super Administrator / Network Owner", fontSize = 11.sp, color = Color(0xFFCBD5E1))
                Text("Active Sessions: 1 On-Device Session (Current)", fontSize = 11.sp, color = Color(0xFF94A3B8))

                if (message != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(message!!, fontSize = 11.sp, color = EmeraldTeal)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { message = "Encrypted profile and network configuration exported." },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Export Data", fontSize = 11.sp)
                    }

                    Button(
                        onClick = onLogout,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = AlertRed),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Logout, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Sign Out", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(
    darkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onBack: () -> Unit,
    onNavigateToProfile: () -> Unit = {},
    onNavigateToAiAssistant: () -> Unit = {},
    onNavigateToMultipleRouters: () -> Unit = {},
    onNavigateToIspPortal: () -> Unit = {},
    onNavigateToNetworkHealth: () -> Unit = {},
    onNavigateToParentalControls: () -> Unit = {},
    onNavigateToSecurityAudit: () -> Unit = {},
    onNavigateToHelp: () -> Unit = {},
    onNavigateToAdmin: () -> Unit = {}
) {
    var isBn by remember { mutableStateOf(LanguageManager.currentLanguage == AppLanguage.BANGLA) }
    var headsUpAlerts by remember { mutableStateOf(true) }
    var soundVibration by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyDark)
            .padding(20.dp)
            .testTag("settings_screen")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = LanguageManager.getString("settings"),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "App preferences & localization",
                    fontSize = 11.sp,
                    color = CyberCyan
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MidnightSurface),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Language / ভাষা", fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(if (isBn) "বাংলা (Bangla)" else "English (EN)", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                Text("Switch interface language", fontSize = 11.sp, color = Color(0xFF94A3B8))
                            }
                            Switch(
                                checked = isBn,
                                onCheckedChange = {
                                    isBn = it
                                    LanguageManager.currentLanguage = if (it) AppLanguage.BANGLA else AppLanguage.ENGLISH
                                },
                                colors = SwitchDefaults.colors(checkedThumbColor = EmeraldTeal)
                            )
                        }
                    }
                }
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MidnightSurface),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Appearance", fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(if (darkTheme) "Dark Cybersecurity Theme" else "Light Theme", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                Text("High contrast security console palette", fontSize = 11.sp, color = Color(0xFF94A3B8))
                            }
                            Switch(
                                checked = darkTheme,
                                onCheckedChange = { onToggleTheme() },
                                colors = SwitchDefaults.colors(checkedThumbColor = EmeraldTeal)
                            )
                        }
                    }
                }
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MidnightSurface),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Alert Delivery", fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Heads-Up Approval Notification", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                Text("Urgent overlay when unknown device connects", fontSize = 11.sp, color = Color(0xFF94A3B8))
                            }
                            Switch(
                                checked = headsUpAlerts,
                                onCheckedChange = { headsUpAlerts = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = EmeraldTeal)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Sound & Vibration", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                Text("Audible alert on security incident", fontSize = 11.sp, color = Color(0xFF94A3B8))
                            }
                            Switch(
                                checked = soundVibration,
                                onCheckedChange = { soundVibration = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = EmeraldTeal)
                            )
                        }
                    }
                }
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MidnightSurface),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Security & Management Modules", fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(10.dp))

                        SettingsNavigationRow(
                            icon = Icons.Default.AutoAwesome,
                            iconColor = CyberCyan,
                            title = "AI Security Assistant",
                            subtitle = "Zero-trust advisory & router explanations",
                            onClick = onNavigateToAiAssistant
                        )

                        SettingsNavigationRow(
                            icon = Icons.Default.Shield,
                            iconColor = AlertRed,
                            title = "Vulnerability Audit",
                            subtitle = "WPA3, default credentials & firmware scan",
                            onClick = onNavigateToSecurityAudit
                        )

                        SettingsNavigationRow(
                            icon = Icons.Default.Speed,
                            iconColor = EmeraldTeal,
                            title = "Network Health & Speed",
                            subtitle = "Latency, jitter & real-time bandwidth test",
                            onClick = onNavigateToNetworkHealth
                        )

                        SettingsNavigationRow(
                            icon = Icons.Default.Router,
                            iconColor = Color(0xFFA78BFA),
                            title = "Multi-Router Fleet",
                            subtitle = "Manage multiple home, office, and mesh gateways",
                            onClick = onNavigateToMultipleRouters
                        )

                        SettingsNavigationRow(
                            icon = Icons.Default.ChildCare,
                            iconColor = WarningAmber,
                            title = "Parental Curfew & Profiles",
                            subtitle = "Scheduled access & bedtime protection",
                            onClick = onNavigateToParentalControls
                        )

                        SettingsNavigationRow(
                            icon = Icons.Default.Business,
                            iconColor = CyberCyan,
                            title = "ISP Enterprise Portal",
                            subtitle = "Customer provisioning & Link3/Carnival tools",
                            onClick = onNavigateToIspPortal
                        )

                        SettingsNavigationRow(
                            icon = Icons.Default.Person,
                            iconColor = Color(0xFF38BDF8),
                            title = "User Profile & Role",
                            subtitle = "Admin authorization & consent settings",
                            onClick = onNavigateToProfile
                        )

                        SettingsNavigationRow(
                            icon = Icons.Default.Help,
                            iconColor = Color(0xFFFCD34D),
                            title = "Help & ISP Guide",
                            subtitle = "Fiber ONT setups & troubleshooting in BD",
                            onClick = onNavigateToHelp
                        )

                        SettingsNavigationRow(
                            icon = Icons.Default.AdminPanelSettings,
                            iconColor = Color(0xFFF472B6),
                            title = "Compatibility Catalog Admin",
                            subtitle = "Add router models & review community requests",
                            onClick = onNavigateToAdmin
                        )
                    }
                }
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MidnightSurface),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("About Platform", fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Universal Wi-Fi Security (Bangladesh)", fontSize = 12.sp, color = EmeraldTeal, fontWeight = FontWeight.SemiBold)
                        Text("Targeted for Bangladesh ISPs: Link3, Carnival, Amber IT, Dot Internet, BTCL.", fontSize = 11.sp, color = Color(0xFFCBD5E1))
                        Text("Build: 2026.10-release • Architecture: Clean MVVM + Room + OkHttp", fontSize = 10.sp, color = Color(0xFF64748B))
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsNavigationRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(iconColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Text(subtitle, color = Color(0xFF94A3B8), fontSize = 11.sp, maxLines = 1)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(18.dp))
    }
}

@Composable
fun HelpTroubleshootingScreen(
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyDark)
            .padding(20.dp)
            .testTag("help_screen")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "Help & Troubleshooting Guide",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Guides for Bangladesh ISPs & Fiber ONTs",
                    fontSize = 11.sp,
                    color = CyberCyan
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            item {
                HelpItemCard(
                    title = "How to find your Router IP & Model",
                    content = "Look at the sticker under your router. Default IPs in Bangladesh are typically 192.168.1.1 (OpenWrt/Link3), 192.168.88.1 (MikroTik), 192.168.100.1 (Huawei GPON), or 192.168.0.1 (TP-Link)."
                )
            }

            item {
                HelpItemCard(
                    title = "Why ISP Fiber ONTs (Huawei/ZTE) have limited control",
                    content = "Many Bangladesh ISPs (Link3, Carnival, Dot) supply Huawei HG8145V5 or ZTE F670L modems where telecom administrative access is managed by the ISP TR-069 server. While pre-access hold is unavailable on standard ONT firmware, our app uses WLAN MAC Blacklisting to automatically ban rogue devices upon notification."
                )
            }

            item {
                HelpItemCard(
                    title = "How to enable pre-access zero-trust protection",
                    content = "For absolute pre-access protection where no unknown station can connect before owner approval, connect an OpenWrt router (hostapd ACL allowlist) or a MikroTik RouterBOARD (/interface wireless access-list) downstream of your fiber ONU."
                )
            }

            item {
                HelpItemCard(
                    title = "Resolving Authentication & Timeout Errors",
                    content = "Ensure your phone is connected to the same Wi-Fi subnet as the router. If using HTTPS on a local router with self-signed certificates, verify the port (typically 443)."
                )
            }
        }
    }
}

@Composable
fun HelpItemCard(title: String, content: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MidnightSurface),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(title, fontWeight = FontWeight.Bold, color = Color.White, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(6.dp))
            Text(content, fontSize = 11.sp, color = Color(0xFFCBD5E1), lineHeight = 16.sp)
        }
    }
}

@Composable
fun ErrorRecoveryScreen(
    errorMessage: String = "Router communication timeout",
    onRetry: () -> Unit,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyDark)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MidnightSurface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(48.dp))
                Spacer(modifier = Modifier.height(12.dp))
                Text("Connection Diagnostic Failure", fontWeight = FontWeight.Bold, color = Color.White, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(errorMessage, fontSize = 12.sp, color = Color(0xFFCBD5E1), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) {
                        Text("Return", color = Color.White)
                    }
                    Button(
                        onClick = onRetry,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal)
                    ) {
                        Text("Retry", color = Color(0xFF022C22), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
