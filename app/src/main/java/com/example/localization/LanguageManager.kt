package com.example.localization

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class AppLanguage {
    ENGLISH,
    BANGLA
}

object LanguageManager {
    var currentLanguage by mutableStateOf(AppLanguage.ENGLISH)

    fun getString(key: String): String {
        val isBn = currentLanguage == AppLanguage.BANGLA
        return when (key) {
            "app_title" -> if (isBn) "ইউনিভার্সাল ওয়াই-ফাই সিকিউরিটি" else "Universal Wi-Fi Security"
            "tagline" -> if (isBn) "আপনার ওয়াই-ফাই। আপনার অনুমতি। আপনার নিয়ন্ত্রণ।" else "Your Wi-Fi. Your permission. Your control."
            "dashboard" -> if (isBn) "ড্যাশবোর্ড" else "Dashboard"
            "devices" -> if (isBn) "সংযুক্ত ডিভাইস" else "Devices"
            "pending" -> if (isBn) "অনুমোদনের অপেক্ষা" else "Pending"
            "compatibility" -> if (isBn) "রাউটার সামঞ্জস্য" else "Compatibility"
            "settings" -> if (isBn) "সেটিংস" else "Settings"
            "history" -> if (isBn) "নিরাপত্তা ইতিহাস" else "Activity Log"
            "guest_access" -> if (isBn) "গেস্ট নেটওয়ার্ক" else "Guest Access"
            "diagnostics" -> if (isBn) "ডায়াগনস্টিকস" else "Diagnostics"
            "security_policy" -> if (isBn) "নিরাপত্তা নীতিমালা" else "Security Policy"
            "admin_panel" -> if (isBn) "এডমিন ম্যানেজমেন্ট" else "Admin Panel"
            "help_support" -> if (isBn) "সাহায্য ও সমাধান" else "Help & Support"
            "connect_router" -> if (isBn) "রাউটার সংযোগ করুন" else "Connect Router"
            "status_protected" -> if (isBn) "সুরক্ষিত (যাচাইকৃত)" else "Protected (Verified)"
            "status_limited" -> if (isBn) "সীমিত ক্ষমতা সম্পন্ন" else "Limited Capabilities"
            "status_demo" -> if (isBn) "টেস্ট ডাবল / সিমুলেটর মোড" else "Test Double / Sandbox"
            "approve" -> if (isBn) "অনুমোদন করুন" else "Approve"
            "reject" -> if (isBn) "প্রত্যাখ্যান করুন" else "Reject"
            "revoke" -> if (isBn) "অ্যাক্সেস বাতিল" else "Revoke"
            "block" -> if (isBn) "ব্লক করুন" else "Block"
            "authorized" -> if (isBn) "অনুমোদিত" else "Authorized"
            "unknown" -> if (isBn) "অপরিচিত" else "Unknown"
            "bangladesh_target" -> if (isBn) "বাংলাদেশ ইন্টারনেট সার্ভিস প্রোভাইডার ও রাউটার প্ল্যাটফর্ম" else "Bangladesh ISP & Router Security Platform"
            else -> key
        }
    }
}
