package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.RouterEntity
import com.example.ui.components.InfoCard
import com.example.ui.components.OutlinedButtonCyber
import com.example.ui.components.PrimaryButton
import com.example.ui.components.ScreenHeader
import com.example.ui.components.StatusChip
import com.example.ui.theme.AlertRed
import com.example.ui.theme.BorderCyan
import com.example.ui.theme.PrimaryBg
import com.example.ui.theme.PrimaryCyan
import com.example.ui.theme.PrimaryText
import com.example.ui.theme.SecondaryText
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningAmber

@Composable
fun RouterConnectedScreen(
    router: RouterEntity?,
    onContinue: () -> Unit,
    onDisconnectRouter: () -> Unit
) {
    val brand = router?.manufacturer ?: "TP-Link"
    val model = router?.model ?: "Archer C6 AC1200"
    val ip = router?.ipAddress ?: "192.168.0.1"
    val firmware = router?.firmwareVersion ?: "v1.3.6 Build 20231120"
    val authStatus = router?.status ?: "AUTHORIZED"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBg)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
            .testTag("router_connected_screen"),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            ScreenHeader(
                title = "Router Connected",
                subtitle = "Screen 05: Verified Capabilities & Session Status",
                onBack = onContinue
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Prominent Router Graphic with Success Indicator
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    color = Color(0xFF064E3B),
                    shape = CircleShape,
                    border = androidx.compose.foundation.BorderStroke(2.dp, SuccessGreen),
                    modifier = Modifier.size(96.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Router,
                            contentDescription = "Router Verified",
                            tint = SuccessGreen,
                            modifier = Modifier.size(52.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "$brand $model",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = PrimaryText,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Local Management IP: $ip • Firmware: $firmware",
                style = MaterialTheme.typography.bodySmall,
                color = SecondaryText,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                StatusChip(status = authStatus)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Capability Matrix (Verified Hardware Features)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = PrimaryCyan
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Capability Items
            CapabilityRow(
                title = "Connected Device Monitoring",
                description = "Live real-time ARP and DHCP lease detection",
                supported = true,
                icon = Icons.Default.Devices
            )
            Spacer(modifier = Modifier.height(8.dp))

            CapabilityRow(
                title = "Device Blocking & Blacklisting",
                description = if (brand.contains("MikroTik", true) || brand.contains("OpenWrt", true)) "Hardware Access-List pre-access quarantine" else "MAC Blacklist filtering supported",
                supported = true,
                icon = Icons.Default.Lock
            )
            Spacer(modifier = Modifier.height(8.dp))

            CapabilityRow(
                title = "Guest Wi-Fi Network Control",
                description = "Isolated SSID management with independent password",
                supported = router?.isGuestNetworkSupported == true || brand.contains("TP-Link", true) || brand.contains("OpenWrt", true),
                icon = Icons.Default.Wifi
            )
            Spacer(modifier = Modifier.height(8.dp))

            CapabilityRow(
                title = "Parental Controls & Schedules",
                description = "Client device restriction and bedtime rules",
                supported = true,
                icon = Icons.Default.People
            )
            Spacer(modifier = Modifier.height(8.dp))

            CapabilityRow(
                title = "Automated Security Audit",
                description = "Evaluates WPS vulnerability, encryption, and open WAN ports",
                supported = true,
                icon = Icons.Default.Shield
            )
            Spacer(modifier = Modifier.height(8.dp))

            CapabilityRow(
                title = "Firmware Version & Telemetry",
                description = "Stock retail firmware query supported via HTTP API",
                supported = true,
                icon = Icons.Default.CheckCircle
            )
        }

        // Bottom Actions
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            PrimaryButton(
                text = "Continue to Dashboard",
                onClick = onContinue,
                testTag = "continue_to_dashboard_button"
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButtonCyber(
                text = "Disconnect / Remove Router Session",
                onClick = onDisconnectRouter,
                testTag = "disconnect_router_button"
            )
        }
    }
}

@Composable
private fun CapabilityRow(
    title: String,
    description: String,
    supported: Boolean,
    icon: ImageVector
) {
    InfoCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    color = if (supported) Color(0xFF064E3B) else Color(0xFF451A03),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = title,
                            tint = if (supported) SuccessGreen else WarningAmber,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryText,
                        fontSize = 13.sp
                    )
                    Text(
                        text = description,
                        color = SecondaryText,
                        fontSize = 11.sp
                    )
                }
            }
            StatusChip(status = if (supported) "SUPPORTED" else "UNSUPPORTED")
        }
    }
}
