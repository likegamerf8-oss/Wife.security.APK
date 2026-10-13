package com.example.data.local

import com.example.data.local.entity.ApprovalRequestEntity
import com.example.data.local.entity.CompatibilityModelEntity
import com.example.data.local.entity.DeviceEntity
import com.example.data.local.entity.GuestPassEntity
import com.example.data.local.entity.IspCustomerEntity
import com.example.data.local.entity.IspOrganizationEntity
import com.example.data.local.entity.NetworkEntity
import com.example.data.local.entity.ParentalProfileEntity
import com.example.data.local.entity.RouterEntity
import com.example.data.local.entity.SecurityAuditFindingEntity
import com.example.data.local.entity.SecurityEventEntity
import com.example.data.local.entity.SecurityPolicyEntity
import com.example.data.local.entity.UserEntity

object DatabasePrepopulate {
    val initialUser = UserEntity(
        id = "user_bd_owner_1",
        email = "dkhdarman24221@gmail.com",
        displayName = "Arman Hossain (Admin)",
        role = "OWNER",
        isAuthenticated = true
    )

    val initialNetwork = NetworkEntity(
        id = "net_dhaka_home_1",
        ssid = "Dhaka_Secure_Mesh",
        bssid = "00:1A:2B:3C:4D:5E",
        securityType = "WPA2/WPA3 Personal",
        routerId = "router_openwrt_01",
        isOwnerVerified = true,
        ownershipConsentSigned = true,
        localSubnet = "192.168.1.0/24",
        gatewayIp = "192.168.1.1"
    )

    val initialRouter = RouterEntity(
        id = "router_openwrt_01",
        name = "OpenWrt Security Gateway (Home)",
        manufacturer = "OpenWrt Foundation",
        model = "OpenWrt x86_64 / Linksys WRT3200ACM",
        hardwareRevision = "Rev v2",
        firmwareVersion = "OpenWrt 23.05.3 r23809",
        connectionType = "OPENWRT_RPC",
        ipAddress = "192.168.1.1",
        port = 80,
        useHttps = false,
        username = "root",
        encryptedToken = "enc_session_token_verified",
        status = "CONNECTED",
        latencyMs = 8L,
        isPreAccessSupported = true,
        isMacFilterSupported = true,
        isGuestNetworkSupported = true,
        ispName = "Link3 Technologies Fiber",
        isActiveRouter = true
    )

    val initialSecondaryRouter = RouterEntity(
        id = "router_mikrotik_02",
        name = "Office MikroTik RouterBOARD",
        manufacturer = "MikroTik",
        model = "RouterBOARD hEX S (RB760iGS)",
        hardwareRevision = "r2",
        firmwareVersion = "RouterOS v7.14.3",
        connectionType = "MIKROTIK_REST",
        ipAddress = "192.168.88.1",
        port = 443,
        useHttps = true,
        username = "admin",
        encryptedToken = "enc_token_mtk",
        status = "CONNECTED",
        latencyMs = 14L,
        isPreAccessSupported = true,
        isMacFilterSupported = true,
        isGuestNetworkSupported = true,
        ispName = "Carnival Internet Fiber",
        isActiveRouter = false
    )

    val initialPolicy = SecurityPolicyEntity(
        id = "policy_net_01",
        networkId = "net_dhaka_home_1",
        mode = "DEFAULT_DENY_PRE_ACCESS",
        autoTrustApproved = true,
        guestAccessTimeoutMinutes = 240,
        alertOnUnknownDevices = true,
        notificationEnabled = true
    )

