package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.network.DiscoveredRouterSignature
import com.example.network.WifiNetworkInfo
import com.example.ui.components.InfoCard
import com.example.ui.components.OutlinedButtonCyber
import com.example.ui.components.PrimaryButton
import com.example.ui.components.ScreenHeader
import com.example.ui.components.StatusChip
import com.example.ui.theme.AlertRed
import com.example.ui.theme.BorderCyan
import com.example.ui.theme.CardBg
import com.example.ui.theme.MutedText
import com.example.ui.theme.PrimaryBg
import com.example.ui.theme.PrimaryCyan
import com.example.ui.theme.PrimaryText
import com.example.ui.theme.SecondaryText
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningAmber
import kotlinx.coroutines.delay

@Composable
fun WifiDetectionScreen(
    wifiInfo: WifiNetworkInfo,
    discoveredSignature: DiscoveredRouterSignature?,
    onRefreshDetection: () -> Unit,
    onProceedToLogin: (gatewayIp: String) -> Unit,
    onManualSetup: () -> Unit,
    onCancel: () -> Unit
) {
    var detectionStage by remember { mutableStateOf("Querying Android network interfaces...") }
    var progress by remember { mutableFloatStateOf(0.3f) }
    var isDone by remember { mutableStateOf(false) }

    LaunchedEffect(wifiInfo, discoveredSignature) {
        if (!wifiInfo.isConnected) {
            detectionStage = "Wi-Fi is not connected. Connect to Wi-Fi to manage router."
            progress = 0.2f
        } else {
            delay(400)
            detectionStage = "Probing default gateway (${wifiInfo.gatewayIp})..."
            progress = 0.65f
            delay(500)
            detectionStage = "Identified router signature: ${discoveredSignature?.manufacturer ?: "Gateway"}"
            progress = 1.0f
            isDone = true
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBg)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
            .testTag("wifi_detection_screen"),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            ScreenHeader(
                title = "Wi-Fi Network Detection",
                subtitle = "Screen 03: Discovering Local Network & Gateway",
                onBack = onCancel
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Animated Wi-Fi Radar / Detection Graphic
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                // Outer pulsing ring
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .scale(if (!isDone) pulseScale else 1.0f)
                        .clip(CircleShape)
                        .background(Color(0xFF0F395A).copy(alpha = 0.35f))
                        .border(1.dp, PrimaryCyan.copy(alpha = 0.5f), CircleShape)
                )

                // Middle ring
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF092943))
                        .border(2.dp, PrimaryCyan, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (wifiInfo.isConnected) Icons.Default.Wifi else Icons.Default.Warning,
                        contentDescription = "Wi-Fi State",
                        tint = if (wifiInfo.isConnected) SuccessGreen else WarningAmber,
                        modifier = Modifier.size(46.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (wifiInfo.isConnected) SuccessGreen else WarningAmber,
                trackColor = Color(0xFF1E293B)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = detectionStage,
                style = MaterialTheme.typography.bodySmall,
                color = SecondaryText,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Status Row 1: Wi-Fi Connection
            DetectionCard(
                title = "1. Wi-Fi Connection",
                status = if (wifiInfo.isConnected) "CONNECTED" else "DISCONNECTED",
                icon = Icons.Default.Wifi,
                details = listOf(
                    "Network (SSID)" to (if (wifiInfo.isConnected) wifiInfo.ssid else "Not Connected"),
                    "BSSID" to (wifiInfo.bssid?.takeIf { it.isNotBlank() } ?: "Android 12+ Restricted"),
                    "Signal Strength" to "${wifiInfo.linkSpeedMbps} Mbps",
                    "Band / Frequency" to (if (wifiInfo.frequencyMhz > 4900) "5 GHz" else "2.4 GHz")
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Status Row 2: Network Information
            DetectionCard(
                title = "2. Network Information",
                status = if (wifiInfo.gatewayIp.isNotBlank()) "DETECTED" else "PENDING",
                icon = Icons.Default.Info,
                details = listOf(
                    "Gateway Address" to (wifiInfo.gatewayIp.ifBlank { "192.168.0.1" }),
                    "Local Device IP" to (wifiInfo.localIp.ifBlank { "192.168.0.x" }),
                    "Subnet Mask" to (wifiInfo.subnetMask.ifBlank { "255.255.255.0" })
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Status Row 3: Router Discovery
            DetectionCard(
                title = "3. Router Discovery",
                status = if (discoveredSignature != null) "FOUND" else "SEARCHING",
                icon = Icons.Default.Router,
                details = listOf(
                    "Detected Brand" to (discoveredSignature?.manufacturer ?: "Probing..."),
                    "Model Guess" to (discoveredSignature?.modelGuess ?: "Detecting HTTP signatures..."),
                    "Management Port" to "${discoveredSignature?.port ?: 80} (${if (discoveredSignature?.useHttps == true) "HTTPS" else "HTTP"})",
                    "Capability" to (discoveredSignature?.supportedCapabilitiesSummary ?: "Supported API required")
                )
            )
        }

        // Bottom Actions
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            if (wifiInfo.isConnected) {
                PrimaryButton(
                    text = "Authenticate Router (${discoveredSignature?.manufacturer ?: "Gateway"})",
                    onClick = {
                        val ip = wifiInfo.gatewayIp.ifBlank { discoveredSignature?.gatewayIp ?: "192.168.0.1" }
                        onProceedToLogin(ip)
                    },
                    testTag = "proceed_to_login_button"
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButtonCyber(
                    text = "Manual Router IP",
                    onClick = onManualSetup,
                    modifier = Modifier.weight(1f),
                    testTag = "manual_setup_button"
                )

                OutlinedButtonCyber(
                    text = "Cancel",
                    onClick = onCancel,
                    modifier = Modifier.weight(1f),
                    testTag = "cancel_detection_button"
                )
            }
        }
    }
}

@Composable
private fun DetectionCard(
    title: String,
    status: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    details: List<Pair<String, String>>
) {
    InfoCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PrimaryCyan,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryText
                )
            }
            StatusChip(status = status)
        }

        Spacer(modifier = Modifier.height(8.dp))

        details.forEach { (label, value) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = SecondaryText
                )
                Text(
                    text = value,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = PrimaryText
                )
            }
        }
    }
}
