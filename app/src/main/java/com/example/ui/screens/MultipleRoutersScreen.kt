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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Router
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
import com.example.data.local.entity.RouterEntity
import com.example.ui.components.StatusBadge
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DeepNavyDark
import com.example.ui.theme.EmeraldTeal
import com.example.ui.theme.MidnightSurface
import com.example.ui.theme.WarningAmber

@Composable
fun MultipleRoutersScreen(
    routers: List<RouterEntity>,
    activeRouter: RouterEntity?,
    onSwitchRouter: (String) -> Unit,
    onRemoveRouter: (String) -> Unit,
    onAddNewRouter: () -> Unit,
    onBack: () -> Unit
) {
    var routerToDelete by remember { mutableStateOf<RouterEntity?>(null) }
    var routerToSwitch by remember { mutableStateOf<RouterEntity?>(null) }

    if (routerToSwitch != null) {
        val target = routerToSwitch!!
        AlertDialog(
            onDismissRequest = { routerToSwitch = null },
            containerColor = MidnightSurface,
            title = {
                Text("Switch Active Network Management", color = Color.White, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "Switch active security management to '${target.name}' (${target.ipAddress})? " +
                            "Subsequent telemetry, device queries, and block commands will target this router exclusively.",
                    color = Color(0xFFCBD5E1),
                    fontSize = 12.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onSwitchRouter(target.id)
                        routerToSwitch = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal)
                ) {
                    Text("Switch Management", color = Color(0xFF022C22), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { routerToSwitch = null }) {
                    Text("Cancel", color = Color.White)
                }
            }
        )
    }

    if (routerToDelete != null) {
        val target = routerToDelete!!
        AlertDialog(
            onDismissRequest = { routerToDelete = null },
            containerColor = MidnightSurface,
            title = {
                Text("Remove Registered Router?", color = AlertRed, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "Are you sure you want to remove '${target.name}' from your management console? " +
                            "Local authorizations and cached statistics will be removed.",
                    color = Color(0xFFCBD5E1),
                    fontSize = 12.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onRemoveRouter(target.id)
                        routerToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AlertRed)
                ) {
                    Text("Remove", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { routerToDelete = null }) {
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
            .testTag("multiple_routers_screen")
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
                    text = "Multiple Router Management",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "${routers.size} registered network gateways",
                    fontSize = 11.sp,
                    color = CyberCyan
                )
            }
            Button(
                onClick = onAddNewRouter,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF022C22), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add", color = Color(0xFF022C22), fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(routers) { rtr ->
                val isActive = rtr.id == activeRouter?.id

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isActive) Color(0xFF064E3B) else MidnightSurface
                    ),
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
                                Surface(
                                    shape = CircleShape,
                                    color = if (isActive) EmeraldTeal else Color(0xFF1E293B),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            Icons.Default.Router,
                                            contentDescription = null,
                                            tint = if (isActive) Color(0xFF022C22) else EmeraldTeal,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = rtr.name,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = "${rtr.manufacturer} • ${rtr.model}",
                                        fontSize = 11.sp,
                                        color = if (isActive) Color(0xFFA7F3D0) else Color(0xFF94A3B8)
                                    )
                                }
                            }

                            if (isActive) {
                                Surface(
                                    color = Color(0xFF022C22),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "ACTIVE",
                                        color = EmeraldTeal,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Gateway: ${rtr.ipAddress}:${rtr.port} • Protocol: ${rtr.connectionType}",
                            fontSize = 11.sp,
                            color = Color(0xFFCBD5E1)
                        )

                        Text(
                            text = "ISP Profile: ${rtr.ispName} • Status: ${rtr.status}",
                            fontSize = 10.sp,
                            color = Color(0xFF94A3B8)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (!isActive) {
                                Button(
                                    onClick = { routerToSwitch = rtr },
                                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Switch to this Router", color = Color(0xFF022C22), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                IconButton(onClick = { routerToDelete = rtr }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = AlertRed)
                                }
                            } else {
                                Text(
                                    text = "Currently Linked & Monitored",
                                    fontSize = 11.sp,
                                    color = EmeraldTeal,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
