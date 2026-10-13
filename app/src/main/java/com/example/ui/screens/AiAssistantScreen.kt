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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai.AiSecurityAssistant
import com.example.data.local.entity.DeviceEntity
import com.example.data.local.entity.RouterEntity
import com.example.data.local.entity.SecurityAuditFindingEntity
import com.example.localization.AppLanguage
import com.example.localization.LanguageManager
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DeepNavyDark
import com.example.ui.theme.EmeraldTeal
import com.example.ui.theme.MidnightSurface
import com.example.ui.theme.WarningAmber

@Composable
fun AiAssistantScreen(
    router: RouterEntity?,
    devices: List<DeviceEntity>,
    findings: List<SecurityAuditFindingEntity>,
    onBack: () -> Unit
) {
    var isBangla by remember { mutableStateOf(LanguageManager.currentLanguage == AppLanguage.BANGLA) }
    var selectedTopic by remember { mutableStateOf("DEVICES") } // DEVICES, AUDIT, COMPATIBILITY

    val auditAdvice = AiSecurityAssistant.analyzeSecurityAudit(findings, isBangla)
    val compatibilitySummary = AiSecurityAssistant.explainRouterCompatibility(router, isBangla)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyDark)
            .padding(horizontal = 20.dp)
            .testTag("ai_assistant_screen")
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
                    text = if (isBangla) "এআই সিকিউরিটি অ্যাসিস্ট্যান্ট" else "AI Security Assistant",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = if (isBangla) "নেটওয়ার্ক নিরাপত্তা ও ডিভাইস নির্দেশিকা" else "Intelligent network defense & insights",
                    fontSize = 11.sp,
                    color = CyberCyan
                )
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF1E293B),
                modifier = Modifier.clickable { isBangla = !isBangla }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Language, contentDescription = null, tint = EmeraldTeal, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isBangla) "বাংলা" else "English", color = EmeraldTeal, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Topic Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                "DEVICES" to if (isBangla) "ডিভাইস এনালাইসিস" else "Device Insights",
                "AUDIT" to if (isBangla) "নিরাপত্তা অডিট" else "Security Audit",
                "COMPATIBILITY" to if (isBangla) "রাউটার সামঞ্জস্য" else "Compatibility"
            ).forEach { (topic, label) ->
                val isSelected = selectedTopic == topic
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) Color(0xFF064E3B) else MidnightSurface,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedTopic = topic }
                ) {
                    Text(
                        text = label,
                        color = if (isSelected) EmeraldTeal else Color(0xFF94A3B8),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            when (selectedTopic) {
                "DEVICES" -> {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MidnightSurface),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isBangla) "র্যান্ডমাইজড ম্যাক এড্রেস সম্পর্কে জানুন" else "Understanding Private/Randomized MACs",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 13.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = if (isBangla)
                                        "একটি নতুন অপরিচিত ডিভাইস যুক্ত হলেই সে হ্যাকার নয়। আধুনিক স্মার্টফোনগুলো ব্যবহারকারীর গোপনীয়তা রক্ষার জন্য স্বয়ংক্রিয়ভাবে নকল ম্যাক অ্যাড্রেস তৈরি করে। আমাদের অ্যাপ ডিভাইসটিকে কোয়ারেন্টাইনে রাখে যাতে আপনি যাচাই করতে পারেন।"
                                    else
                                        "An unfamiliar device with an unknown vendor is rarely an intruder. iOS and Android randomize MAC addresses by default for privacy. Universal Wi-Fi Security keeps stations in pre-access quarantine until you confirm who they are.",
                                    fontSize = 11.sp,
                                    color = Color(0xFFCBD5E1),
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }

                    items(devices) { dev ->
                        val advice = AiSecurityAssistant.analyzeDeviceObservation(dev, isBangla)
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MidnightSurface),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "${dev.hostname} (${dev.macAddress})",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (isBangla) advice.summaryBn else advice.summaryEn,
                                    fontSize = 11.sp,
                                    color = Color(0xFFCBD5E1)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = if (isBangla) "পরামর্শ: ${advice.recommendationBn}" else "Recommendation: ${advice.recommendationEn}",
                                    fontSize = 11.sp,
                                    color = EmeraldTeal,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }

                "AUDIT" -> {
                    items(auditAdvice) { advice ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MidnightSurface),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(advice.title, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(if (isBangla) advice.summaryBn else advice.summaryEn, fontSize = 11.sp, color = Color(0xFFCBD5E1))
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(if (isBangla) advice.recommendationBn else advice.recommendationEn, fontSize = 11.sp, color = CyberCyan)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(advice.safetyGuidance, fontSize = 10.sp, color = Color(0xFF94A3B8))
                            }
                        }
                    }
                }

                "COMPATIBILITY" -> {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MidnightSurface),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = if (isBangla) "রাউটার ও ইন্টারনেট মডেল বিশ্লেষণ" else "Router Hardware Analysis",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = compatibilitySummary,
                                    fontSize = 12.sp,
                                    color = Color(0xFFCBD5E1),
                                    lineHeight = 18.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = if (isBangla)
                                        "বাংলাদেশ ইন্টারনেট প্রোভাইডার যেমন Link3, Carnival, Amber IT-এর ফাইবার মডেমে প্রি-অ্যাক্সেস অনুমোদনের বদলে ইনস্ট্যান্ট ম্যাক ব্ল্যাকলিস্ট ব্যবহার করা সবচেয়ে নির্ভরযোগ্য পদ্ধতি।"
                                    else
                                        "For Bangladesh fiber ISPs (Link3, Carnival, Amber IT), standard GPON ONTs are best managed via automated post-connection MAC blacklisting combined with isolated guest SSIDs.",
                                    fontSize = 11.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
