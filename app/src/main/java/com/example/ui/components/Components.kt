package com.example.ui.components

import com.example.R
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.EmeraldTeal
import com.example.ui.theme.MidnightSurface
import com.example.ui.theme.SurfaceVariantDark
import com.example.ui.theme.WarningAmber

@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, label) = when (status.uppercase()) {
        "CONNECTED", "AUTHORIZED", "FULLY_VERIFIED", "APPROVED" ->
            Triple(Color(0xFF064E3B), EmeraldTeal, if (status == "CONNECTED") "Connected & Verified" else status)
        "CONNECTED_LIMITED", "PARTIALLY_VERIFIED" ->
            Triple(Color(0xFF78350F), WarningAmber, "Limited Capabilities")
        "DEMO_MODE", "TEST_DOUBLE" ->
            Triple(Color(0xFF1E293B), CyberCyan, "Test Double / Sandbox")
        "BLOCKED", "REVOKED", "CRITICAL" ->
            Triple(Color(0xFF7F1D1D), AlertRed, "Blocked / Revoked")
        "PENDING", "WARNING" ->
            Triple(Color(0xFF451A03), WarningAmber, "Pending Approval")
        else ->
            Triple(Color(0xFF334155), Color.LightGray, status)
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.testTag("status_badge_${status.lowercase()}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(textColor)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                color = textColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun MetricStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color = EmeraldTeal,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(14.dp),
        modifier = modifier
            .testTag("stat_card_${title.lowercase().replace(" ", "_")}")
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
fun HonestCapabilityItem(
    title: String,
    supported: Boolean,
    note: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (supported) Icons.Default.CheckCircle else Icons.Default.Warning,
            contentDescription = if (supported) "Supported" else "Unsupported",
            tint = if (supported) EmeraldTeal else WarningAmber,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
            if (!note.isNullOrBlank()) {
                Text(
                    text = note,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            }
        }
        Text(
            text = if (supported) "Verified" else "Unavailable",
            color = if (supported) EmeraldTeal else WarningAmber,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            if (!subtitle.isNullOrBlank()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        if (!actionText.isNullOrBlank() && onActionClick != null) {
            Text(
                text = actionText,
                style = MaterialTheme.typography.bodySmall,
                color = CyberCyan,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clickable { onActionClick() }
                    .padding(4.dp)
            )
        }
    }
}

// -------------------------------------------------------------
// EXACT PART 2 REUSABLE COMPONENTS
// -------------------------------------------------------------

@Composable
fun AppLogo(
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 80.dp
) {
    Surface(
        shape = CircleShape,
        color = Color(0xFF0F172A),
        border = androidx.compose.foundation.BorderStroke(2.dp, com.example.ui.theme.BorderCyan),
        modifier = modifier
            .size(size)
            .clip(CircleShape)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.wifi_security_icon_1791718058809),
                contentDescription = "Wi-Fi Security Logo",
                modifier = Modifier.size(size * 0.8f)
            )
        }
    }
}

@Composable
fun ScreenHeader(
    title: String,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null,
    action: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            if (onBack != null) {
                BackButton(onClick = onBack)
                Spacer(modifier = Modifier.width(8.dp))
            }
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = com.example.ui.theme.PrimaryText
                )
                if (!subtitle.isNullOrBlank()) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = com.example.ui.theme.SecondaryText
                    )
                }
            }
        }
        if (action != null) {
            action()
        }
    }
}

@Composable
fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(48.dp)
            .testTag("back_button")
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            tint = com.example.ui.theme.PrimaryText
        )
    }
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    testTag: String = "primary_button"
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = com.example.ui.theme.SuccessGreen,
            disabledContainerColor = Color(0xFF1E3A3A)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag(testTag)
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF022C22),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            color = Color(0xFF022C22),
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    testTag: String = "secondary_button"
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = com.example.ui.theme.SecondaryCyan,
            disabledContainerColor = Color(0xFF1E293B)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag(testTag)
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF022C22),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            color = Color(0xFF022C22),
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
    }
}

@Composable
fun OutlinedButtonCyber(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    testTag: String = "outlined_button"
) {
    androidx.compose.material3.OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.BorderCyan),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag(testTag)
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = com.example.ui.theme.PrimaryCyan,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            color = com.example.ui.theme.PrimaryCyan,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun InfoCard(
    modifier: Modifier = Modifier,
    border: Boolean = true,
    content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = com.example.ui.theme.CardBg),
        border = if (border) androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.BorderCyan) else null,
        shape = RoundedCornerShape(14.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
fun RouterCard(
    routerName: String,
    model: String,
    ipAddress: String,
    status: String,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    InfoCard(
        modifier = modifier.then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    color = Color(0xFF0F395A),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Router,
                            contentDescription = "Router",
                            tint = com.example.ui.theme.PrimaryCyan,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = routerName,
                        fontWeight = FontWeight.Bold,
                        color = com.example.ui.theme.PrimaryText,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "$model • $ipAddress",
                        color = com.example.ui.theme.SecondaryText,
                        fontSize = 12.sp
                    )
                }
            }
            StatusChip(status = status)
        }
    }
}

