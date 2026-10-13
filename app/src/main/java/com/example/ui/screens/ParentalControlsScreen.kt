package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.data.local.entity.DeviceEntity
import com.example.data.local.entity.ParentalProfileEntity
import com.example.data.local.entity.RouterEntity
import com.example.ui.components.StatusBadge
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DeepNavyDark
import com.example.ui.theme.EmeraldTeal
import com.example.ui.theme.MidnightSurface
import com.example.ui.theme.WarningAmber

@Composable
fun ParentalControlsScreen(
    router: RouterEntity?,
    profiles: List<ParentalProfileEntity>,
    devices: List<DeviceEntity>,
    onSaveProfile: (ParentalProfileEntity) -> Unit,
    onDeleteProfile: (String) -> Unit,
    onBack: () -> Unit
) {
    var showAddForm by remember { mutableStateOf(false) }
    var newProfileName by remember { mutableStateOf("") }
    var selectedBedtimeHour by remember { mutableIntStateOf(22) }
    var selectedWakeupHour by remember { mutableIntStateOf(6) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyDark)
            .padding(horizontal = 20.dp)
            .testTag("parental_controls_screen")
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
                    text = "Family & Parental Curfew",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Schedule access curfews & protect children",
                    fontSize = 11.sp,
                    color = CyberCyan
                )
            }
            Button(
                onClick = { showAddForm = !showAddForm },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF022C22), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("New Profile", color = Color(0xFF022C22), fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (showAddForm) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MidnightSurface),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Create Family Profile", fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = newProfileName,
                        onValueChange = { newProfileName = it },
                        label = { Text("Profile Name (e.g. Study Room, Kids)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Curfew Bedtime: $selectedBedtimeHour:00 PM  •  Wakeup: $selectedWakeupHour:00 AM", fontSize = 11.sp, color = CyberCyan)

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            if (newProfileName.isNotBlank()) {
                                onSaveProfile(
                                    ParentalProfileEntity(
                                        id = "parent_" + System.currentTimeMillis(),
                                        routerId = router?.id ?: "rtr_default",
                                        profileName = newProfileName,
                                        assignedDeviceMacs = "2A:B5:1C:DF:88:99",
                                        bedTimeHour = selectedBedtimeHour,
                                        bedTimeMinute = 0,
                                        wakeUpHour = selectedWakeupHour,
                                        isScheduleActive = true,
                                        isBlockedNow = false
                                    )
                                )
                                newProfileName = ""
                                showAddForm = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Profile Schedule", color = Color(0xFF022C22), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(profiles) { profile ->
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.ChildCare, contentDescription = null, tint = CyberCyan)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(profile.profileName, fontWeight = FontWeight.Bold, color = Color.White, style = MaterialTheme.typography.bodyLarge)
                            }

                            IconButton(onClick = { onDeleteProfile(profile.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = AlertRed)
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Nightly Curfew: ${profile.bedTimeHour}:00 - ${profile.wakeUpHour}:00", fontSize = 11.sp, color = Color(0xFFCBD5E1))
                                Text("Assigned MACs: ${profile.assignedDeviceMacs}", fontSize = 10.sp, color = Color(0xFF94A3B8))
                            }

                            Switch(
                                checked = profile.isScheduleActive,
                                onCheckedChange = { active ->
                                    onSaveProfile(profile.copy(isScheduleActive = active))
                                },
                                colors = SwitchDefaults.colors(checkedThumbColor = EmeraldTeal)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (profile.isBlockedNow) "Access Paused Now" else "Access Permitted",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (profile.isBlockedNow) AlertRed else EmeraldTeal
                            )

                            Button(
                                onClick = {
                                    onSaveProfile(profile.copy(isBlockedNow = !profile.isBlockedNow))
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (profile.isBlockedNow) EmeraldTeal else AlertRed
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Block, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (profile.isBlockedNow) "Resume Access" else "Pause Access Now",
                                    fontSize = 10.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