    val initialDevices = listOf(
        DeviceEntity(
            id = "dev_01",
            networkId = "net_dhaka_home_1",
            macAddress = "B4:CE:F6:12:34:56",
            ipAddress = "192.168.1.101",
            hostname = "Arman-Pixel8-Pro",
            vendor = "Google LLC",
            category = "PHONE",
            authorizationState = "AUTHORIZED",
            isRandomizedMac = false,
            isTrusted = true,
            notes = "Owner Primary Device"
        ),
        DeviceEntity(
            id = "dev_02",
            networkId = "net_dhaka_home_1",
            macAddress = "3C:06:30:78:90:AB",
            ipAddress = "192.168.1.102",
            hostname = "ThinkPad-T14-Work",
            vendor = "Lenovo Corporation",
            category = "LAPTOP",
            authorizationState = "AUTHORIZED",
            isRandomizedMac = false,
            isTrusted = true,
            notes = "Office Workstation"
        ),
        DeviceEntity(
            id = "dev_03",
            networkId = "net_dhaka_home_1",
            macAddress = "2A:B5:1C:DF:88:99",
            ipAddress = "192.168.1.155",
            hostname = "Galaxy-A54-Guest",
            vendor = "Samsung Electronics",
            category = "PHONE",
            authorizationState = "PENDING",
            isRandomizedMac = true,
            isTrusted = false,
            notes = "Joined via Guest Portal (Pending Pre-access approval)"
        ),
        DeviceEntity(
            id = "dev_04",
            networkId = "net_dhaka_home_1",
            macAddress = "58:D9:C3:FA:BC:DE",
            ipAddress = "192.168.1.200",
            hostname = "ESP32-Smart-Socket",
            vendor = "Espressif Systems",
            category = "IOT",
            authorizationState = "AUTHORIZED",
            isRandomizedMac = false,
            isTrusted = true,
            notes = "Living room smart plug"
        ),
        DeviceEntity(
            id = "dev_05",
            networkId = "net_dhaka_home_1",
            macAddress = "DE:AD:BE:EF:00:11",
            ipAddress = "192.168.1.250",
            hostname = "Unknown-Kali-Probe",
            vendor = "Unknown NIC (Randomized)",
            category = "UNKNOWN",
            authorizationState = "BLOCKED",
            isRandomizedMac = true,
            isTrusted = false,
            notes = "Denied by pre-access ACL policy"
        )
    )

    val initialApprovalRequests = listOf(
        ApprovalRequestEntity(
            id = "req_101",
            deviceId = "dev_03",
            networkId = "net_dhaka_home_1",
            macAddress = "2A:B5:1C:DF:88:99",
            hostname = "Galaxy-A54-Guest",
            ipAddress = "192.168.1.155",
            requestedTimestamp = System.currentTimeMillis() - 120000,
            expiresTimestamp = System.currentTimeMillis() + 13 * 60 * 1000,
            status = "PENDING",
            decisionReason = ""
        )
    )

    val initialSecurityEvents = listOf(
        SecurityEventEntity(
            id = "evt_01",
            networkId = "net_dhaka_home_1",
            type = "ROUTER_CONNECTED",
            title = "Router Verified & Connected",
            description = "OpenWrt 23.05.3 uBus API connected at 192.168.1.1 with verified pre-access MAC filter capability.",
            severity = "INFO"
        ),
        SecurityEventEntity(
            id = "evt_02",
            networkId = "net_dhaka_home_1",
            type = "APPROVAL_REQUESTED",
            title = "New Device Requesting Wi-Fi Access",
            description = "Device 'Galaxy-A54-Guest' (2A:B5:1C:DF:88:99) held outside protected network pending owner verification.",
            severity = "WARNING",
            relatedDeviceId = "dev_03"
        ),
        SecurityEventEntity(
            id = "evt_03",
            networkId = "net_dhaka_home_1",
            type = "ACCESS_REVOKED",
            title = "Unauthorized Device Blocked",
            description = "Device 'Unknown-Kali-Probe' (DE:AD:BE:EF:00:11) blocked by hardware access-list policy.",
            severity = "CRITICAL",
            relatedDeviceId = "dev_05"
        )
    )

    val initialGuestPasses = listOf(
        GuestPassEntity(
            id = "guest_01",
            networkId = "net_dhaka_home_1",
            guestName = "Tanvir Ahmed (Guest)",
            passCode = "BD-SAFE-8821",
            expiresAt = System.currentTimeMillis() + 3 * 3600 * 1000L,
            isActive = true
        )
    )

    val initialParentalProfiles = listOf(
        ParentalProfileEntity(
            id = "parent_01",
            routerId = "router_openwrt_01",
            profileName = "Kids Mobile & Tablets",
            assignedDeviceMacs = "2A:B5:1C:DF:88:99",
            bedTimeHour = 22,
            bedTimeMinute = 0,
            wakeUpHour = 6,
            isScheduleActive = true,
            isBlockedNow = false
        )
    )

