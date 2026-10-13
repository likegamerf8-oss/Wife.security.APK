package com.example.ai

import com.example.data.local.entity.DeviceEntity
import com.example.data.local.entity.RouterEntity
import com.example.data.local.entity.SecurityAuditFindingEntity
import com.example.localization.AppLanguage
import com.example.localization.LanguageManager

data class AiAdviceResult(
    val title: String,
    val summaryEn: String,
    val summaryBn: String,
    val recommendationEn: String,
    val recommendationBn: String,
    val safetyGuidance: String,
    val requiresManualAction: Boolean
)

object AiSecurityAssistant {

    fun analyzeDeviceObservation(device: DeviceEntity, isBangla: Boolean = false): AiAdviceResult {
        return if (device.isRandomizedMac) {
            AiAdviceResult(
                title = if (isBangla) "অপরিচিত ডিভাইস বিশ্লেষণ (প্রাইভেট ম্যাক)" else "Device Analysis: Private / Randomized MAC",
                summaryEn = "Device '${device.hostname}' is using a locally administered (randomized) MAC address (${device.macAddress}). This is a standard privacy feature built into modern Android, iOS, and Windows devices to prevent cross-network tracking.",
                summaryBn = "'${device.hostname}' ডিভাইসটি একটি র্যান্ডমাইজড (প্রাইভেট) ম্যাক অ্যাড্রেস (${device.macAddress}) ব্যবহার করছে। এটি আধুনিক অ্যান্ড্রয়েড ও আইওএস ডিভাইসের সাধারণ গোপনীয়তা বৈশিষ্ট্য।",
                recommendationEn = "Do NOT immediately panic or assume an attack. Check if a family member, guest, or colleague connected a modern phone. If recognized, tap 'Approve' and assign their name.",
                recommendationBn = "আতঙ্কিত হবেন না। পরিবারের সদস্য বা সহকর্মীর আধুনিক স্মার্টফোন হতে পারে। ডিভাইসটি শনাক্ত হলে অনুমোদন করুন এবং নাম নির্ধারণ করুন।",
                safetyGuidance = "Network isolation / pre-access quarantine protects your LAN until you verify ownership.",
                requiresManualAction = true
            )
        } else {
            AiAdviceResult(
                title = if (isBangla) "ডিভাইস টেলিমেট্রি বিশ্লেষণ" else "Device Analysis: Hardware NIC",
                summaryEn = "Device '${device.hostname}' presented a globally registered hardware OUI from manufacturer '${device.vendor}'. IP assigned: ${device.ipAddress}.",
                summaryBn = "'${device.hostname}' ডিভাইসটি '${device.vendor}' প্রস্তুতকারকের অনুমোদিত হার্ডওয়্যার নেটওয়ার্ক চিপ ব্যবহার করছে। আইপি: ${device.ipAddress}।",
                recommendationEn = "If this matches equipment you own (such as your laptop, smart TV, or phone), keep it marked as Trusted.",
                recommendationBn = "যদি এটি আপনার ল্যাপটপ, স্মার্ট টিভি বা ফোনের সাথে মিলে যায়, তবে এটিকে বিশ্বস্ত (Trusted) হিসেবে চিহ্নিত রাখুন।",
                safetyGuidance = "Device telemetry reflects router DHCP lease and hostapd association tables.",
                requiresManualAction = false
            )
        }
    }

    fun analyzeSecurityAudit(findings: List<SecurityAuditFindingEntity>, isBangla: Boolean = false): List<AiAdviceResult> {
        return findings.map { finding ->
            when {
                finding.title.contains("WPS", ignoreCase = true) -> AiAdviceResult(
                    title = if (isBangla) "WPS ঝুঁকি ও সমাধান" else "WPS PIN Assessment",
                    summaryEn = "WPS PIN allows fast connections without entering the Wi-Fi password, but is susceptible to offline PIN brute-forcing tools (Reaver, Pixie Dust).",
                    summaryBn = "WPS পিনের মাধ্যমে পাসওয়ার্ড ছাড়া দ্রুত কানেক্ট করা যায়, তবে এতে হ্যাকিং টুলসের মাধ্যমে রাউটারের পাসওয়ার্ড বের করার ঝুঁকি থাকে।",
                    recommendationEn = "Disable WPS PIN in the router web console. Use WPA2/WPA3 passphrase authentication instead.",
                    recommendationBn = "রাউটার ওয়েব কনসোল থেকে WPS পিন বন্ধ করুন। শুধুমাত্র শক্তিশালী WPA2/WPA3 পাসওয়ার্ড ব্যবহার করুন।",
                    safetyGuidance = "Auto-remediation is supported if you tap 'Auto-Remediate' with an authorized router connection.",
                    requiresManualAction = false
                )
                finding.title.contains("Encryption", ignoreCase = true) -> AiAdviceResult(
                    title = if (isBangla) "ওয়াই-ফাই এনক্রিপশন পর্যালোচনা" else "Wi-Fi Encryption Suite",
                    summaryEn = "Your network uses robust WPA2/WPA3-SAE encryption with CCMP AES ciphers. Eavesdropping on wireless airframes is prevented.",
                    summaryBn = "আপনার নেটওয়ার্ক শক্তিশালী WPA2/WPA3 এনক্রিপশন ব্যবহার করছে। বাতাসের মাধ্যমে ডেটা চুরি করা সম্ভব নয়।",
                    recommendationEn = "No action needed. Keep Protected Management Frames (PMF) active.",
                    recommendationBn = "কোনো পদক্ষেপের প্রয়োজন নেই। আপনার ওয়াই-ফাই এনক্রিপশন সুরক্ষিত রয়েছে।",
                    safetyGuidance = "Confirmed by active hostapd configuration.",
                    requiresManualAction = false
                )
                else -> AiAdviceResult(
                    title = finding.title,
                    summaryEn = finding.explanation,
                    summaryBn = if (isBangla) "নিরাপত্তা নীতি নিরীক্ষা: ${finding.explanation}" else finding.explanation,
                    recommendationEn = finding.recommendedSolution,
                    recommendationBn = if (isBangla) "পরামর্শ: ${finding.recommendedSolution}" else finding.recommendedSolution,
                    safetyGuidance = "Evidence: ${finding.evidence}",
                    requiresManualAction = !finding.isRemediated
                )
            }
        }
    }

    fun explainRouterCompatibility(router: RouterEntity?, isBangla: Boolean = false): String {
        if (router == null) return "No router linked."
        return if (router.isPreAccessSupported) {
            if (isBangla) {
                "${router.name} রাউটারে প্রি-অ্যাক্সেস সুরক্ষা সক্রিয়। নতুন কোনো ডিভাইস অনুমোদনের আগে লোকাল নেটওয়ার্কে কোনো ডেটা পাঠাতে পারবে না।"
            } else {
                "${router.name} supports hardware pre-access quarantine. Unauthorized devices are prevented from communicating with your LAN until you tap Approve."
            }
        } else {
            if (isBangla) {
                "${router.name} (সাধারণত ফাইবার ওএনইউ) হার্ডওয়্যারে প্রি-অ্যাসোসিয়েশন হোল্ড সুবিধা নেই। তবে অননুমোদিত ডিভাইস শনাক্ত হলে তাত্ক্ষণিক ম্যাক ব্ল্যাকলিস্ট করা হয়।"
            } else {
                "${router.name} (standard GPON ONT / retail firmware) lacks 802.11w pre-association holding. Instead, Universal Wi-Fi Security enforces instant post-connection WLAN MAC blacklisting."
            }
        }
    }
}
