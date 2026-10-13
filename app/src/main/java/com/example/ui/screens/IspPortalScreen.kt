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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.IspCustomerEntity
import com.example.data.local.entity.IspOrganizationEntity
import com.example.ui.components.StatusBadge
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DeepNavyDark
import com.example.ui.theme.EmeraldTeal
import com.example.ui.theme.MidnightSurface
import com.example.ui.theme.WarningAmber

@Composable
fun IspPortalScreen(
    organizations: List<IspOrganizationEntity>,
    customers: List<IspCustomerEntity>,
    onBack: () -> Unit
) {
    var selectedOrgId by remember { mutableStateOf(organizations.firstOrNull()?.id ?: "isp_link3") }
    var selectedRole by remember { mutableStateOf("OPERATOR") } // ADMIN, OPERATOR, SUPPORT_AGENT
    var searchQuery by remember { mutableStateOf("") }

    val currentOrg = organizations.find { it.id == selectedOrgId } ?: organizations.firstOrNull()
    val orgCustomers = customers.filter { it.organizationId == selectedOrgId && (searchQuery.isBlank() || it.customerName.contains(searchQuery, ignoreCase = true) || it.customerCode.contains(searchQuery, ignoreCase = true)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyDark)
            .padding(horizontal = 20.dp)
            .testTag("isp_portal_screen")
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
                    text = "ISP Enterprise Management Portal",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Authorized BTRC ISP & TR-069 ACS Gateway",
                    fontSize = 11.sp,
                    color = CyberCyan
                )
            }
            Surface(
                color = Color(0xFF064E3B),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = "TENANT ISOLATED",
                    color = EmeraldTeal,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Organization Selector Chips
        Text("Select Licensed ISP Organization:", fontSize = 11.sp, color = Color(0xFF94A3B8))
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            organizations.forEach { org ->
                val isSelected = org.id == selectedOrgId
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) Color(0xFF064E3B) else MidnightSurface,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedOrgId = org.id }
                ) {
                    Text(
                        text = org.name.split(" ").first(),
                        color = if (isSelected) EmeraldTeal else Color(0xFF94A3B8),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Organization Info Card
        if (currentOrg != null) {
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
                        Text(currentOrg.name, fontWeight = FontWeight.Bold, color = Color.White, style = MaterialTheme.typography.bodyLarge)
                        StatusBadge(status = "FULLY_VERIFIED")
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("License: ${currentOrg.btrcLicenseNumber} • Hotline: ${currentOrg.supportHotline}", fontSize = 11.sp, color = CyberCyan)
                    Text("ACS URL: ${currentOrg.tr069AcsUrl}", fontSize = 10.sp, color = Color(0xFF94A3B8))
                    Text("Certified ONTs: ${currentOrg.supportedOntTypes}", fontSize = 10.sp, color = Color(0xFFCBD5E1))

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("OPERATOR" to "Operator", "SUPPORT_AGENT" to "Support", "ADMIN" to "Admin").forEach { (role, label) ->
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (selectedRole == role) Color(0xFF1E293B) else Color.Transparent,
                                modifier = Modifier.clickable { selectedRole = role }
                            ) {
                                Text(
                                    text = "Role: $label",
                                    fontSize = 10.sp,
                                    color = if (selectedRole == role) EmeraldTeal else Color(0xFF64748B),
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search subscriber name, customer code...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = EmeraldTeal) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EmeraldTeal,
                unfocusedBorderColor = Color(0xFF334155),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(orgCustomers) { cust ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MidnightSurface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(cust.customerName, fontWeight = FontWeight.Bold, color = Color.White, style = MaterialTheme.typography.bodyMedium)
                                Text("ID: ${cust.customerCode} • PPPoE: ${cust.pppoeUsername}", fontSize = 11.sp, color = CyberCyan)
                            }
                            StatusBadge(status = if (cust.isOnline) "CONNECTED" else "OFFLINE")
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text("Plan: ${cust.servicePackage}", fontSize = 11.sp, color = Color(0xFFCBD5E1))
                        Text("Address: ${cust.address} • Phone: ${cust.phone}", fontSize = 10.sp, color = Color(0xFF94A3B8))

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.SignalCellularAlt, contentDescription = null, tint = EmeraldTeal, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Optical Rx: ${cust.ontOpticalPowerDbm} dBm (Healthy)", fontSize = 10.sp, color = EmeraldTeal)
                            }

                            Text("TR-069 Inform: Active", fontSize = 10.sp, color = Color(0xFF64748B))
                        }
                    }
                }
            }
        }
    }
}
