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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.data.local.entity.ParentalProfileEntity
import com.example.data.local.entity.RouterEntity
import com.example.data.local.entity.SecurityAuditFindingEntity
import com.example.ui.components.StatusBadge
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DeepNavyDark
import com.example.ui.theme.EmeraldTeal
import com.example.ui.theme.MidnightSurface
import com.example.ui.theme.WarningAmber

@Composable
fun SecurityAuditScreen(
    router: RouterEntity?,
    findings: List<SecurityAuditFindingEntity>,
    parentalProfiles: List<ParentalProfileEntity>,
    onRemediateFinding: (String) -> Unit,
    onNavigateToPolicy: () -> Unit,
    onNavigateToAiAssistant: () -> Unit,
    onNavigateToParentalControls: () -> Unit,
    onBack: () -> Unit
) {
    var selectedFindingForRemediation by remember { mutableStateOf<SecurityAuditFindingEntity?>(null) }

    if (selectedFindingForRemediation != null) {
        val finding = selectedFindingForRemediation!!
        AlertDialog(
            onDismissRequest = { selectedFindingForRemediation = null },
            containerColor = MidnightSurface,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = EmeraldTeal)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Confirm Security Remediation", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(finding.title, fontWeight = FontWeight.Bold, color = CyberCyan, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(finding.recommendedSolution, color = Color(0xFFCBD5E1), fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "This will dispatch an authorized configuration command to ${router?.name ?: "router"}. No downtime will occur.",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onRemediateFinding(finding.id)
                        selectedFindingForRemediation = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal)
                ) {
                    Text("Apply Fix", color = Color(0xFF022C22), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { selectedFindingForRemediation = null }) {
                    Text("Cancel", color = Color.White)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyDark)
            .padding(horizontal = 20.dp)
            .testTag("security_audit_screen")
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
                    text = "Router Security Health Check",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Vulnerability audit & firmware hardening",
                    fontSize = 11.sp,
                    color = CyberCyan
                )
            }
            IconButton(onClick = onNavigateToAiAssistant) {
                Icon(Icons.Default.Info, contentDescription = "AI Explainer", tint = CyberCyan)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MidnightSurface),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFF064E3B),
                                    modifier = Modifier.size(42.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.Shield, contentDescription = null, tint = EmeraldTeal, modifier = Modifier.size(22.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Security Audit Score: 88/100",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = "Target: ${router?.name ?: "Gateway"} (${router?.ipAddress ?: "192.168.1.1"})",
                                        fontSize = 11.sp,
                                        color = Color(0xFF94A3B8)
                                    )
                                }
                            }
                            StatusBadge(status = "FULLY_VERIFIED")
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFF1E293B),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onNavigateToPolicy() }
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Policy, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Policy Rules", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFF1E293B),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onNavigateToParentalControls() }
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.ChildCare, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Parental Controls", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Security Audit Findings (${findings.size})",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            items(findings) { finding ->
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
                            Text(
                                text = finding.title,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                            StatusBadge(status = finding.severity)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Evidence: ${finding.evidence}",
                            fontSize = 11.sp,
                            color = Color(0xFFCBD5E1)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Explanation: ${finding.explanation}",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Recommendation: ${finding.recommendedSolution}",
                            fontSize = 11.sp,
                            color = CyberCyan
                        )

                        if (!finding.isRemediated && finding.isAutoRemediationSupported) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = { selectedFindingForRemediation = finding },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Build, contentDescription = null, tint = Color(0xFF022C22), modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Auto-Remediate via Router API", color = Color(0xFF022C22), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        } else if (finding.isRemediated) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldTeal, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Verified & Remediated", color = EmeraldTeal, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }
    }
}
