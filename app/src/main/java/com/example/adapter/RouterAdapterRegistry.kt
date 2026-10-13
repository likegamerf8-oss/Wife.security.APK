package com.example.adapter

import com.example.data.local.entity.RouterEntity

object RouterAdapterRegistry {

    fun getAdapterForRouter(router: RouterEntity): RouterAdapter {
        return when (router.connectionType) {
            "OPENWRT_RPC" -> OpenWrtRpcAdapter(
                host = router.ipAddress,
                port = router.port,
                username = router.username,
                tokenOrPass = router.encryptedToken,
                useHttps = router.useHttps
            )
            "MIKROTIK_REST" -> MikroTikRestAdapter(
                host = router.ipAddress,
                port = router.port,
                username = router.username,
                passwordOrToken = router.encryptedToken,
                useHttps = router.useHttps
            )
            "HUAWEI_ONT" -> HuaweiOntAdapter(
                host = router.ipAddress,
                port = router.port,
                username = router.username,
                passwordOrToken = router.encryptedToken
            )
            "TPLINK_HTTP" -> TpLinkHttpAdapter(
                host = router.ipAddress,
                port = router.port,
                username = router.username,
                passwordOrToken = router.encryptedToken
            )
            "TR069_ACS" -> Tr069AcsAdapter()
            else -> TestDoubleSimulatorAdapter()
        }
    }

    fun getAllAvailableAdapters(): List<RouterAdapter> {
        return listOf(
            OpenWrtRpcAdapter(),
            MikroTikRestAdapter(),
            HuaweiOntAdapter(),
            TpLinkHttpAdapter(),
            Tr069AcsAdapter(),
            TestDoubleSimulatorAdapter()
        )
    }
}