@Composable
fun DeviceCard(
    name: String,
    ipAddress: String,
    macAddress: String?,
    isTrusted: Boolean,
    isBlocked: Boolean,
    deviceType: String = "phone",
    onClick: (() -> Unit)? = null,
    onBlockToggle: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    InfoCard(
        modifier = modifier.then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    color = if (isBlocked) Color(0xFF451A03) else Color(0xFF072740),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.size(42.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Devices,
                            contentDescription = deviceType,
                            tint = if (isBlocked) com.example.ui.theme.AlertRed else com.example.ui.theme.PrimaryCyan,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = name,
                        fontWeight = FontWeight.Bold,
                        color = com.example.ui.theme.PrimaryText,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "$ipAddress${if (macAddress != null) " • $macAddress" else ""}",
                        color = com.example.ui.theme.SecondaryText,
                        fontSize = 11.sp
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isBlocked) {
                    StatusChip(status = "BLOCKED")
                } else if (isTrusted) {
                    StatusChip(status = "TRUSTED")
                } else {
                    StatusChip(status = "UNVERIFIED")
                }
            }
        }
    }
}

@Composable
fun StatusChip(
    status: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, label) = when (status.uppercase()) {
        "CONNECTED", "ONLINE", "ACTIVE", "SUPPORTED", "TRUSTED" ->
            Triple(Color(0xFF064E3B), com.example.ui.theme.SuccessGreen, status)
        "UNVERIFIED", "LIMITED", "PARTIALLY_SUPPORTED", "WARNING" ->
            Triple(Color(0xFF451A03), com.example.ui.theme.WarningAmber, status)
        "BLOCKED", "OFFLINE", "UNSUPPORTED", "FAILED", "CRITICAL" ->
            Triple(Color(0xFF7F1D1D), com.example.ui.theme.AlertRed, status)
        "AUTHENTICATION_REQUIRED", "PENDING" ->
            Triple(Color(0xFF1E3A5F), com.example.ui.theme.PrimaryCyan, status)
        else ->
            Triple(Color(0xFF1E293B), com.example.ui.theme.SecondaryText, status)
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(6.dp),
        modifier = modifier
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

@Composable
fun SecurityScoreCard(
    score: Int,
    label: String,
    grade: String = "A",
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    InfoCard(
        modifier = modifier.then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Network Security Score",
                    color = com.example.ui.theme.SecondaryText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$score",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (score >= 80) com.example.ui.theme.SuccessGreen else if (score >= 60) com.example.ui.theme.WarningAmber else com.example.ui.theme.AlertRed
                    )
                    Text(
                        text = " / 100",
                        color = com.example.ui.theme.MutedText,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }
                Text(
                    text = label,
                    color = com.example.ui.theme.PrimaryCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Surface(
                shape = CircleShape,
                color = Color(0xFF0F395A),
                border = androidx.compose.foundation.BorderStroke(2.dp, if (score >= 80) com.example.ui.theme.SuccessGreen else com.example.ui.theme.WarningAmber),
                modifier = Modifier.size(64.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = grade,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (score >= 80) com.example.ui.theme.SuccessGreen else com.example.ui.theme.WarningAmber
                    )
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color = com.example.ui.theme.SuccessGreen,
    subtitle: String? = null,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    InfoCard(
        modifier = modifier.then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = com.example.ui.theme.SecondaryText
            )
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = color
        )
        if (!subtitle.isNullOrBlank()) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = com.example.ui.theme.MutedText,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String = "Search devices, IPs, MAC...",
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text(placeholder, color = com.example.ui.theme.MutedText, fontSize = 13.sp) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = com.example.ui.theme.SecondaryText
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                        tint = com.example.ui.theme.SecondaryText
                    )
                }
            }
        },
        singleLine = true,
        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
            focusedContainerColor = com.example.ui.theme.InputBg,
            unfocusedContainerColor = com.example.ui.theme.InputBg,
            focusedBorderColor = com.example.ui.theme.PrimaryCyan,
            unfocusedBorderColor = com.example.ui.theme.BorderCyan,
            focusedTextColor = com.example.ui.theme.PrimaryText,
            unfocusedTextColor = com.example.ui.theme.PrimaryText
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("search_field")
    )
}

