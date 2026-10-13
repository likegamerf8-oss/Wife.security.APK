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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.GuestPassEntity
import com.example.data.local.entity.RouterEntity
import com.example.data.local.entity.SecurityPolicyEntity
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
fun SecurityPolicyScreen(
    policy: SecurityPolicyEntity?,
    router: RouterEntity?,
    onUpdatePolicy: (SecurityPolicyEntity) -> Unit,
    onBack: () -> Unit
) {
    var selectedMode by remember { mutableStateOf(policy?.mode ?: "DEFAULT_DENY_PRE_ACCESS") }
    var autoTrust by remember { mutableStateOf(policy?.autoTrustApproved ?: true) }
    var alertUnknown by remember { mutableStateOf(policy?.alertOnUnknownDevices ?: true) }
    var notifications by remember { mutableStateOf(policy?.notificationEnabled ?: true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyDark)
            .padding(20.dp)
            .testTag("security_policy_screen")
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
                    text = "Security Policy Enclave",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Network access-control enforcement rules",
                    fontSize = 11.sp,
                    color = CyberCyan
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item {
                Text(
                    text = "Select Network Security Mode",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))

                listOf(
                    Triple(
                        "DEFAULT_DENY_PRE_ACCESS",
                        "Default-Deny Pre-Access (Zero Trust)",
                        "All new devices held outside protected network routing until owner verifies. Requires supported router (OpenWrt / MikroTik / Omada)."
                    ),
                    Triple(
                        "DETECT_AND_NOTIFY",
                        "Detect & Immediate Notify (Standard)",
                        "Devices associate normally. Owner is alerted immediately with option to ban station if unauthorized."
                    ),
                    Triple(
                        "QUARANTINE_POST_ACCESS",
                        "Post-Connection Quarantine (ONT / Retail)",
                        "Ideal for Huawei/ZTE GPON ONTs and retail routers that cannot hold stations pre-auth. Quarantines unknown traffic to isolated VLAN."
                    )
                ).forEach { (mode, name, desc) ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedMode == mode) Color(0xFF064E3B) else MidnightSurface
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { selectedMode = mode }
                            .testTag("policy_mode_$mode")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(name, fontWeight = FontWeight.Bold, color = Color.White)
                                if (selectedMode == mode) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldTeal)
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(desc, fontSize = 11.sp, color = Color(0xFF94A3B8))

                            if (mode == "DEFAULT_DENY_PRE_ACCESS" && router?.isPreAccessSupported == false) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Warning, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Current connected router (${router.model}) lacks pre-access station quarantine. Mode will fall back to Post-Connect MAC blocking.",
                                        fontSize = 10.sp,
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
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Automated Policies & Alerts", fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Auto-Trust Approved Devices", fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                                Text("Do not re-prompt for known authorized devices", fontSize = 11.sp, color = Color(0xFF94A3B8))
                            }
                            Switch(
                                checked = autoTrust,
                                onCheckedChange = { autoTrust = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = EmeraldTeal)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("High-Priority Alert on Unknown Devices", fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                                Text("Trigger instant push and heads-up notification", fontSize = 11.sp, color = Color(0xFF94A3B8))
                            }
                            Switch(
                                checked = alertUnknown,
                                onCheckedChange = { alertUnknown = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = EmeraldTeal)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("System Push Notifications", fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                                Text("Deliver via Android system notification tray", fontSize = 11.sp, color = Color(0xFF94A3B8))
                            }
                            Switch(
                                checked = notifications,
                                onCheckedChange = { notifications = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = EmeraldTeal)
                            )
                        }
                    }
                }
            }

            item {
                Button(
                    onClick = {
                        val updated = (policy ?: SecurityPolicyEntity(id = "policy_01", networkId = "net_01", mode = selectedMode)).copy(
                            mode = selectedMode,
                            autoTrustApproved = autoTrust,
                            alertOnUnknownDevices = alertUnknown,
                            notificationEnabled = notifications
                        )
                        onUpdatePolicy(updated)
                        onBack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("save_policy_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Apply Security Policy", color = Color(0xFF022C22), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun GuestAccessScreen(
    guestPasses: List<GuestPassEntity>,
    onCreatePass: (guestName: String, hours: Int) -> Unit,
    onRevokePass: (passId: String) -> Unit,
    onBack: () -> Unit
) {
    var guestName by remember { mutableStateOf("") }
    var selectedHours by remember { mutableIntStateOf(4) }
    var showCreateForm by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyDark)
            .padding(20.dp)
            .testTag("guest_access_screen")
    ) {
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
                    text = "Guest Access Management",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Temporary, time-limited Wi-Fi tokens",
                    fontSize = 11.sp,
                    color = CyberCyan
                )
            }
            Button(
                onClick = { showCreateForm = !showCreateForm },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF022C22), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("New Pass", color = Color(0xFF022C22), fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (showCreateForm) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MidnightSurface),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Issue Temporary Guest Pass", fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = guestName,
                        onValueChange = { guestName = it },
                        label = { Text("Guest Name / Visitor Identifier") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Access Duration:", fontSize = 11.sp, color = Color(0xFF94A3B8))
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(1 to "1 Hour", 4 to "4 Hours", 24 to "24 Hours", 168 to "7 Days").forEach { (h, label) ->
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (selectedHours == h) Color(0xFF064E3B) else Color(0xFF1E293B),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedHours = h }
                            ) {
                                Text(
                                    text = label,
                                    color = if (selectedHours == h) EmeraldTeal else Color(0xFF94A3B8),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            if (guestName.isNotBlank()) {
                                onCreatePass(guestName, selectedHours)
                                guestName = ""
                                showCreateForm = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal)
                    ) {
                        Text("Generate Pass & QR Token", color = Color(0xFF022C22), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(guestPasses) { pass ->
                val expiresStr = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date(pass.expiresAt))
                val isExpired = System.currentTimeMillis() > pass.expiresAt || !pass.isActive

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
                            Column {
                                Text(pass.guestName, fontWeight = FontWeight.Bold, color = Color.White, style = MaterialTheme.typography.bodyLarge)
                                Text("Token: ${pass.passCode}", fontSize = 12.sp, color = CyberCyan, fontWeight = FontWeight.SemiBold)
                            }
                            StatusBadge(status = if (isExpired) "BLOCKED" else "AUTHORIZED")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isExpired) "Expired" else "Expires: $expiresStr",
                                fontSize = 11.sp,
                                color = if (isExpired) AlertRed else Color(0xFF94A3B8)
                            )

                            if (!isExpired) {
                                OutlinedButton(
                                    onClick = { onRevokePass(pass.id) },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AlertRed),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Revoke Pass", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
