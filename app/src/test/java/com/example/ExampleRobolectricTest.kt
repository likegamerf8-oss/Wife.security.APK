package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.adapter.CapabilityStatus
import com.example.adapter.OpenWrtRpcAdapter
import com.example.adapter.TestDoubleSimulatorAdapter
import com.example.network.RouterProber
import com.example.network.WifiNetworkDetector
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context matches application name`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Universal Wi-Fi Security", appName)
    }

    @Test
    fun `test double adapter approves and denies devices correctly`() = runBlocking {
        val adapter = TestDoubleSimulatorAdapter()
        assertTrue(adapter.isTestDouble)

        val initialDevices = adapter.listConnectedDevices().getOrThrow()
        assertTrue(initialDevices.isNotEmpty())

        val testMac = "2A:B5:1C:DF:88:99"
        val approveResult = adapter.approveDevice(testMac, "192.168.1.155")
        assertTrue(approveResult.isSuccess)

        val updatedDevices = adapter.listConnectedDevices().getOrThrow()
        val approvedStation = updatedDevices.find { it.macAddress == testMac }
        assertTrue(approvedStation?.isAuthorized == true)

        val denyResult = adapter.denyDevice(testMac)
        assertTrue(denyResult.isSuccess)

        val revokedDevices = adapter.listConnectedDevices().getOrThrow()
        val deniedStation = revokedDevices.find { it.macAddress == testMac }
        assertFalse(deniedStation?.isAuthorized == true)
    }

    @Test
    fun `openwrt adapter reports verified pre access capability`() = runBlocking {
        val adapter = OpenWrtRpcAdapter(host = "192.168.1.1")
        val capabilities = adapter.discoverCapabilities()

        assertEquals(CapabilityStatus.SUPPORTED, capabilities.readRouterIdentity)
        assertEquals(CapabilityStatus.SUPPORTED, capabilities.readConnectedDevices)
        assertEquals(CapabilityStatus.SUPPORTED, capabilities.preAccessApproval)
        assertEquals(CapabilityStatus.SUPPORTED, capabilities.postAccessRevocation)
    }

    @Test
    fun `wifi network detector returns valid network info structure`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val wifiInfo = WifiNetworkDetector.detectCurrentWifi(context)
        assertNotNull(wifiInfo)
        assertNotNull(wifiInfo.gatewayIp)
        assertNotNull(wifiInfo.ssid)
    }

    @Test
    fun `router prober identifies default gateways intelligently`() = runBlocking {
        val mikrotikSig = RouterProber.probeGateway("192.168.88.1")
        assertEquals("MikroTik", mikrotikSig.manufacturer)
        assertEquals("MIKROTIK_REST", mikrotikSig.connectionType)
        assertTrue(mikrotikSig.isPreAccessSupported)

        val huaweiSig = RouterProber.probeGateway("192.168.100.1")
        assertEquals("Huawei", huaweiSig.manufacturer)
        assertEquals("HUAWEI_ONT", huaweiSig.connectionType)
        assertFalse(huaweiSig.isPreAccessSupported) // Accurately reflects hardware limitation

        val tplinkSig = RouterProber.probeGateway("192.168.0.1")
        assertEquals("TP-Link", tplinkSig.manufacturer)
        assertEquals("TPLINK_HTTP", tplinkSig.connectionType)
    }

    @Test
    fun `main activity launches successfully without crashing`() {
        val controller = org.robolectric.Robolectric.buildActivity(MainActivity::class.java)
        controller.setup()
        assertNotNull(controller.get())
    }
}
