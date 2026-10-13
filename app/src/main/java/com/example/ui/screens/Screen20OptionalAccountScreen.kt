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
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AppLogo
import com.example.ui.components.InfoCard
import com.example.ui.components.OutlinedButtonCyber
import com.example.ui.components.PrimaryButton
import com.example.ui.components.ScreenHeader
import com.example.ui.theme.BorderCyan
import com.example.ui.theme.InputBg
import com.example.ui.theme.PrimaryBg
import com.example.ui.theme.PrimaryCyan
import com.example.ui.theme.PrimaryText
import com.example.ui.theme.SecondaryText
import com.example.ui.theme.SuccessGreen

@Composable
fun OptionalAccountScreen(
    onContinueLocalMode: () -> Unit,
    onLoginSubmit: ((email: String, role: String) -> Unit)? = null,
    onBack: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var technicianRole by remember { mutableStateOf("ISP Fleet Engineer") }
    var signedInNotice by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBg)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
            .testTag("optional_account_screen"),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            ScreenHeader(
                title = "Optional Cloud & ISP Account",
                subtitle = "Screen 20: Account-Free Local Mode & Optional Sync",
                onBack = onBack
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Logo & Badge
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AppLogo(size = 80.dp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Prominent Local-First Banner
            Surface(
                color = Color(0xFF064E3B),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SuccessGreen),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = SuccessGreen,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "No Account Required for Local Security",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "All local Wi-Fi monitoring, device approval, router management, and network health tools work 100% account-free on your device.",
                            color = Color(0xFFA7F3D0),
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Optional Cloud Sync & ISP Management",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = PrimaryCyan
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "If you manage multi-branch ISP routers or require cloud configuration backups, you can sign in below. This does not affect your local router admin password.",
                color = SecondaryText,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (signedInNotice != null) {
                InfoCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = signedInNotice!!,
                        color = SuccessGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Optional Login Form
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Corporate / ISP Email (Optional)", color = SecondaryText) },
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null, tint = SecondaryText)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = InputBg,
                    unfocusedContainerColor = InputBg,
                    focusedBorderColor = PrimaryCyan,
                    unfocusedBorderColor = BorderCyan,
                    focusedTextColor = PrimaryText,
                    unfocusedTextColor = PrimaryText
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("optional_email_field")
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Organization Password / API Token", color = SecondaryText) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = SecondaryText)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = InputBg,
                    unfocusedContainerColor = InputBg,
                    focusedBorderColor = PrimaryCyan,
                    unfocusedBorderColor = BorderCyan,
                    focusedTextColor = PrimaryText,
                    unfocusedTextColor = PrimaryText
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("optional_password_field")
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButtonCyber(
                text = "Sign In to ISP Cloud Account",
                onClick = {
                    if (email.isNotBlank()) {
                        onLoginSubmit?.invoke(email, technicianRole)
                        signedInNotice = "Signed in as $email ($technicianRole)"
                    }
                },
                enabled = email.isNotBlank(),
                testTag = "isp_cloud_signin_button"
            )
        }

        // Bottom Primary Action: Continue in Local Mode
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            PrimaryButton(
                text = "Continue in Local Mode (No Account Needed)",
                onClick = onContinueLocalMode,
                testTag = "continue_local_mode_button"
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Local data is stored on this phone only and never uploaded.",
                color = SecondaryText,
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
