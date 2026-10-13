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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.DeviceEntity
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
fun DevicesScreen(
    devices: List<DeviceEntity>,
    isScanning: Boolean = false,
    onScanDevices: () -> Unit = {},
    onSelectDevice: (DeviceEntity) -> Unit,
    onBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var filterState by remember { mutableStateOf("ALL") }

    val filteredList = devices.filter { dev ->
        val matchesQuery = searchQuery.isBlank() ||
                dev.hostname.contains(searchQuery, ignoreCase = true) ||
                dev.macAddress.contains(searchQuery, ignoreCase = true) ||
                dev.ipAddress.contains(searchQuery, ignoreCase = true) ||
                dev.vendor.contains(searchQuery, ignoreCase = true)

        val matchesFilter = when (filterState) {
            "AUTHORIZED" -> dev.authorizationState == "AUTHORIZED"
            "PENDING" -> dev.authorizationState == "PENDING"
            "UNKNOWN" -> dev.authorizationState == "UNKNOWN"
            "BLOCKED" -> dev.authorizationState == "BLOCKED" || dev.authorizationState == "REVOKED"
            else -> true
        }
        matchesQuery && matchesFilter
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyDark)
            .padding(horizontal = 20.dp)
            .testTag("devices_screen")
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
                    text = "Connected & Tracked Devices",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "${devices.size} total stations on network",
                    fontSize = 11.sp,
                    color = CyberCyan
                )
            }

            IconButton(
                onClick = onScanDevices,
                enabled = !isScanning,
                modifier = Modifier.testTag("scan_network_devices_btn")
            ) {
                if (isScanning) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = EmeraldTeal,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Scan Subnet",
                        tint = EmeraldTeal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search hostname, IP, MAC address...") },
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

        // State filter pills
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("ALL" to "All", "AUTHORIZED" to "Authorized", "PENDING" to "Pending", "BLOCKED" to "Blocked").forEach { (state, label) ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (filterState == state) Color(0xFF064E3B) else MidnightSurface,
                    modifier = Modifier.clickable { filterState = state }
                ) {
                    Text(
                        text = label,
                        color = if (filterState == state) EmeraldTeal else Color(0xFF94A3B8),
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
            items(filteredList) { device ->
                DeviceListItemCard(
                    device = device,
                    onClick = { onSelectDevice(device) }
                )
            }
        }
    }
}

@Composable
fun DeviceListItemCard(
    device: DeviceEntity,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MidnightSurface),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("device_card_${device.id}")
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFF1E293B),
                modifier = Modifier.size(42.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = getDeviceIcon(device.category),
                        contentDescription = device.category,
                        tint = when (device.authorizationState) {
                            "AUTHORIZED" -> EmeraldTeal
                            "PENDING" -> WarningAmber
                            "BLOCKED", "REVOKED" -> AlertRed
                            else -> CyberCyan
                        },
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = device.hostname,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (device.isRandomizedMac) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = Color(0xFF334155),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "RAND MAC",
                                fontSize = 9.sp,
                                color = Color(0xFFFDE68A),
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Text(
                    text = "${device.ipAddress} • ${device.macAddress}",
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8)
                )

                Text(
                    text = "${device.vendor} • ${device.category}",
                    fontSize = 10.sp,
                    color = Color(0xFF64748B)
                )
            }

            StatusBadge(status = device.authorizationState)
        }
    }
}

@Composable
fun DeviceDetailScreen(
    device: DeviceEntity,
    onBack: () -> Unit,
    onUpdateDevice: (DeviceEntity) -> Unit,
    onRevokeAccess: (String) -> Unit
) {
    var hostname by remember { mutableStateOf(device.hostname) }
    var notes by remember { mutableStateOf(device.notes) }
    var category by remember { mutableStateOf(device.category) }
    var isTrusted by remember { mutableStateOf(device.isTrusted) }

    val firstSeen = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(device.firstObservedTimestamp))
    val lastSeen = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(device.lastObservedTimestamp))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyDark)
            .padding(20.dp)
            .testTag("device_detail_screen")
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
                text = "Device Deep Inspection",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
                                Text(device.hostname, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                                Text(device.macAddress, fontSize = 12.sp, color = CyberCyan)
                            }
                            StatusBadge(status = device.authorizationState)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("IP Address: ${device.ipAddress}", fontSize = 12.sp, color = Color.White)
                        Text("NIC Manufacturer: ${device.vendor}", fontSize = 12.sp, color = Color(0xFFCBD5E1))
                        Text("First Observed: $firstSeen", fontSize = 11.sp, color = Color(0xFF94A3B8))
                        Text("Last Observed: $lastSeen", fontSize = 11.sp, color = Color(0xFF94A3B8))

                        if (device.isRandomizedMac) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                color = Color(0xFF451A03),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Warning, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Randomized Private MAC: Android/iOS privacy rotation detected. Device hardware identity may change across re-associations.",
                                        fontSize = 11.sp,
                                        color = WarningAmber
                                    )
                                }
                            }
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
                        Text("Edit Device Metadata", fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = hostname,
                            onValueChange = { hostname = it },
                            label = { Text("Display Name / Hostname") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = notes,
                            onValueChange = { notes = it },
                            label = { Text("Security Notes (Owner memo)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Device Category:", fontSize = 11.sp, color = Color(0xFF94A3B8))
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf("PHONE", "LAPTOP", "IOT", "SMART_TV", "GUEST").forEach { cat ->
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (category == cat) Color(0xFF064E3B) else Color(0xFF1E293B),
                                    modifier = Modifier.clickable { category = cat }
                                ) {
                                    Text(
                                        text = cat,
                                        color = if (category == cat) EmeraldTeal else Color(0xFF94A3B8),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                onUpdateDevice(
                                    device.copy(
                                        hostname = hostname,
                                        notes = notes,
                                        category = category,
                                        isTrusted = isTrusted
                                    )
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal)
                        ) {
                            Text("Save Changes", color = Color(0xFF022C22), fontWeight = FontWeight.Bold)
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
                        Text("Access Control Actions", fontWeight = FontWeight.Bold, color = AlertRed)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Revoking authorization sends an instant disassociation frame and updates the hardware router MAC blacklist.",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { onRevokeAccess(device.id) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("revoke_device_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = AlertRed)
                        ) {
                            Text("Revoke Access & Block Device", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

private fun getDeviceIcon(category: String): ImageVector = when (category) {
    "PHONE" -> Icons.Default.PhoneAndroid
    "LAPTOP" -> Icons.Default.Computer
    "IOT" -> Icons.Default.Devices
    "SMART_TV" -> Icons.Default.Tv
    "GUEST" -> Icons.Default.Person
    else -> Icons.Default.Devices
}
