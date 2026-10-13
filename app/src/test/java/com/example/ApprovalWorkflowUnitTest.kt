package com.example

import com.example.data.local.entity.ApprovalRequestEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ApprovalWorkflowUnitTest {

    @Test
    fun `approval request expiration validation`() {
        val now = System.currentTimeMillis()
        val activeRequest = ApprovalRequestEntity(
            id = "req_1",
            deviceId = "dev_1",
            networkId = "net_1",
            macAddress = "B4:CE:F6:12:34:56",
            hostname = "Test-Device",
            ipAddress = "192.168.1.150",
            requestedTimestamp = now - 60000,
            expiresTimestamp = now + 600000,
            status = "PENDING"
        )

        assertFalse(now > activeRequest.expiresTimestamp)
        assertEquals("PENDING", activeRequest.status)

        val expiredRequest = activeRequest.copy(
            expiresTimestamp = now - 1000
        )
        assertTrue(now > expiredRequest.expiresTimestamp)
    }

    @Test
    fun `mac address normalization and randomization check`() {
        val normalMac = "B4:CE:F6:12:34:56"
        val firstByte = normalMac.substring(0, 2).toInt(16)
        // Bit 1 (0x02) of the most significant byte is the locally administered (randomized) bit
        val isLocallyAdministered = (firstByte and 0x02) != 0
        assertFalse(isLocallyAdministered)

        val randomMac = "2A:B5:1C:DF:88:99"
        val randomFirstByte = randomMac.substring(0, 2).toInt(16)
        val isRandom = (randomFirstByte and 0x02) != 0
        assertTrue(isRandom)
    }
}
