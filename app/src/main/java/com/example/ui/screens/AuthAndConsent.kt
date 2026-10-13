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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DeepNavyDark
import com.example.ui.theme.EmeraldTeal
import com.example.ui.theme.MidnightSurface
import com.example.ui.theme.WarningAmber

@Composable
fun AuthScreen(
    onAuthenticated: () -> Unit,
    onLoginSubmit: (email: String, name: String, role: String) -> Unit
) {
    var isCreateAccount by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("dkhdarman24221@gmail.com") }
    var password by remember { mutableStateOf("DhakaSecurePass#2026") }
    var displayName by remember { mutableStateOf("Arman Hossain") }
    var selectedRole by remember { mutableStateOf("OWNER") }
    var showForgotPassword by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyDark)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF0F172A),
            modifier = Modifier.size(72.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = "Shield",
                    tint = EmeraldTeal,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isCreateAccount) "Create Security Account" else "Network Owner Sign-In",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Text(
            text = "Universal Wi-Fi Security Bangladesh",
            style = MaterialTheme.typography.bodySmall,
            color = CyberCyan
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = MidnightSurface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                if (isCreateAccount) {
                    OutlinedTextField(
                        value = displayName,
                        onValueChange = { displayName = it },
                        label = { Text("Full Name / Organization") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = EmeraldTeal) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("name_input"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldTeal,
                            unfocusedBorderColor = Color(0xFF334155),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = EmeraldTeal) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("email_input"),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldTeal,
                        unfocusedBorderColor = Color(0xFF334155),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = EmeraldTeal) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("password_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldTeal,
                        unfocusedBorderColor = Color(0xFF334155),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Authorization Role:",
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8),
                    fontWeight = FontWeight.Medium
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("OWNER", "ADMIN", "SUPPORT").forEach { role ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (selectedRole == role) Color(0xFF064E3B) else Color(0xFF1E293B),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("role_btn_${role.lowercase()}"),
                            onClick = { selectedRole = role }
                        ) {
                            Text(
                                text = role,
                                color = if (selectedRole == role) EmeraldTeal else Color(0xFF94A3B8),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }

                if (feedbackMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = feedbackMessage!!,
                        color = EmeraldTeal,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        onLoginSubmit(email, displayName, selectedRole)
                        onAuthenticated()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("submit_auth_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (isCreateAccount) "Create Account" else "Sign In",
                        color = Color(0xFF022C22),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = {
                        onLoginSubmit("google.user@dhaka.net", "Google Verified Owner", "OWNER")
                        onAuthenticated()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("google_auth_btn"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Sign In with Google", color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { isCreateAccount = !isCreateAccount }) {
                Text(
                    text = if (isCreateAccount) "Already have an account? Sign In" else "New network? Create Account",
                    color = CyberCyan,
                    fontSize = 12.sp
                )
            }

            TextButton(onClick = { feedbackMessage = "Password reset instructions sent to $email." }) {
                Text(
                    text = "Forgot password?",
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun OwnershipConsentScreen(
    networkSsid: String = "Dhaka_Secure_Mesh",
    onConsentApproved: () -> Unit
) {
    var check1 by remember { mutableStateOf(false) }
    var check2 by remember { mutableStateOf(false) }
    var check3 by remember { mutableStateOf(false) }
    val allAgreed = check1 && check2 && check3

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyDark)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF78350F),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "LEGAL VERIFICATION REQUIRED",
                        color = WarningAmber,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = "Network Ownership & Consent",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Before connecting to '$networkSsid', Bangladesh telecom compliance and security standards require verification that you are the authorized owner or network administrator.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF94A3B8)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = MidnightSurface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ConsentCheckboxRow(
                        checked = check1,
                        onCheckedChange = { check1 = it },
                        text = "I declare under penalty of law that I am the legal owner or appointed network administrator of this Wi-Fi network and router.",
                        tag = "consent_check_owner"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ConsentCheckboxRow(
                        checked = check2,
                        onCheckedChange = { check2 = it },
                        text = "I authorize this application to read connected station telemetry, observe associations, and enforce MAC access-lists or device quarantines.",
                        tag = "consent_check_control"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ConsentCheckboxRow(
                        checked = check3,
                        onCheckedChange = { check3 = it },
                        text = "I understand that pre-access authorization is subject to router hardware capabilities and will not exploit or bypass manufacturer firmware.",
                        tag = "consent_check_hardware"
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(top = 24.dp)) {
            Button(
                onClick = onConsentApproved,
                enabled = allAgreed,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("confirm_ownership_btn"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EmeraldTeal,
                    disabledContainerColor = Color(0xFF1E293B)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Confirm Ownership & Proceed",
                    color = if (allAgreed) Color(0xFF022C22) else Color(0xFF64748B),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun ConsentCheckboxRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    text: String,
    tag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(tag),
        verticalAlignment = Alignment.Top
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = EmeraldTeal,
                uncheckedColor = Color(0xFF475569)
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = if (checked) Color.White else Color(0xFFCBD5E1),
            lineHeight = 18.sp,
            modifier = Modifier.padding(top = 10.dp)
        )
    }
}
