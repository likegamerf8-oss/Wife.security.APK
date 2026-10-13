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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.InfoCard
import com.example.ui.components.OutlinedButtonCyber
import com.example.ui.components.PrimaryButton
import com.example.ui.components.ScreenHeader
import com.example.ui.theme.AlertRed
import com.example.ui.theme.BorderCyan
import com.example.ui.theme.InputBg
import com.example.ui.theme.PrimaryBg
import com.example.ui.theme.PrimaryCyan
import com.example.ui.theme.PrimaryText
import com.example.ui.theme.SecondaryText

@Composable
fun RouterLoginScreen(
    initialGatewayIp: String = "192.168.0.1",
    detectedManufacturer: String = "TP-Link",
    isAuthenticating: Boolean = false,
    errorMessage: String? = null,
    onAuthenticate: (ip: String, username: String, pass: String) -> Unit,
    onSkipReadOnly: () -> Unit,
    onBack: () -> Unit
) {
    var ipAddress by remember { mutableStateOf(initialGatewayIp) }
    var username by remember { mutableStateOf(if (detectedManufacturer.contains("OpenWrt", true)) "root" else "admin") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBg)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
            .testTag("router_login_screen"),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            ScreenHeader(
                title = "Router Authorization",
                subtitle = "Screen 04: Authenticate with Router Admin",
                onBack = onBack
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Router Graphic
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    color = Color(0xFF0F395A),
                    shape = CircleShape,
                    border = androidx.compose.foundation.BorderStroke(2.dp, PrimaryCyan),
                    modifier = Modifier.size(90.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Router,
                            contentDescription = "Router",
                            tint = PrimaryCyan,
                            modifier = Modifier.size(46.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Connect to $detectedManufacturer Gateway",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = PrimaryText,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (!errorMessage.isNullOrBlank()) {
                InfoCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Error",
                            tint = AlertRed,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = errorMessage,
                            color = AlertRed,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Router IP Address Field
            Text(
                text = "Router IP / Gateway Address",
                color = SecondaryText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = ipAddress,
                onValueChange = { ipAddress = it },
                placeholder = { Text("e.g. 192.168.0.1", color = Color(0xFF64748B)) },
                leadingIcon = {
                    Icon(Icons.Default.Router, contentDescription = null, tint = SecondaryText)
                },
                singleLine = true,
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
                    .testTag("router_ip_field")
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Admin Username Field
            Text(
                text = "Admin Username",
                color = SecondaryText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                placeholder = { Text("admin / root", color = Color(0xFF64748B)) },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null, tint = SecondaryText)
                },
                singleLine = true,
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
                    .testTag("admin_username_field")
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Admin Password Field
            Text(
                text = "Admin Password",
                color = SecondaryText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Enter router admin password", color = Color(0xFF64748B)) },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = SecondaryText)
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = SecondaryText
                        )
                    }
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
                    .testTag("admin_password_field")
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Short Security Explanation
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
                        text = "Router administrator credentials are saved exclusively within Android's private encrypted keystore. They are never transmitted over the Internet, never sent to AI services, and never shared.",
                        fontSize = 11.sp,
                        color = SecondaryText,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Bottom Action Buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            PrimaryButton(
                text = if (isAuthenticating) "Verifying Credentials..." else "Connect & Authorize",
                onClick = { onAuthenticate(ipAddress, username, password) },
                enabled = !isAuthenticating && ipAddress.isNotBlank(),
                testTag = "connect_router_button"
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButtonCyber(
                text = "Skip / Read Only Diagnostics Mode",
                onClick = onSkipReadOnly,
                testTag = "skip_read_only_button"
            )
        }
    }
}
