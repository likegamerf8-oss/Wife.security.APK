package com.example.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

data class DiscoveredRouterSignature(
    val gatewayIp: String,
    val port: Int,
    val useHttps: Boolean,
    val manufacturer: String,
    val modelGuess: String,
    val connectionType: String,
    val banner: String,
    val isSupported: Boolean,
    val isPreAccessSupported: Boolean,
    val supportedCapabilitiesSummary: String
)

object RouterProber {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(2, TimeUnit.SECONDS)
        .readTimeout(2, TimeUnit.SECONDS)
        .followRedirects(true)
        .build()

    suspend fun probeGateway(gatewayIp: String): DiscoveredRouterSignature = withContext(Dispatchers.IO) {
        // Try common HTTP port 80 first, then 443
        val probe80 = probePort(gatewayIp, 80, false)
        if (probe80 != null) return@withContext probe80

        val probe443 = probePort(gatewayIp, 443, true)
        if (probe443 != null) return@withContext probe443

        val probe8080 = probePort(gatewayIp, 8080, false)
        if (probe8080 != null) return@withContext probe8080

        // If gateway is unreachable on HTTP, provide an intelligent default based on Bangladesh subnet convention:
        when (gatewayIp) {
            "192.168.88.1" -> DiscoveredRouterSignature(
                gatewayIp = gatewayIp,
                port = 443,
                useHttps = true,
                manufacturer = "MikroTik",
                modelGuess = "RouterBOARD hEX / hAP (Default Subnet)",
                connectionType = "MIKROTIK_REST",
                banner = "Standard MikroTik 192.168.88.1 Subnet",
                isSupported = true,
                isPreAccessSupported = true,
                supportedCapabilitiesSummary = "Pre-Access wireless ACL, Station query, bandwidth throttling"
            )
            "192.168.100.1" -> DiscoveredRouterSignature(
                gatewayIp = gatewayIp,
                port = 80,
                useHttps = false,
                manufacturer = "Huawei",
                modelGuess = "EchoLife HG8145V5 GPON ONT (Link3/Amber IT)",
                connectionType = "HUAWEI_ONT",
                banner = "Standard GPON ONT Subnet 192.168.100.1",
                isSupported = true,
                isPreAccessSupported = false,
                supportedCapabilitiesSummary = "Post-connection WLAN MAC filtering, station telemetry"
            )
            "192.168.0.1" -> DiscoveredRouterSignature(
                gatewayIp = gatewayIp,
                port = 80,
                useHttps = false,
                manufacturer = "TP-Link",
                modelGuess = "Archer C6 / AX10 (Default Subnet)",
                connectionType = "TPLINK_HTTP",
                banner = "Standard TP-Link Subnet 192.168.0.1",
                isSupported = true,
                isPreAccessSupported = false,
                supportedCapabilitiesSummary = "Post-connection MAC blacklist, guest network toggle"
            )
            else -> DiscoveredRouterSignature(
                gatewayIp = gatewayIp,
                port = 80,
                useHttps = false,
                manufacturer = "OpenWrt / Generic Gateway",
                modelGuess = "OpenWrt or Compatible Router ($gatewayIp)",
                connectionType = "OPENWRT_RPC",
                banner = "Gateway at $gatewayIp",
                isSupported = true,
                isPreAccessSupported = true,
                supportedCapabilitiesSummary = "uBus JSON-RPC station telemetry and hostapd MAC filtering"
            )
        }
    }

