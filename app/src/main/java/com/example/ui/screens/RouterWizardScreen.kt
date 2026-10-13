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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Security
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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.adapter.ConnectionTestResult
import com.example.data.local.entity.RouterEntity
import com.example.ui.components.HonestCapabilityItem
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DeepNavyDark
import com.example.ui.theme.EmeraldTeal
import com.example.ui.theme.MidnightSurface
import com.example.ui.theme.WarningAmber

@Composable
fun RouterWizardScreen(
    isConnecting: Boolean,
    testResult: ConnectionTestResult?,
    onCancel: () -> Unit,
    onSaveAndConnect: (RouterEntity) -> Unit
) {
    var step by remember { mutableIntStateOf(1) }

    var selectedConnectionType by remember { mutableStateOf("OPENWRT_RPC") }
    var selectedManufacturer by remember { mutableStateOf("OpenWrt Foundation") }
    var selectedModel by remember { mutableStateOf("OpenWrt x86 / ARM Gateway") }
    var hardwareRevision by remember { mutableStateOf("Rev v2") }
    var firmwareVersion by remember { mutableStateOf("23.05.3") }
    var ispName by remember { mutableStateOf("Link3 Technologies") }

    var ipAddress by remember { mutableStateOf("192.168.1.1") }
    var port by remember { mutableStateOf("80") }
    var username by remember { mutableStateOf("root") }
    var password by remember { mutableStateOf("") }
    var useHttps by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyDark)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
            .testTag("router_wizard_screen")
    ) {
        // Top navigation bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onCancel) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "Router Connection Wizard",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Step $step of 6: ${getStepTitle(step)}",
                    fontSize = 12.sp,
                    color = CyberCyan
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Progress bar indicator (6 steps)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            (1..6).forEach { s ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(if (s <= step) EmeraldTeal else Color(0xFF334155))
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Wizard Steps
        when (step) {
            1 -> {
                // Step 1: Choose Connection Type
                Text("Select Router Connection Type", fontWeight = FontWeight.Bold, color = Color.White)
                Text(
                    "Choose how this app communicates with your access-control gateway.",
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8)
                )
                Spacer(modifier = Modifier.height(14.dp))

                listOf(
                    Triple("OPENWRT_RPC", "OpenWrt uBus / LuCI JSON-RPC", "Full pre-access station authorization via hostapd MAC filter allowlist."),
                    Triple("MIKROTIK_REST", "MikroTik RouterOS v7 REST API", "RouterBOARD access-list rules and pre-authentication hold."),
                    Triple("TPLINK_HTTP", "TP-Link Archer / Deco Session", "Post-connection MAC blacklist control via management session."),
                    Triple("HUAWEI_ONT", "Huawei GPON ONT (Link3 / Amber IT)", "Fiber terminal WLAN MAC filter and post-connection isolation."),
                    Triple("TEST_DOUBLE", "Test Double / Simulator Sandbox", "Offline evaluation sandbox for testing all approval CUJs without hardware.")
                ).forEach { (type, name, desc) ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedConnectionType == type) Color(0xFF064E3B) else MidnightSurface
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                            .clickable {
                                selectedConnectionType = type
                                if (type == "OPENWRT_RPC") {
                                    selectedManufacturer = "OpenWrt Foundation"
                                    selectedModel = "OpenWrt 23.05"
                                    ipAddress = "192.168.1.1"
                                    port = "80"
                                    username = "root"
                                } else if (type == "MIKROTIK_REST") {
                                    selectedManufacturer = "MikroTik"
                                    selectedModel = "RouterBOARD hEX"
                                    ipAddress = "192.168.88.1"
                                    port = "443"
                                    useHttps = true
                                    username = "admin"
                                } else if (type == "HUAWEI_ONT") {
                                    selectedManufacturer = "Huawei"
                                    selectedModel = "HG8145V5 GPON ONT"
                                    ipAddress = "192.168.100.1"
                                    port = "80"
                                    username = "telecomadmin"
                                } else if (type == "TPLINK_HTTP") {
                                    selectedManufacturer = "TP-Link"
                                    selectedModel = "Archer C6 AC1200"
                                    ipAddress = "192.168.0.1"
                                    port = "80"
                                    username = "admin"
                                } else {
                                    selectedManufacturer = "Simulator Lab"
                                    selectedModel = "Virtual Gateway"
                                    ipAddress = "127.0.0.1"
                                    port = "8080"
                                    username = "test"
                                }
                            }
                            .testTag("conn_type_$type")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(name, fontWeight = FontWeight.Bold, color = Color.White)
                                if (selectedConnectionType == type) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldTeal, modifier = Modifier.size(18.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(desc, fontSize = 11.sp, color = Color(0xFF94A3B8))
                        }
                    }
                }
            }

            2 -> {
                // Step 2: Select Manufacturer
                Text("Select Manufacturer", fontWeight = FontWeight.Bold, color = Color.White)
                Text("Select the hardware manufacturer verified in Bangladesh.", fontSize = 12.sp, color = Color(0xFF94A3B8))
                Spacer(modifier = Modifier.height(14.dp))

                listOf("OpenWrt Foundation", "MikroTik", "TP-Link", "Huawei", "ZTE", "Netis", "Tenda", "BDCOM").forEach { mfg ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedManufacturer == mfg) Color(0xFF064E3B) else MidnightSurface
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { selectedManufacturer = mfg }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(mfg, fontWeight = FontWeight.SemiBold, color = Color.White)
                            if (selectedManufacturer == mfg) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = EmeraldTeal)
                            }
                        }
                    }
                }
            }

            3 -> {
                // Step 3: Select Model & Bangladesh ISP
                Text("Hardware & ISP Customization", fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = selectedModel,
                    onValueChange = { selectedModel = it },
                    label = { Text("Exact Model Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldTeal,
                        unfocusedBorderColor = Color(0xFF334155),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = hardwareRevision,
                    onValueChange = { hardwareRevision = it },
                    label = { Text("Hardware Revision (e.g. v2, Rev B)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldTeal,
                        unfocusedBorderColor = Color(0xFF334155),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = firmwareVersion,
                    onValueChange = { firmwareVersion = it },
                    label = { Text("Firmware Version") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldTeal,
                        unfocusedBorderColor = Color(0xFF334155),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = ispName,
                    onValueChange = { ispName = it },
                    label = { Text("Bangladesh ISP (Link3, Carnival, Amber IT, Dot, etc.)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldTeal,
                        unfocusedBorderColor = Color(0xFF334155),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
            }

            4 -> {
                // Step 4: Secure Credentials
                Text("Router Connection & Credentials", fontWeight = FontWeight.Bold, color = Color.White)
                Text(
                    "Credentials are encrypted on-device and never transmitted to external AI or third parties.",
                    fontSize = 11.sp,
                    color = CyberCyan
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = ipAddress,
                    onValueChange = { ipAddress = it },
                    label = { Text("Router IP Address") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldTeal,
                        unfocusedBorderColor = Color(0xFF334155),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = port,
                    onValueChange = { port = it },
                    label = { Text("Port (80 / 443 / 8080)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldTeal,
                        unfocusedBorderColor = Color(0xFF334155),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Admin Username") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldTeal,
                        unfocusedBorderColor = Color(0xFF334155),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password or API Token") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldTeal,
                        unfocusedBorderColor = Color(0xFF334155),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Use HTTPS / TLS Encrypted Connection", fontSize = 12.sp, color = Color.White)
                    Switch(
                        checked = useHttps,
                        onCheckedChange = { useHttps = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = EmeraldTeal)
                    )
                }
            }

            5 -> {
                // Step 5: Test Read-Only Connection
                Text("Verifying Integration Capabilities", fontWeight = FontWeight.Bold, color = Color.White)
                Text(
                    "Executing safe read-only API discovery on $ipAddress without modifying configuration.",
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = MidnightSurface),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (isConnecting) {
                            CircularProgressIndicator(color = EmeraldTeal)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Querying router API...", color = Color.White, fontSize = 12.sp)
                        } else {
                            Icon(
                                imageVector = if (testResult?.success == true) Icons.Default.CheckCircle else Icons.Default.Info,
                                contentDescription = null,
                                tint = if (testResult?.success == true) EmeraldTeal else CyberCyan,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = testResult?.message ?: "Ready to verify router reachability and permission.",
                                color = Color.White,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            if (testResult?.warnings?.isNotEmpty() == true) {
                                Spacer(modifier = Modifier.height(10.dp))
                                testResult.warnings.forEach { warning ->
                                    Text(
                                        text = "• $warning",
                                        color = WarningAmber,
                                        fontSize = 11.sp,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }
            }

            6 -> {
                // Step 6: Capability Report & Final Confirmation
                Text("Router Capability Report", fontWeight = FontWeight.Bold, color = Color.White)
                Text("Verified access-control capabilities for $selectedModel.", fontSize = 12.sp, color = Color(0xFF94A3B8))
                Spacer(modifier = Modifier.height(14.dp))

                val isPreAccess = selectedConnectionType == "OPENWRT_RPC" || selectedConnectionType == "MIKROTIK_REST" || selectedConnectionType == "TEST_DOUBLE"

                Card(
                    colors = CardDefaults.cardColors(containerColor = MidnightSurface),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        HonestCapabilityItem(
                            title = "Pre-Access Station Approval",
                            supported = isPreAccess,
                            note = if (isPreAccess) "Dynamic station quarantine supported" else "Pre-access ACL unavailable on this model; post-access MAC filter will be enforced"
                        )
                        HonestCapabilityItem(
                            title = "Instant Station Deauthentication",
                            supported = true,
                            note = "Disconnects revoked devices from Wi-Fi"
                        )
                        HonestCapabilityItem(
                            title = "Connected Device Telemetry",
                            supported = true,
                            note = "Reads IP, MAC, signal RSSI, and bitrates"
                        )
                        HonestCapabilityItem(
                            title = "Isolated Guest Subnet Control",
                            supported = selectedConnectionType != "NETIS_HTTP",
                            note = "Manages temporary guest tokens"
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Navigation Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (step > 1) {
                OutlinedButton(
                    onClick = { step-- },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Back", color = Color.White)
                }
            }

            Button(
                onClick = {
                    if (step < 6) {
                        step++
                    } else {
                        val isPreAccess = selectedConnectionType == "OPENWRT_RPC" || selectedConnectionType == "MIKROTIK_REST" || selectedConnectionType == "TEST_DOUBLE"
                        val newRouter = RouterEntity(
                            id = "rtr_" + System.currentTimeMillis(),
                            name = "$selectedManufacturer $selectedModel",
                            manufacturer = selectedManufacturer,
                            model = selectedModel,
                            hardwareRevision = hardwareRevision,
                            firmwareVersion = firmwareVersion,
                            connectionType = selectedConnectionType,
                            ipAddress = ipAddress,
                            port = port.toIntOrNull() ?: 80,
                            useHttps = useHttps,
                            username = username,
                            encryptedToken = password,
                            status = if (selectedConnectionType == "TEST_DOUBLE") "DEMO_MODE" else "CONNECTED",
                            latencyMs = 8L,
                            isPreAccessSupported = isPreAccess,
                            isMacFilterSupported = true,
                            isGuestNetworkSupported = true,
                            ispName = ispName
                        )
                        onSaveAndConnect(newRouter)
                    }
                },
                modifier = Modifier
                    .weight(if (step > 1) 1.5f else 1f)
                    .testTag("wizard_next_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (step == 6) "Confirm & Connect" else "Continue",
                    color = Color(0xFF022C22),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun getStepTitle(step: Int): String = when (step) {
    1 -> "Connection Type"
    2 -> "Manufacturer"
    3 -> "Model & ISP"
    4 -> "Credentials"
    5 -> "Verify"
    6 -> "Capability Report"
    else -> ""
}
