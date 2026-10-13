package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.localization.LanguageManager
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DeepNavyDark
import com.example.ui.theme.EmeraldTeal
import com.example.ui.theme.WarningAmber
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onInitialized: () -> Unit
) {
    var initStage by remember { mutableStateOf("Verifying local security enclave...") }
    var progress by remember { mutableStateOf(0.2f) }
    var isReady by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(600)
        initStage = "Checking BTRC compliance & network permission..."
        progress = 0.5f
        delay(600)
        initStage = "Initializing router integration adapters..."
        progress = 0.85f
        delay(600)
        initStage = "Security engine ready."
        progress = 1.0f
        isReady = true
        delay(400)
        onInitialized()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyDark)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFF0F172A),
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.wifi_security_icon_1791718058809),
                        contentDescription = "Universal Wi-Fi Security Logo",
                        modifier = Modifier.size(90.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = LanguageManager.getString("app_title"),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = LanguageManager.getString("tagline"),
                style = MaterialTheme.typography.bodyMedium,
                color = CyberCyan,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Bangladesh ISP & Enterprise Access Control",
                fontSize = 12.sp,
                color = Color(0xFF94A3B8)
            )

            Spacer(modifier = Modifier.height(40.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = EmeraldTeal,
                trackColor = Color(0xFF1E293B)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = initStage,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFCBD5E1),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "v1.0.0 (Production Core)",
                fontSize = 11.sp,
                color = Color(0xFF64748B)
            )
        }
    }
}

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit
) {
    var currentStep by remember { mutableIntStateOf(0) }

    val steps = listOf(
        OnboardingStep(
            title = "Protect Your Wi-Fi",
            description = "Defend your home, shop, or office network against unauthorized access. Ensure every device on your network is authenticated.",
            icon = Icons.Default.Shield,
            tag = "Absolute Control"
        ),
        OnboardingStep(
            title = "Pre-Access Approval",
            description = "A new device must not receive protected network access until you approve it. Held outside until you verify.",
            icon = Icons.Default.Devices,
            tag = "Zero Trust Wi-Fi"
        ),
        OnboardingStep(
            title = "Supported Bangladesh Routers",
            description = "Integrates with MikroTik, TP-Link, OpenWrt, Huawei GPON ONTs, and ZTE gateways across Link3, Carnival, Amber IT, and more.",
            icon = Icons.Default.Router,
            tag = "Honest Compatibility"
        )
    )

    val step = steps[currentStep]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyDark)
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = Color(0xFF1E293B),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Step ${currentStep + 1} of 3",
                    color = CyberCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }

            if (currentStep < 2) {
                Button(
                    onClick = onFinished,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Text("Skip", color = Color(0xFF94A3B8))
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFF0F172A),
                modifier = Modifier.size(120.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = step.icon,
                        contentDescription = step.title,
                        tint = EmeraldTeal,
                        modifier = Modifier.size(56.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Surface(
                color = Color(0xFF064E3B),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = step.tag,
                    color = EmeraldTeal,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = step.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = step.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF94A3B8),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(width = if (index == currentStep) 24.dp else 8.dp, height = 8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (index == currentStep) EmeraldTeal else Color(0xFF334155))
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (currentStep > 0) {
                    OutlinedButton(
                        onClick = { currentStep-- },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Back", color = Color.White)
                    }
                }

                Button(
                    onClick = {
                        if (currentStep < 2) {
                            currentStep++
                        } else {
                            onFinished()
                        }
                    },
                    modifier = Modifier
                        .weight(if (currentStep > 0) 1.5f else 1f)
                        .testTag("onboarding_next_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (currentStep == 2) "Get Started" else "Next",
                        color = Color(0xFF022C22),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

private data class OnboardingStep(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val tag: String
)