    private fun probePort(gatewayIp: String, port: Int, useHttps: Boolean): DiscoveredRouterSignature? {
        val scheme = if (useHttps) "https" else "http"
        val url = "$scheme://$gatewayIp:$port/"

        return try {
            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "Mozilla/5.0 (Android; UniversalWifiSecurity)")
                .build()

            val response = httpClient.newCall(request).execute()
            val serverHeader = response.header("Server") ?: ""
            val authHeader = response.header("WWW-Authenticate") ?: ""
            val bodySnippet = try { response.body?.string()?.take(2048) ?: "" } catch (e: Exception) { "" }

            val combined = (serverHeader + " " + authHeader + " " + bodySnippet).lowercase()

            when {
                combined.contains("uhttpd") || combined.contains("luci") || combined.contains("openwrt") -> {
                    DiscoveredRouterSignature(
                        gatewayIp = gatewayIp,
                        port = port,
                        useHttps = useHttps,
                        manufacturer = "OpenWrt Foundation",
                        modelGuess = "OpenWrt Security Gateway",
                        connectionType = "OPENWRT_RPC",
                        banner = "uhttpd LuCI detected",
                        isSupported = true,
                        isPreAccessSupported = true,
                        supportedCapabilitiesSummary = "Pre-access hostapd MAC allowlist, uBus JSON-RPC"
                    )
                }
                combined.contains("routeros") || combined.contains("mikrotik") -> {
                    DiscoveredRouterSignature(
                        gatewayIp = gatewayIp,
                        port = port,
                        useHttps = useHttps,
                        manufacturer = "MikroTik",
                        modelGuess = "RouterBOARD / RouterOS v7",
                        connectionType = "MIKROTIK_REST",
                        banner = "MikroTik RouterOS detected",
                        isSupported = true,
                        isPreAccessSupported = true,
                        supportedCapabilitiesSummary = "REST API wireless access-list, pre-auth quarantine"
                    )
                }
                combined.contains("echolife") || combined.contains("hg8145") || combined.contains("eg8145") || combined.contains("huawei") -> {
                    DiscoveredRouterSignature(
                        gatewayIp = gatewayIp,
                        port = port,
                        useHttps = useHttps,
                        manufacturer = "Huawei",
                        modelGuess = "EchoLife HG8145V5 GPON ONT",
                        connectionType = "HUAWEI_ONT",
                        banner = "Huawei GPON Web Management detected",
                        isSupported = true,
                        isPreAccessSupported = false,
                        supportedCapabilitiesSummary = "Post-connection WLAN MAC filtering (Hardware lacks pre-access hold)"
                    )
                }
                combined.contains("tp-link") || combined.contains("archer") || combined.contains("wr") -> {
                    DiscoveredRouterSignature(
                        gatewayIp = gatewayIp,
                        port = port,
                        useHttps = useHttps,
                        manufacturer = "TP-Link",
                        modelGuess = "Archer Series Router",
                        connectionType = "TPLINK_HTTP",
                        banner = "TP-Link Management Gateway detected",
                        isSupported = true,
                        isPreAccessSupported = false,
                        supportedCapabilitiesSummary = "Post-connection MAC blacklist filtering"
                    )
                }
                combined.contains("zxhn") || combined.contains("f670") || combined.contains("zte") -> {
                    DiscoveredRouterSignature(
                        gatewayIp = gatewayIp,
                        port = port,
                        useHttps = useHttps,
                        manufacturer = "ZTE",
                        modelGuess = "ZXHN F670L GPON ONT",
                        connectionType = "HUAWEI_ONT", // Uses comparable ONT web session protocol
                        banner = "ZTE GPON ONT detected",
                        isSupported = true,
                        isPreAccessSupported = false,
                        supportedCapabilitiesSummary = "Post-connection station query & MAC filter"
                    )
                }
                else -> {
                    DiscoveredRouterSignature(
                        gatewayIp = gatewayIp,
                        port = port,
                        useHttps = useHttps,
                        manufacturer = if (serverHeader.isNotBlank()) serverHeader else "Generic Router",
                        modelGuess = "Web Management Interface ($gatewayIp)",
                        connectionType = "OPENWRT_RPC",
                        banner = if (serverHeader.isNotBlank()) "Server: $serverHeader" else "HTTP $port Active",
                        isSupported = true,
                        isPreAccessSupported = false,
                        supportedCapabilitiesSummary = "HTTP Gateway detected. Credentials required to link."
                    )
                }
            }
        } catch (e: Exception) {
            null
        }
    }
}
