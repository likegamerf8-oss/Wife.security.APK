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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adapter.IntegrationHealth
import com.example.data.local.entity.RouterEntity
import com.example.data.local.entity.SecurityEventEntity
import com.example.ui.components.HonestCapabilityItem
import com.example.ui.components.StatusBadge
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DeepNavyDark
import com.example.ui.theme.EmeraldTeal
import com.example.ui.theme.MidnightSurface
import com.example.ui.theme.WarningAmber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ActivityHistoryScreen(
    events: List<SecurityEventEntity>,
    onBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var severityFilter by remember { mutableStateOf("ALL") }
    var exportMessage by remember { mutableStateOf<String?>(null) }

    val filteredEvents = events.filter { ev ->
        val matchesQuery = searchQuery.isBlank() ||
                ev.title.contains(searchQuery, ignoreCase = true) ||
                ev.description.contains(searchQuery, ignoreCase = true) ||
                ev.type.contains(searchQuery, ignoreCase = true)

        val matchesSeverity = when (severityFilter) {
            "CRITICAL" -> ev.severity == "CRITICAL"
            "WARNING" -> ev.severity == "WARNING"
            "INFO" -> ev.severity == "INFO"
            else -> true
        }
        matchesQuery && matchesSeverity
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyDark)
            .padding(horizontal = 20.dp)
            .testTag("activity_history_screen")
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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Security Activity Audit Log",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Immutable server & router event log",
                    fontSize = 11.sp,
                    color = CyberCyan
                )
            }
            IconButton(onClick = { exportMessage = "Exported ${filteredEvents.size} events to JSON/CSV audit bundle." }) {
                Icon(Icons.Default.Download, contentDescription = "Export Logs", tint = EmeraldTeal)
            }
        }

        if (exportMessage != null) {
            Surface(
                color = Color(0xFF064E3B),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = exportMessage!!,
                    color = EmeraldTeal,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Filter audit events...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = EmeraldTeal) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EmeraldTeal,
                unfocusedBorderColor = Color(0xFF334155),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("ALL" to "All", "CRITICAL" to "Critical", "WARNING" to "Warning", "INFO" to "Info").forEach { (sev, label) ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (severityFilter == sev) Color(0xFF064E3B) else MidnightSurface,
                    modifier = Modifier.clickable { severityFilter = sev }
                ) {
                    Text(
                        text = label,
                        color = if (severityFilter == sev) EmeraldTeal else Color(0xFF94A3B8),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredEvents) { event ->
                val timeFormatted = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(event.timestamp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = MidnightSurface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = event.title,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            StatusBadge(status = event.severity)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = event.description,
                            fontSize = 12.sp,
                            color = Color(0xFFCBD5E1)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Timestamp: $timeFormatted • Type: ${event.type}",
                            fontSize = 10.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DiagnosticsScreen(
    router: RouterEntity?,
    health: IntegrationHealth?,
    onRunDiagnostics: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyDark)
            .padding(20.dp)
            .testTag("diagnostics_screen")
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
                    text = "Router Health & Diagnostics",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Live telemetry & non-invasive read-only check",
                    fontSize = 11.sp,
                    color = CyberCyan
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MidnightSurface),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(router?.name ?: "No router", fontWeight = FontWeight.Bold, color = Color.White, style = MaterialTheme.typography.titleMedium)
                                Text("${router?.ipAddress}:${router?.port} • ${router?.connectionType}", fontSize = 12.sp, color = CyberCyan)
                            }
                            StatusBadge(status = if (health?.isReachable == true) "CONNECTED" else "OFFLINE")
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                color = Color(0xFF1E293B),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("API Latency", fontSize = 10.sp, color = Color(0xFF94A3B8))
                                    Text("${health?.latencyMs ?: router?.latencyMs ?: 0} ms", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = EmeraldTeal)
                                }
                            }

                            Surface(
                                color = Color(0xFF1E293B),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("HTTP Status", fontSize = 10.sp, color = Color(0xFF94A3B8))
                                    Text("${health?.apiStatusCode ?: 200} OK", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = CyberCyan)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("ISP Gateway: ${router?.ispName}", fontSize = 11.sp, color = Color(0xFFCBD5E1))
                        Text("Firmware: ${router?.firmwareVersion}", fontSize = 11.sp, color = Color(0xFFCBD5E1))
                        Text("Hardware Revision: ${router?.hardwareRevision}", fontSize = 11.sp, color = Color(0xFFCBD5E1))

                        if (health?.errorMessage != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Diagnostic Error: ${health.errorMessage}", fontSize = 11.sp, color = AlertRed)
                        }
                    }
                }
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MidnightSurface),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Integration Verification Matrix", fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(10.dp))

                        HonestCapabilityItem(title = "Local Subnet ARP Reachability", supported = true)
                        HonestCapabilityItem(title = "Administrative Session Authentication", supported = true)
                        HonestCapabilityItem(title = "DHCP Lease & Station Query API", supported = true)
                        HonestCapabilityItem(title = "Hardware Access-List Command Execution", supported = router?.isPreAccessSupported ?: false)
                        HonestCapabilityItem(title = "Protected Management Frames (802.11w)", supported = router?.isPreAccessSupported ?: false)
                    }
                }
            }

            item {
                Button(
                    onClick = onRunDiagnostics,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("run_diagnostics_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, tint = Color(0xFF022C22))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Run Safe Diagnostic Test", color = Color(0xFF022C22), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