    val initialSecurityAuditFindings = listOf(
        SecurityAuditFindingEntity(
            id = "find_01",
            routerId = "router_openwrt_01",
            title = "Wi-Fi Encryption Suite Verified (WPA2/WPA3-SAE)",
            severity = "INFORMATIONAL",
            evidence = "hostapd wireless configuration uses 'wpa=2' with CCMP pairwise cipher and PMF (802.11w) active.",
            explanation = "Strong AES-based encryption is active. Legacy TKIP and insecure WEP ciphers are disabled.",
            recommendedSolution = "Keep Protected Management Frames (PMF) enabled to prevent deauthentication attacks.",
            isRemediated = true,
            isAutoRemediationSupported = false
        ),
        SecurityAuditFindingEntity(
            id = "find_02",
            routerId = "router_openwrt_01",
            title = "WPS (Wi-Fi Protected Setup) PIN Vulnerability",
            severity = "MEDIUM",
            evidence = "WPS PIN is advertised on wlan0. PIN-based brute force (Pixie Dust) poses a risk on legacy Wi-Fi hardware.",
            explanation = "WPS PIN allows nearby devices to bypass WPA2 passphrase by brute-forcing an 8-digit PIN.",
            recommendedSolution = "Disable WPS PIN via router web console or tap Auto-Remediate to disable wps_pin in wireless config.",
            isRemediated = false,
            isAutoRemediationSupported = true
        ),
        SecurityAuditFindingEntity(
            id = "find_03",
            routerId = "router_openwrt_01",
            title = "WAN Remote Administration Port Closed",
            severity = "INFORMATIONAL",
            evidence = "Port 80 and 443 are dropped on external WAN interface (eth0.2). Management is strictly local-LAN bound.",
            explanation = "Router administrative console cannot be reached from the public internet.",
            recommendedSolution = "Maintain firewall drop rules on WAN input chain.",
            isRemediated = true,
            isAutoRemediationSupported = false
        ),
        SecurityAuditFindingEntity(
            id = "find_04",
            routerId = "router_openwrt_01",
            title = "Default Administrator Password Strength",
            severity = "LOW",
            evidence = "Current root password entropy meets local guidelines (14 characters with mixed symbols).",
            explanation = "Administrative password is not default 'admin' or 'password'.",
            recommendedSolution = "Rotate administrative password every 90 days.",
            isRemediated = true,
            isAutoRemediationSupported = false
        )
    )

    val initialIspOrganizations = listOf(
        IspOrganizationEntity(
            id = "isp_link3",
            name = "Link3 Technologies Ltd.",
            btrcLicenseNumber = "BTRC/ISP/NATIONWIDE/004",
            supportHotline = "16335",
            coverageAreas = "Dhaka, Chittagong, Sylhet, Rajshahi, Khulna",
            tr069AcsUrl = "https://acs.link3.net/tr069",
            supportedOntTypes = "Huawei HG8145V5, BDCOM GP1702, ZTE F670L",
            isActive = true
        ),
        IspOrganizationEntity(
            id = "isp_carnival",
            name = "Carnival Internet (Dotlines)",
            btrcLicenseNumber = "BTRC/ISP/NATIONWIDE/028",
            supportHotline = "09601555555",
            coverageAreas = "Dhaka, Gazipur, Narayanganj, Comilla",
            tr069AcsUrl = "https://acs.carnival.com.bd",
            supportedOntTypes = "MikroTik RouterBOARD, Huawei EG8145, VSOL",
            isActive = true
        ),
        IspOrganizationEntity(
            id = "isp_amber",
            name = "Amber IT Ltd.",
            btrcLicenseNumber = "BTRC/ISP/NATIONWIDE/012",
            supportHotline = "09612000888",
            coverageAreas = "Dhaka, Chittagong, Cox's Bazar",
            tr069AcsUrl = "https://acs.amberit.com.bd/cpe",
            supportedOntTypes = "Huawei HG8145V5, TP-Link Archer C6",
            isActive = true
        )
    )

    val initialIspCustomers = listOf(
        IspCustomerEntity(
            id = "cust_01",
            organizationId = "isp_link3",
            customerCode = "L3-DHK-88912",
            customerName = "Arman Hossain",
            phone = "+8801712345678",
            address = "House 14, Road 7, Dhanmondi, Dhaka",
            routerId = "router_openwrt_01",
            pppoeUsername = "arman.dhaka@link3",
            servicePackage = "Ultra Fiber 50 Mbps (BDIX 100 Mbps)",
            ontOpticalPowerDbm = -18.2f,
            isOnline = true
        )
    )

