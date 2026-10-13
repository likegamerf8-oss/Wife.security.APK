package com.example.network

import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import androidx.core.content.ContextCompat
import java.net.InetAddress
import java.nio.ByteBuffer
import java.nio.ByteOrder

data class WifiNetworkInfo(
    val isConnected: Boolean,
    val ssid: String,
    val bssid: String?,
    val gatewayIp: String,
    val localIp: String,
    val subnetMask: String,
    val frequencyMhz: Int,
    val linkSpeedMbps: Int,
    val requiresLocationPermission: Boolean,
    val detectionTimestamp: Long = System.currentTimeMillis()
)

object WifiNetworkDetector {

    fun detectCurrentWifi(context: Context): WifiNetworkInfo {
        return try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
                ?: return disconnectedState()

            val activeNetwork = connectivityManager.activeNetwork ?: return disconnectedState()
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return disconnectedState()

            if (!capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return disconnectedState()
            }

            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager

            var rawSsid = "<unknown ssid>"
            var bssid: String? = null
            var frequency = 0
            var linkSpeed = 0

            // Check location permission which Android requires for Wi-Fi SSID
            val hasLocationPerm = try {
                ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            } catch (e: Throwable) {
                false
            }

            // Extract WifiInfo safely
            val connectionInfo: WifiInfo? = try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    capabilities.transportInfo as? WifiInfo ?: wifiManager?.connectionInfo
                } else {
                    wifiManager?.connectionInfo
                }
            } catch (e: Throwable) {
                null
            }

            if (connectionInfo != null) {
                try {
                    rawSsid = connectionInfo.ssid ?: "<unknown ssid>"
                    bssid = connectionInfo.bssid
                    frequency = connectionInfo.frequency
                    linkSpeed = connectionInfo.linkSpeed
                } catch (e: Throwable) {
                    // Ignore reflection/permission exceptions
                }
            }

            // Clean SSID quotes if present (Android returns "\"MySSID\"")
            var cleanSsid = rawSsid.trim()
            if (cleanSsid.startsWith("\"") && cleanSsid.endsWith("\"") && cleanSsid.length >= 2) {
                cleanSsid = cleanSsid.substring(1, cleanSsid.length - 1)
            }

            val linkProperties = try {
                connectivityManager.getLinkProperties(activeNetwork)
            } catch (e: Throwable) {
                null
            }

            // Resolve Gateway IP
            var gatewayIp = "192.168.1.1"
            if (linkProperties != null) {
                try {
                    for (route in linkProperties.routes) {
                        if (route.isDefaultRoute && route.gateway != null) {
                            val gw = route.gateway?.hostAddress
                            if (!gw.isNullOrBlank() && gw != "0.0.0.0") {
                                gatewayIp = gw
                                break
                            }
                        }
                    }
                } catch (e: Throwable) {
                    // Ignore route inspection exceptions
                }
            }

            // Fallback to DhcpInfo gateway if linkProperties routes didn't find it
            if (gatewayIp == "192.168.1.1") {
                try {
                    val dhcpGw = wifiManager?.dhcpInfo?.gateway ?: 0
                    if (dhcpGw != 0) {
                        gatewayIp = intToIp(dhcpGw)
                    }
                } catch (e: Throwable) {
                    // Ignore DHCP info exceptions
                }
            }

            // Local IP
            var localIp = "192.168.1.100"
            if (linkProperties != null) {
                try {
                    for (linkAddress in linkProperties.linkAddresses) {
                        val addr = linkAddress.address
                        if (!addr.isLoopbackAddress && addr is java.net.Inet4Address) {
                            localIp = addr.hostAddress ?: localIp
                            break
                        }
                    }
                } catch (e: Throwable) {
                    // Ignore link address exceptions
                }
            }

            if (cleanSsid == "<unknown ssid>" || cleanSsid.isBlank()) {
                cleanSsid = if (!hasLocationPerm) {
                    "Connected Wi-Fi (Gateway: $gatewayIp)"
                } else {
                    "Local Wi-Fi Network ($gatewayIp)"
                }
            }

            WifiNetworkInfo(
                isConnected = true,
                ssid = cleanSsid,
                bssid = bssid,
                gatewayIp = gatewayIp,
                localIp = localIp,
                subnetMask = "255.255.255.0",
                frequencyMhz = frequency,
                linkSpeedMbps = linkSpeed,
                requiresLocationPermission = !hasLocationPerm && rawSsid == "<unknown ssid>"
            )
        } catch (t: Throwable) {
            t.printStackTrace()
            disconnectedState()
        }
    }

    private fun disconnectedState(): WifiNetworkInfo {
        return WifiNetworkInfo(
            isConnected = false,
            ssid = "Not Connected",
            bssid = null,
            gatewayIp = "0.0.0.0",
            localIp = "0.0.0.0",
            subnetMask = "0.0.0.0",
            frequencyMhz = 0,
            linkSpeedMbps = 0,
            requiresLocationPermission = false
        )
    }

    private fun intToIp(ipInt: Int): String {
        return try {
            val bytes = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(ipInt).array()
            InetAddress.getByAddress(bytes).hostAddress ?: "192.168.1.1"
        } catch (e: Exception) {
            "192.168.1.1"
        }
    }
}