@Composable
fun FilterChipCyber(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    count: Int? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        color = if (selected) Color(0xFF0F395A) else com.example.ui.theme.InputBg,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (selected) com.example.ui.theme.PrimaryCyan else com.example.ui.theme.BorderCyan
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .clickable { onClick() }
            .testTag("filter_chip_${label.lowercase()}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
        ) {
            Text(
                text = label,
                color = if (selected) com.example.ui.theme.PrimaryCyan else com.example.ui.theme.SecondaryText,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 12.sp
            )
            if (count != null) {
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                    color = if (selected) com.example.ui.theme.PrimaryCyan else Color(0xFF1E293B),
                    shape = CircleShape,
                    modifier = Modifier.size(18.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "$count",
                            color = if (selected) Color(0xFF022C22) else Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "Admin Password",
    modifier: Modifier = Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }

    androidx.compose.material3.OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = com.example.ui.theme.SecondaryText) },
        singleLine = true,
        visualTransformation = if (passwordVisible) androidx.compose.ui.text.input.VisualTransformation.None else androidx.compose.ui.text.input.PasswordVisualTransformation(),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = com.example.ui.theme.SecondaryText
            )
        },
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                    tint = com.example.ui.theme.SecondaryText
                )
            }
        },
        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
            focusedContainerColor = com.example.ui.theme.InputBg,
            unfocusedContainerColor = com.example.ui.theme.InputBg,
            focusedBorderColor = com.example.ui.theme.PrimaryCyan,
            unfocusedBorderColor = com.example.ui.theme.BorderCyan,
            focusedTextColor = com.example.ui.theme.PrimaryText,
            unfocusedTextColor = com.example.ui.theme.PrimaryText
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("password_field")
    )
}

@Composable
fun SettingsRow(
    title: String,
    subtitle: String? = null,
    icon: ImageVector,
    trailing: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(vertical = 12.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Surface(
                color = Color(0xFF0F395A),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.size(38.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = com.example.ui.theme.PrimaryCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = com.example.ui.theme.PrimaryText
                )
                if (!subtitle.isNullOrBlank()) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = com.example.ui.theme.SecondaryText
                    )
                }
            }
        }
        if (trailing != null) {
            trailing()
        }
    }
}

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    confirmText: String = "Confirm",
    isDestructive: Boolean = false,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, color = com.example.ui.theme.PrimaryText, fontWeight = FontWeight.Bold) },
        text = { Text(message, color = com.example.ui.theme.SecondaryText) },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = if (isDestructive) com.example.ui.theme.AlertRed else com.example.ui.theme.SuccessGreen
                )
            ) {
                Text(confirmText, color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text("Cancel", color = com.example.ui.theme.SecondaryText)
            }
        },
        containerColor = com.example.ui.theme.CardBg,
        textContentColor = com.example.ui.theme.PrimaryText
    )
}

@Composable
fun EmptyState(
    title: String,
    message: String,
    icon: ImageVector = Icons.Default.Info,
    actionText: String? = null,
    onAction: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            color = Color(0xFF0F395A),
            shape = CircleShape,
            modifier = Modifier.size(64.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = com.example.ui.theme.PrimaryCyan,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = com.example.ui.theme.PrimaryText
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = com.example.ui.theme.SecondaryText,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        if (!actionText.isNullOrBlank() && onAction != null) {
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButtonCyber(
                text = actionText,
                onClick = onAction,
                modifier = Modifier.width(180.dp)
            )
        }
    }
}

@Composable
fun LoadingState(
    message: String = "Scanning router...",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = com.example.ui.theme.PrimaryCyan,
            modifier = Modifier.size(44.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = com.example.ui.theme.SecondaryText
        )
    }
}

@Composable
fun ErrorState(
    title: String = "Error",
    message: String,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Error",
            tint = com.example.ui.theme.AlertRed,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = com.example.ui.theme.PrimaryText
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = com.example.ui.theme.SecondaryText,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        if (onRetry != null) {
            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButton(
                text = "Retry",
                onClick = onRetry,
                modifier = Modifier.width(140.dp)
            )
        }
    }
}

@Composable
fun NotificationItem(
    title: String,
    message: String,
    timestamp: String,
    severity: String = "INFO",
    isRead: Boolean = false,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    InfoCard(
        modifier = modifier.then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                color = when (severity.uppercase()) {
                    "CRITICAL", "ALERT" -> Color(0xFF451A03)
                    "WARNING" -> Color(0xFF3B2700)
                    else -> Color(0xFF0F395A)
                },
                shape = CircleShape,
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = when (severity.uppercase()) {
                            "CRITICAL", "ALERT" -> Icons.Default.Warning
                            "WARNING" -> Icons.Default.Info
                            else -> Icons.Default.Shield
                        },
                        contentDescription = severity,
                        tint = when (severity.uppercase()) {
                            "CRITICAL", "ALERT" -> com.example.ui.theme.AlertRed
                            "WARNING" -> com.example.ui.theme.WarningAmber
                            else -> com.example.ui.theme.PrimaryCyan
                        },
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = title,
                        fontWeight = if (!isRead) FontWeight.Bold else FontWeight.Medium,
                        color = com.example.ui.theme.PrimaryText,
                        fontSize = 14.sp
                    )
                    Text(
                        text = timestamp,
                        color = com.example.ui.theme.MutedText,
                        fontSize = 11.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = message,
                    color = com.example.ui.theme.SecondaryText,
                    fontSize = 12.sp
                )
            }
            if (!isRead) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(com.example.ui.theme.PrimaryCyan)
                )
            }
        }
    }
}
