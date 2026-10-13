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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.SecurityEventEntity
import com.example.ui.components.EmptyState
import com.example.ui.components.FilterChipCyber
import com.example.ui.components.NotificationItem
import com.example.ui.components.ScreenHeader
import com.example.ui.theme.BorderCyan
import com.example.ui.theme.PrimaryBg
import com.example.ui.theme.PrimaryCyan
import com.example.ui.theme.PrimaryText
import com.example.ui.theme.SecondaryText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NotificationsScreen(
    events: List<SecurityEventEntity>,
    onNavigateToDevice: ((mac: String?) -> Unit)? = null,
    onNavigateToAudit: (() -> Unit)? = null,
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableStateOf("All") }
    var readIds by remember { mutableStateOf(setOf<String>()) }

    val filteredEvents = when (selectedTab) {
        "Alerts" -> events.filter { it.severity.equals("CRITICAL", true) || it.severity.equals("WARNING", true) }
        "System" -> events.filter { it.severity.equals("INFO", true) || it.type.contains("ROUTER", true) }
        else -> events
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBg)
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .testTag("notifications_screen")
    ) {
        ScreenHeader(
            title = "Notifications & Alerts",
            subtitle = "Screen 18: Security Events & Channel Alerts",
            onBack = onBack,
            action = {
                IconButton(
                    onClick = { readIds = events.map { it.id }.toSet() },
                    modifier = Modifier.testTag("mark_all_read_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.DoneAll,
                        contentDescription = "Mark all read",
                        tint = PrimaryCyan
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Tabs: All, Alerts, System
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChipCyber(
                label = "All",
                selected = selectedTab == "All",
                count = events.size,
                onClick = { selectedTab = "All" }
            )
            FilterChipCyber(
                label = "Alerts",
                selected = selectedTab == "Alerts",
                count = events.count { it.severity.equals("CRITICAL", true) || it.severity.equals("WARNING", true) },
                onClick = { selectedTab = "Alerts" }
            )
            FilterChipCyber(
                label = "System",
                selected = selectedTab == "System",
                count = events.count { it.severity.equals("INFO", true) },
                onClick = { selectedTab = "System" }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredEvents.isEmpty()) {
            EmptyState(
                title = "No Notifications",
                message = "Your Wi-Fi network has no active alerts under this filter. All normal.",
                icon = Icons.Default.Notifications
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredEvents, key = { it.id }) { ev ->
                    val isRead = readIds.contains(ev.id)
                    val dateFormatted = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
                        .format(Date(ev.timestamp))

                    NotificationItem(
                        title = ev.title.ifBlank { ev.type.replace("_", " ") },
                        message = ev.description,
                        timestamp = dateFormatted,
                        severity = ev.severity,
                        isRead = isRead,
                        onClick = {
                            readIds = readIds + ev.id
                            if (ev.type.contains("DEVICE", true) && onNavigateToDevice != null) {
                                onNavigateToDevice(ev.relatedDeviceId)
                            } else if (onNavigateToAudit != null) {
                                onNavigateToAudit()
                            }
                        }
                    )
                }
            }
        }
    }
}
