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
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.DeviceEntity
import com.example.ui.components.InfoCard
import com.example.ui.components.OutlinedButtonCyber
import com.example.ui.components.ScreenHeader
import com.example.ui.components.StatusChip
import com.example.ui.theme.AlertRed
import com.example.ui.theme.BorderCyan
import com.example.ui.theme.PrimaryBg
import com.example.ui.theme.PrimaryCyan
import com.example.ui.theme.PrimaryText
import com.example.ui.theme.SecondaryText
import com.example.ui.theme.SuccessGreen

@Composable
fun BlockDeviceScreen(
    device: DeviceEntity?,
    onConfirmBlock: (deviceId: String) -> Unit,
    onConfirmUnblock: (deviceId: String) -> Unit,
    onCancel: () -> Unit
) {
    if (device == null) {
        onCancel()
        return
    }

    val isCurrentlyBlocked = device.authorizationState == "BLOCKED" || device.authorizationState == "REVOKED"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBg)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
            .testTag("block_device_screen"),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            ScreenHeader(
                title = if (isCurrentlyBlocked) "Unblock Device" else "Block Device Access",
                subtitle = "Screen 09: Router Access Control Confirmation",
                onBack = onCancel
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Warning Icon
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    color = if (isCurrentlyBlocked) Color(0xFF064E3B) else Color(0xFF7F1D1D),
                    shape = CircleShape,
                    border = androidx.compose.foundation.BorderStroke(
                        2.dp,
                        if (isCurrentlyBlocked) SuccessGreen else AlertRed
                    ),
                    modifier = Modifier.size(96.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (isCurrentlyBlocked) Icons.Default.LockOpen else Icons.Default.Block,
                            contentDescription = "Access Control",
                            tint = if (isCurrentlyBlocked) SuccessGreen else AlertRed,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = if (isCurrentlyBlocked) "Restore Wi-Fi Access to Device?" else "Block Device from Wi-Fi Network?",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = PrimaryText,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isCurrentlyBlocked)
                    "This device will be removed from the router's blacklist and permitted to reconnect."
                else
                    "This device will be immediately disconnected from the router and barred from obtaining an IP address.",
                style = MaterialTheme.typography.bodySmall,
                color = SecondaryText,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Device Identifiers Card
            InfoCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "TARGET DEVICE DETAILS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryCyan
                )

                Spacer(modifier = Modifier.height(8.dp))

                DetailItem(label = "Device Name", value = device.hostname.ifBlank { "Unknown Device" })
                DetailItem(label = "IP Address", value = device.ipAddress)
                DetailItem(label = "MAC Address", value = device.macAddress)
                DetailItem(label = "Device Type", value = device.category)
                DetailItem(label = "Trust Status", value = if (device.isTrusted) "TRUSTED" else "UNVERIFIED")
                DetailItem(label = "Current State", value = if (isCurrentlyBlocked) "BLOCKED" else "ACTIVE")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Notice about router enforcement
            InfoCard(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = PrimaryCyan,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "The block operation applies hardware MAC blacklist filtering on your authorized router. This will be recorded in the local Activity Log.",
                        color = SecondaryText,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Bottom Actions
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            if (isCurrentlyBlocked) {
                Button(
                    onClick = { onConfirmUnblock(device.id) },
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("unblock_device_confirm_button")
                ) {
                    Icon(Icons.Default.LockOpen, contentDescription = null, tint = Color(0xFF022C22))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Unblock & Allow Access",
                        color = Color(0xFF022C22),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            } else {
                Button(
                    onClick = { onConfirmBlock(device.id) },
                    colors = ButtonDefaults.buttonColors(containerColor = AlertRed),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("block_device_confirm_button")
                ) {
                    Icon(Icons.Default.Block, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Block Device",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButtonCyber(
                text = "Cancel",
                onClick = onCancel,
                testTag = "cancel_block_button"
            )
        }
    }
}

@Composable
private fun DetailItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = SecondaryText, fontSize = 12.sp)
        Text(text = value, color = PrimaryText, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}