    val initialCompatibilityCatalog = listOf(
        CompatibilityModelEntity(
            id = "comp_openwrt_all",
            manufacturer = "OpenWrt Foundation",
            modelName = "OpenWrt (All Supported Routers)",
            deviceCategory = "ROUTER",
            hardwareRevisions = "x86, MediaTek, Qualcomm, Broadcom",
            firmwareRequirements = "OpenWrt 21.02 / 22.03 / 23.05+",
            integrationMethod = "uBus JSON-RPC / hostapd ACL",
            bangladeshIsps = "Compatible with all ISPs (Link3, Carnival, Amber IT, etc.)",
            supportLevel = "FULLY_VERIFIED",
            supportsPreAccess = true,
            supportsMacFilter = true,
            supportsGuestNetwork = true,
            officialDocUrl = "https://openwrt.org/docs/techref/ubus",
            lastVerifiedDate = "2026-03-15",
            knownLimitations = "Requires rpcd-mod-rpcsys and luci-mod-rpc packages installed."
        ),
        CompatibilityModelEntity(
            id = "comp_mikrotik_hex",
            manufacturer = "MikroTik",
            modelName = "RouterBOARD hEX / hAP ac2 / hAP ac3",
            deviceCategory = "ROUTER",
            hardwareRevisions = "RB750Gr3, RBD52G, RBD53iG",
            firmwareRequirements = "RouterOS v7.10+",
            integrationMethod = "RouterOS REST API / Wireless Access-List",
            bangladeshIsps = "Widely used by Link3, Carnival, Dot Internet",
            supportLevel = "FULLY_VERIFIED",
            supportsPreAccess = true,
            supportsMacFilter = true,
            supportsGuestNetwork = true,
            officialDocUrl = "https://help.mikrotik.com/docs/display/ROS/REST+API",
            lastVerifiedDate = "2026-02-28",
            knownLimitations = "REST API must be enabled via '/ip service set rest disabled=no'."
        ),
        CompatibilityModelEntity(
            id = "comp_huawei_hg8145v5",
            manufacturer = "Huawei",
            modelName = "EchoLife HG8145V5 / EG8145V5 GPON ONT",
            deviceCategory = "GPON_ONT",
            hardwareRevisions = "V5 Hardware, dual-band Wi-Fi",
            firmwareRequirements = "V5R019C00 / V5R020C10",
            integrationMethod = "Web Admin Session API / WLAN MAC Filter",
            bangladeshIsps = "Primary ONT provided by Link3, Amber IT, Carnival, ICC",
            supportLevel = "PARTIALLY_VERIFIED",
            supportsPreAccess = false,
            supportsMacFilter = true,
            supportsGuestNetwork = true,
            officialDocUrl = "https://support.huawei.com/enterprise/en/access-network/echolife-hg8145v5-pid-22921575",
            lastVerifiedDate = "2026-01-20",
            knownLimitations = "Pre-access approval not supported by hardware; supports post-connection MAC filtering and guest isolation."
        ),
        CompatibilityModelEntity(
            id = "comp_zte_f670l",
            manufacturer = "ZTE",
            modelName = "ZXHN F670L / F660 GPON ONT",
            deviceCategory = "GPON_ONT",
            hardwareRevisions = "F670L v9 / v10",
            firmwareRequirements = "V9.0.10P1T1 or ISP locked build",
            integrationMethod = "HTTP Management Gateway / Station Query",
            bangladeshIsps = "Provided by Dot Internet, Circle Network, BTCL",
            supportLevel = "PARTIALLY_VERIFIED",
            supportsPreAccess = false,
            supportsMacFilter = true,
            supportsGuestNetwork = false,
            officialDocUrl = "https://support.zte.com.cn",
            lastVerifiedDate = "2026-01-15",
            knownLimitations = "ISP SuperAdmin lock on some units prevents API-based MAC blacklisting without telecom user credentials."
        ),
        CompatibilityModelEntity(
            id = "comp_tplink_archer_c6",
            manufacturer = "TP-Link",
            modelName = "Archer C6 / A6 / AX10 / AX23",
            deviceCategory = "ROUTER",
            hardwareRevisions = "v2, v3, v4",
            firmwareRequirements = "Stock TP-Link Firmware (latest 2024-2026)",
            integrationMethod = "TP-Link TDP / HTTP Web Session",
            bangladeshIsps = "Popular retail router across Bangladesh",
            supportLevel = "PARTIALLY_VERIFIED",
            supportsPreAccess = false,
            supportsMacFilter = true,
            supportsGuestNetwork = true,
            officialDocUrl = "https://www.tp-link.com/bd/support/download/archer-c6/",
            lastVerifiedDate = "2026-02-10",
            knownLimitations = "AES encrypted session authentication required. Post-connection MAC block supported."
        ),
        CompatibilityModelEntity(
            id = "comp_tplink_omada",
            manufacturer = "TP-Link Omada",
            modelName = "Omada Controller / EAP610 / ER605",
            deviceCategory = "ACCESS_POINT",
            hardwareRevisions = "OC200, OC300, Software Controller",
            firmwareRequirements = "Omada Controller v5.9+",
            integrationMethod = "Omada OpenAPI / Dynamic Authorization",
            bangladeshIsps = "Used in BD offices, restaurants, colleges",
            supportLevel = "FULLY_VERIFIED",
            supportsPreAccess = true,
            supportsMacFilter = true,
            supportsGuestNetwork = true,
            officialDocUrl = "https://www.tp-link.com/us/support/faq/3285/",
            lastVerifiedDate = "2026-03-01",
            knownLimitations = "Requires Omada OpenAPI application ID and controller secret."
        ),
        CompatibilityModelEntity(
            id = "comp_netis_w1",
            manufacturer = "Netis",
            modelName = "Netis W1 / WF2409E",
            deviceCategory = "ROUTER",
            hardwareRevisions = "v1.0",
            firmwareRequirements = "Stock v2.4.x",
            integrationMethod = "Basic HTTP Auth / MAC Filter",
            bangladeshIsps = "Low-cost router widely used in rural & student housing",
            supportLevel = "RESEARCH_REQUIRED",
            supportsPreAccess = false,
            supportsMacFilter = true,
            supportsGuestNetwork = false,
            officialDocUrl = "https://www.netis-systems.com",
            lastVerifiedDate = "2025-11-12",
            knownLimitations = "Lacks standard REST API; requires automated web form submission."
        ),
        CompatibilityModelEntity(
            id = "comp_tenda_ac10",
            manufacturer = "Tenda",
            modelName = "Tenda AC10 / F3 / AC1200",
            deviceCategory = "ROUTER",
            hardwareRevisions = "v3, v4",
            firmwareRequirements = "V16.03.10.x",
            integrationMethod = "Tenda Cloud / Local CGI API",
            bangladeshIsps = "Budget retail router across Dhaka, Chittagong, Sylhet",
            supportLevel = "PARTIALLY_VERIFIED",
            supportsPreAccess = false,
            supportsMacFilter = true,
            supportsGuestNetwork = true,
            officialDocUrl = "https://www.tendacn.com/bd/",
            lastVerifiedDate = "2026-01-05",
            knownLimitations = "Blacklist enforcement causes 1-2 sec Wi-Fi interface restart."
        ),
        CompatibilityModelEntity(
            id = "comp_bdcom_gpon",
            manufacturer = "BDCOM",
            modelName = "BDCOM GP1702 / GP1704 GPON ONT",
            deviceCategory = "GPON_ONT",
            hardwareRevisions = "GP1704-2GV-W",
            firmwareRequirements = "BDCOM EPON/GPON firmware 2.0",
            integrationMethod = "CLI / Telnet / SNMP",
            bangladeshIsps = "Common local ISP fiber gateway in BD",
            supportLevel = "DEV_REQUIRED",
            supportsPreAccess = false,
            supportsMacFilter = true,
            supportsGuestNetwork = false,
            officialDocUrl = "https://www.bdcom.cn/en/",
            lastVerifiedDate = "2025-12-01",
            knownLimitations = "Requires integration development via SNMP OID or SSH CLI."
        ),
        CompatibilityModelEntity(
            id = "comp_asus_merlin",
            manufacturer = "ASUS",
            modelName = "RT-AX58U / RT-AC86U (Asuswrt-Merlin)",
            deviceCategory = "ROUTER",
            hardwareRevisions = "v1, v2",
            firmwareRequirements = "Asuswrt-Merlin 386.x+",
            integrationMethod = "SSH CLI / NVRAM macfilter table",
            bangladeshIsps = "Enthusiast gaming routers in Bangladesh",
            supportLevel = "FULLY_VERIFIED",
            supportsPreAccess = true,
            supportsMacFilter = true,
            supportsGuestNetwork = true,
            officialDocUrl = "https://www.asuswrt-merlin.net",
            lastVerifiedDate = "2026-02-15",
            knownLimitations = "SSH key authentication required."
        )
    )
}
