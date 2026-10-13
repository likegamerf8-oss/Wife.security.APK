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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.CompatibilityModelEntity
import com.example.ui.components.StatusBadge
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DeepNavyDark
import com.example.ui.theme.EmeraldTeal
import com.example.ui.theme.MidnightSurface
import com.example.ui.theme.WarningAmber

@Composable
fun CompatibilityCatalogScreen(
    models: List<CompatibilityModelEntity>,
    onBack: () -> Unit,
    onNavigateToUnsupportedHelp: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("ALL") }

    val filteredList = models.filter { model ->
        val matchesQuery = searchQuery.isBlank() ||
                model.manufacturer.contains(searchQuery, ignoreCase = true) ||
                model.modelName.contains(searchQuery, ignoreCase = true) ||
                model.bangladeshIsps.contains(searchQuery, ignoreCase = true)

        val matchesFilter = when (selectedFilter) {
            "VERIFIED" -> model.supportLevel == "FULLY_VERIFIED"
            "PARTIAL" -> model.supportLevel == "PARTIALLY_VERIFIED"
            "RESEARCH" -> model.supportLevel == "RESEARCH_REQUIRED" || model.supportLevel == "DEV_REQUIRED"
            else -> true
        }
        matchesQuery && matchesFilter
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyDark)
            .padding(horizontal = 20.dp)
            .testTag("compatibility_catalog_screen")
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
                    text = "Router Compatibility Explorer",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Researched hardware & Bangladesh ISP Gateways",
                    fontSize = 11.sp,
                    color = CyberCyan
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search brand, model, or BD ISP (Link3, Amber IT...)") },
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

        // Filter pills
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("ALL" to "All", "VERIFIED" to "Verified", "PARTIAL" to "Partial", "RESEARCH" to "Research Needed").forEach { (key, label) ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (selectedFilter == key) Color(0xFF064E3B) else MidnightSurface,
                    modifier = Modifier
                        .clickable { selectedFilter = key }
                ) {
                    Text(
                        text = label,
                        color = if (selectedFilter == key) EmeraldTeal else Color(0xFF94A3B8),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredList) { model ->
                CompatibilityModelCard(model = model)
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .clickable { onNavigateToUnsupportedHelp() }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Don't see your router or ONT?", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                            Text("Learn why some routers lack pre-access ACL and submit a request.", fontSize = 11.sp, color = Color(0xFF94A3B8))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CompatibilityModelCard(model: CompatibilityModelEntity) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MidnightSurface),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = model.modelName,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "${model.manufacturer} • ${model.deviceCategory}",
                        fontSize = 12.sp,
                        color = CyberCyan
                    )
                }
                StatusBadge(status = model.supportLevel)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Bangladesh ISP Deployments: ${model.bangladeshIsps}",
                fontSize = 11.sp,
                color = Color(0xFFCBD5E1)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Integration Method: ${model.integrationMethod}",
                fontSize = 11.sp,
                color = Color(0xFF94A3B8)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (model.supportsPreAccess) Icons.Default.CheckCircle else Icons.Default.Warning,
                        contentDescription = null,
                        tint = if (model.supportsPreAccess) EmeraldTeal else WarningAmber,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (model.supportsPreAccess) "Pre-Access ACL" else "Post-Connect MAC Block",
                        fontSize = 11.sp,
                        color = if (model.supportsPreAccess) EmeraldTeal else WarningAmber
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldTeal, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "MAC Filtering",
                        fontSize = 11.sp,
                        color = EmeraldTeal
                    )
                }
            }

            if (model.knownLimitations.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Limitation: ${model.knownLimitations}",
                    fontSize = 10.sp,
                    color = Color(0xFFFDE68A)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Verified: ${model.lastVerifiedDate}",
                fontSize = 10.sp,
                color = Color(0xFF64748B)
            )
        }
    }
}

@Composable
fun UnsupportedRouterScreen(
    onBack: () -> Unit,
    onSubmitRequest: (mfg: String, model: String, isp: String, email: String, notes: String) -> Unit
) {
    var mfg by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var isp by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyDark)
            .padding(20.dp)
            .testTag("unsupported_router_screen")
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
                text = "Understanding Router Compatibility",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MidnightSurface),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Why Isn't Every Router Fully Supported?",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "A mobile app alone cannot invent router hardware features. Pre-access security requires the network equipment itself to hold connecting devices outside protected LAN routing.",
                            fontSize = 12.sp,
                            color = Color(0xFFCBD5E1)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "1. GPON ONTs (Huawei HG8145V5 / ZTE F670L): ISP-provided fiber modems lack 802.11w Protected Management Frames. They support post-connection MAC blacklisting, but not holding devices in limbo.\n\n" +
                                    "2. OpenWrt & MikroTik: Full pre-access support because their Linux kernel / RouterOS drivers support dynamic station allowlisting.\n\n" +
                                    "3. Safe Alternatives: If your router lacks pre-access ACL, Universal Wi-Fi Security uses Post-Connection Quarantine and instant disassociation.",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8),
                            lineHeight = 16.sp
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
                        Text(
                            text = "Submit a Compatibility Request",
                            fontWeight = FontWeight.Bold,
                            color = EmeraldTeal,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Tell our integration engineering team which router or ISP fiber box you are using.",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        if (submitted) {
                            Surface(
                                color = Color(0xFF064E3B),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Thank you! Request recorded in our compatibility queue.",
                                    color = EmeraldTeal,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        } else {
                            OutlinedTextField(
                                value = mfg,
                                onValueChange = { mfg = it },
                                label = { Text("Manufacturer (e.g. Netis, VSOL, BDCOM)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = model,
                                onValueChange = { model = it },
                                label = { Text("Model & Hardware Revision") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = isp,
                                onValueChange = { isp = it },
                                label = { Text("Bangladesh ISP (Link3, Carnival, Amber IT...)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("Your Email (optional notification)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = notes,
                                onValueChange = { notes = it },
                                label = { Text("Firmware version or notes") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = {
                                    onSubmitRequest(mfg, model, isp, email, notes)
                                    submitted = true
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal)
                            ) {
                                Text("Submit Compatibility Request", color = Color(0xFF022C22), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
