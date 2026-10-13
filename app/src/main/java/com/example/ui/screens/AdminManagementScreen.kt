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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adapter.RouterAdapterRegistry
import com.example.data.local.entity.CompatibilityModelEntity
import com.example.data.local.entity.CompatibilityRequestEntity
import com.example.data.local.entity.UserEntity
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
fun AdminManagementScreen(
    user: UserEntity?,
    models: List<CompatibilityModelEntity>,
    requests: List<CompatibilityRequestEntity>,
    onAddModel: (CompatibilityModelEntity) -> Unit,
    onBack: () -> Unit
) {
    var adminTab by remember { mutableStateOf("ADAPTERS") }

    var newMfg by remember { mutableStateOf("") }
    var newModel by remember { mutableStateOf("") }
    var newCategory by remember { mutableStateOf("ROUTER") }
    var newIsp by remember { mutableStateOf("Link3 / Carnival") }
    var newMethod by remember { mutableStateOf("REST API") }
    var newDocUrl by remember { mutableStateOf("https://docs.openwrt.org") }

    val adapters = RouterAdapterRegistry.getAllAvailableAdapters()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyDark)
            .padding(20.dp)
            .testTag("admin_management_screen")
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
                    text = "Administration & Adapter Registry",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Role: ${user?.role ?: "OWNER"} • Super Admin Console",
                    fontSize = 11.sp,
                    color = EmeraldTeal
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Tab Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("ADAPTERS" to "Active Adapters", "REQUESTS" to "User Requests", "ADD_MODEL" to "Catalog Editor").forEach { (tab, label) ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (adminTab == tab) Color(0xFF064E3B) else MidnightSurface,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { adminTab = tab }
                ) {
                    Text(
                        text = label,
                        color = if (adminTab == tab) EmeraldTeal else Color(0xFF94A3B8),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        when (adminTab) {
            "ADAPTERS" -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    item {
                        Text(
                            text = "Registered Router Integration Adapters",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Each adapter implements the provider-neutral RouterAdapter contract.",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }

                    items(adapters) { adapter ->
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
                                    Text(adapter.adapterName, fontWeight = FontWeight.Bold, color = Color.White)
                                    StatusBadge(status = if (adapter.isTestDouble) "TEST_DOUBLE" else "FULLY_VERIFIED")
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("ID: ${adapter.adapterId}", fontSize = 11.sp, color = CyberCyan)
                                Text("Type: ${if (adapter.isTestDouble) "In-Memory Simulation Harness" else "Live HTTP / REST / JSON-RPC"}", fontSize = 11.sp, color = Color(0xFF94A3B8))
                            }
                        }
                    }
                }
            }

            "REQUESTS" -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    item {
                        Text(
                            text = "Community Compatibility Requests",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "${requests.size} requests queued for engineering review.",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }

                    if (requests.isEmpty()) {
                        item {
                            Text("No pending requests.", color = Color(0xFF64748B), fontSize = 12.sp)
                        }
                    } else {
                        items(requests) { req ->
                            val dateStr = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(req.createdAt))
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
                                        Text("${req.manufacturer} ${req.model}", fontWeight = FontWeight.Bold, color = Color.White)
                                        StatusBadge(status = req.status)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("ISP: ${req.ispName} • User: ${req.userEmail}", fontSize = 11.sp, color = CyberCyan)
                                    if (req.notes.isNotBlank()) {
                                        Text("Notes: ${req.notes}", fontSize = 11.sp, color = Color(0xFFCBD5E1))
                                    }
                                    Text("Created: $dateStr", fontSize = 10.sp, color = Color(0xFF64748B))
                                }
                            }
                        }
                    }
                }
            }

            "ADD_MODEL" -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    item {
                        Text(
                            text = "Add Verified Router to Catalog",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = newMfg,
                            onValueChange = { newMfg = it },
                            label = { Text("Manufacturer") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newModel,
                            onValueChange = { newModel = it },
                            label = { Text("Model Identifier") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newIsp,
                            onValueChange = { newIsp = it },
                            label = { Text("Bangladesh ISPs") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newMethod,
                            onValueChange = { newMethod = it },
                            label = { Text("Integration Method") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newDocUrl,
                            onValueChange = { newDocUrl = it },
                            label = { Text("Official Documentation URL") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                if (newMfg.isNotBlank() && newModel.isNotBlank()) {
                                    val newEntity = CompatibilityModelEntity(
                                        id = "comp_" + System.currentTimeMillis(),
                                        manufacturer = newMfg,
                                        modelName = newModel,
                                        deviceCategory = newCategory,
                                        hardwareRevisions = "All Revisions",
                                        firmwareRequirements = "Latest OEM Firmware",
                                        integrationMethod = newMethod,
                                        bangladeshIsps = newIsp,
                                        supportLevel = "PARTIALLY_VERIFIED",
                                        supportsPreAccess = false,
                                        supportsMacFilter = true,
                                        supportsGuestNetwork = true,
                                        officialDocUrl = newDocUrl,
                                        lastVerifiedDate = "2026-03-20",
                                        knownLimitations = "Added via Admin Console"
                                    )
                                    onAddModel(newEntity)
                                    newMfg = ""
                                    newModel = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal)
                        ) {
                            Text("Publish Model to Compatibility Database", color = Color(0xFF022C22), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
