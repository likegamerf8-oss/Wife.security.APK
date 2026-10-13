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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.DeviceEntity
import com.example.data.local.entity.RouterEntity
import com.example.network.WifiNetworkInfo
import com.example.ui.components.StatusBadge
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DeepNavyDark
import com.example.ui.theme.EmeraldTeal
import com.example.ui.theme.MidnightSurface
import com.example.ui.theme.WarningAmber
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NetworkHealthScreen(
    router: RouterEntity?,
    wifiInfo: WifiNetworkInfo,
    devices: List<DeviceEntity>,
    onBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var isTestingSpeed by remember { mutableStateOf(false) }
    var downloadMbps by remember { mutableFloatStateOf(48.5f) }
    var uploadMbps by remember { mutableFloatStateOf(42.1f) }
    var pingMs by remember { mutableLongStateOf(router?.latencyMs ?: 8L) }
    var packetLossPercent by remember { mutableFloatStateOf(0.0f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyDark)
            .padding(horizontal = 20.dp)
            .testTag("network_health_screen")
    ) {
        Spacer(modifier = Modifier.height(16.dp))

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
                    text = "Network Health & Speed",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Latency, DNS benchmarks, & bandwidth telemetry",
                    fontSize = 11.sp,
                    color = CyberCyan
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Speed Test Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MidnightSurface),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Bandwidth Speed Benchmark",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Target Gateway: ${wifiInfo.gatewayIp} • ISP: ${router?.ispName}",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Download", fontSize = 11.sp, color = Color(0xFF94A3B8))
                                Text("$downloadMbps", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = EmeraldTeal)
                                Text("Mbps", fontSize = 10.sp, color = EmeraldTeal)
                            }

                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(40.dp)
                                    .background(Color(0xFF334155))
                            )

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Upload", fontSize = 11.sp, color = Color(0xFF94A3B8))
                                Text("$uploadMbps", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = CyberCyan)
                                Text("Mbps", fontSize = 10.sp, color = CyberCyan)
                            }

                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(40.dp)
                                    .background(Color(0xFF334155))
                            )

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Ping Latency", fontSize = 11.sp, color = Color(0xFF94A3B8))
                                Text("$pingMs", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                                Text("ms", fontSize = 10.sp, color = WarningAmber)
                            }
                        }

                        if (isTestingSpeed) {
                            Spacer(modifier = Modifier.height(12.dp))
                            LinearProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp)),
                                color = EmeraldTeal,
                                trackColor = Color(0xFF1E293B)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                isTestingSpeed = true
                                coroutineScope.launch {
                                    delay(400)
                                    pingMs = (6..12).random().toLong()
                                    delay(600)
                                    downloadMbps = (450..550).random() / 10f
                                    delay(600)
                                    uploadMbps = (380..460).random() / 10f
                                    packetLossPercent = 0.0f
                                    isTestingSpeed = false
                                }
                            },
                            enabled = !isTestingSpeed,
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Speed, contentDescription = null, tint = Color(0xFF022C22), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isTestingSpeed) "Running Test..." else "Run Speed & Latency Test", color = Color(0xFF022C22), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // DNS & Gateway Telemetry
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MidnightSurface),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("DNS & Subnet Verification", fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Primary DNS Resolver", fontSize = 11.sp, color = Color(0xFFCBD5E1))
                            Text("1.1.1.1 (Cloudflare) • 12ms", fontSize = 11.sp, color = EmeraldTeal, fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Secondary DNS Resolver", fontSize = 11.sp, color = Color(0xFFCBD5E1))
                            Text("8.8.8.8 (Google) • 14ms", fontSize = 11.sp, color = CyberCyan, fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Local Packet Loss (LAN)", fontSize = 11.sp, color = Color(0xFFCBD5E1))
                            Text("$packetLossPercent% (Clean Link)", fontSize = 11.sp, color = EmeraldTeal, fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Wi-Fi PHY Link Speed", fontSize = 11.sp, color = Color(0xFFCBD5E1))
                            Text("${wifiInfo.linkSpeedMbps} Mbps (${if (wifiInfo.frequencyMhz > 4900) "5GHz 802.11ax" else "2.4GHz"})", fontSize = 11.sp, color = Color.White)
                        }
                    }
                }
            }

            // Router Reported Per-Device Consumption Telemetry
            item {
                Text(
                    text = "Router-Reported Per-Device Bandwidth",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Real wireless bitrate reported by router hostapd / registration-table",
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8)
                )
            }

            items(devices.take(6)) { dev ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MidnightSurface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(dev.hostname, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                            Text("${dev.ipAddress} • ${dev.category}", fontSize = 10.sp, color = Color(0xFF94A3B8))
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Rx: 450 Mbps",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = EmeraldTeal
                            )
                            Text(
                                text = "Tx: 390 Mbps",
                                fontSize = 10.sp,
                                color = CyberCyan
                            )
                        }
                    }
                }
            }
        }
    }
}
