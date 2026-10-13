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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AppLogo
import com.example.ui.components.InfoCard
import com.example.ui.components.PrimaryButton
import com.example.ui.theme.BorderCyan
import com.example.ui.theme.CardBg
import com.example.ui.theme.MutedText
import com.example.ui.theme.PrimaryBg
import com.example.ui.theme.PrimaryCyan
import com.example.ui.theme.PrimaryText
import com.example.ui.theme.SecondaryText
import com.example.ui.theme.SuccessGreen

@Composable
fun WelcomeScreen(
    onConnectWifi: () -> Unit,
    onOpenOptionalAccount: () -> Unit
) {
    var showHelpDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBg)
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
            .testTag("welcome_screen"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Bar with Help Info
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = { showHelpDialog = true },
                modifier = Modifier.testTag("welcome_help_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Help & Information",
                    tint = SecondaryText
                )
            }
        }

        // Center Content: Logo, Bengali Headline, Feature Highlights
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            AppLogo(size = 96.dp)

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Wi-Fi Security",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = PrimaryText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Secure Your Network. Protect Your Devices.",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = PrimaryCyan,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "আপনার নেটওয়ার্ক সুরক্ষিত রাখুন। আপনার ডিভাইসগুলো নিরাপদে রাখুন।",
                fontSize = 12.sp,
                color = SecondaryText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 4 Key Feature Highlights in Cards
            FeatureHighlightRow(
                icon = Icons.Default.Devices,
                title = "Connected Device Monitoring",
                subtitle = "Discover real devices connected to your Wi-Fi router"
            )
            Spacer(modifier = Modifier.height(10.dp))

            FeatureHighlightRow(
                icon = Icons.Default.NotificationsActive,
                title = "Security Alerts",
                subtitle = "Instant notifications on unknown or unverified access"
            )
            Spacer(modifier = Modifier.height(10.dp))

            FeatureHighlightRow(
                icon = Icons.Default.NetworkCheck,
                title = "Network Diagnostics",
                subtitle = "Real latency, link rate, and router reachability tests"
            )
            Spacer(modifier = Modifier.height(10.dp))

            FeatureHighlightRow(
                icon = Icons.Default.Shield,
                title = "Router Security Guidance",
                subtitle = "Audit WPS, encryption, and admin exposure safely"
            )
        }

        // Bottom Actions: Primary Button + No Account Required
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            PrimaryButton(
                text = "Connect My Wi-Fi",
                onClick = onConnectWifi,
                testTag = "connect_my_wifi_button"
            )

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                color = Color(0xFF072740),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderCyan)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = SuccessGreen,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "No account required • 100% Local Wi-Fi Security",
                        color = SecondaryText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            TextButton(
                onClick = onOpenOptionalAccount,
                modifier = Modifier.testTag("optional_account_button")
            ) {
                Text(
                    text = "Cloud Sync & ISP Fleet Account (Optional)",
                    color = PrimaryCyan,
                    fontSize = 11.sp
                )
            }
        }
    }

    if (showHelpDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            title = {
                Text("About Wi-Fi Security", color = PrimaryText, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    text = "This application protects your local Wi-Fi without requiring an account or internet login. All diagnostics run locally on your device. Router credentials are saved only in your phone's private storage and are never sent to external servers.",
                    color = SecondaryText,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                TextButton(onClick = { showHelpDialog = false }) {
                    Text("Understood", color = PrimaryCyan, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = CardBg
        )
    }
}

@Composable
private fun FeatureHighlightRow(
    icon: ImageVector,
    title: String,
    subtitle: String
) {
    InfoCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(
                color = Color(0xFF0F395A),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = PrimaryCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryText
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = SecondaryText,
                    fontSize = 11.sp
                )
            }
        }
    }
}
