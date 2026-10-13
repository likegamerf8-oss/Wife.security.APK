package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.adapter.ConnectionTestResult
import com.example.adapter.IntegrationHealth
import com.example.data.local.AppDatabase
import com.example.data.local.entity.ApprovalRequestEntity
import com.example.data.local.entity.CompatibilityModelEntity
import com.example.data.local.entity.CompatibilityRequestEntity
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
import com.example.data.repository.WifiSecurityRepository
import com.example.localization.AppLanguage
import com.example.localization.LanguageManager
import com.example.network.AutoLinkStatus
import com.example.network.AutoRouterLinker
import com.example.network.DiscoveredRouterSignature
import com.example.network.SubnetDeviceScanner
import com.example.network.WifiNetworkDetector
import com.example.network.WifiNetworkInfo
import com.example.notification.SecurityNotificationManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val repository = WifiSecurityRepository(database)
    private val autoRouterLinker = AutoRouterLinker(application, repository)

    val currentUser: StateFlow<UserEntity?> = repository.currentUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val activeNetwork: StateFlow<NetworkEntity?> = repository.activeNetwork
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val activeRouter: StateFlow<RouterEntity?> = repository.activeRouter
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allDevices: StateFlow<List<DeviceEntity>> = repository.allDevices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pendingRequests: StateFlow<List<ApprovalRequestEntity>> = repository.pendingApprovalRequests
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allRequests: StateFlow<List<ApprovalRequestEntity>> = repository.allApprovalRequests
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val securityEvents: StateFlow<List<SecurityEventEntity>> = repository.securityEvents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val compatibilityModels: StateFlow<List<CompatibilityModelEntity>> = repository.compatibilityModels
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val securityPolicy: StateFlow<SecurityPolicyEntity?> = repository.securityPolicy
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val guestPasses: StateFlow<List<GuestPassEntity>> = repository.guestPasses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val compatibilityRequests: StateFlow<List<CompatibilityRequestEntity>> = repository.compatibilityRequests
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allRouters: StateFlow<List<RouterEntity>> = repository.allRouters
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val ispOrganizations: StateFlow<List<IspOrganizationEntity>> = repository.ispOrganizations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val ispCustomers: StateFlow<List<IspCustomerEntity>> = repository.allIspCustomers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val parentalProfiles: StateFlow<List<ParentalProfileEntity>> = repository.allParentalProfiles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val auditFindings: StateFlow<List<SecurityAuditFindingEntity>> = repository.allAuditFindings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedDevice = MutableStateFlow<DeviceEntity?>(null)
    val selectedDevice: StateFlow<DeviceEntity?> = _selectedDevice.asStateFlow()

    private val _lastDiagnosticHealth = MutableStateFlow<IntegrationHealth?>(null)
    val lastDiagnosticHealth: StateFlow<IntegrationHealth?> = _lastDiagnosticHealth.asStateFlow()

    private val _connectionTestState = MutableStateFlow<ConnectionTestResult?>(null)
    val connectionTestState: StateFlow<ConnectionTestResult?> = _connectionTestState.asStateFlow()

    private val _isConnecting = MutableStateFlow(false)
    val isConnecting: StateFlow<Boolean> = _isConnecting.asStateFlow()

    private val _darkThemeEnabled = MutableStateFlow(true)
    val darkThemeEnabled: StateFlow<Boolean> = _darkThemeEnabled.asStateFlow()

    private val _isScanningDevices = MutableStateFlow(false)
    val isScanningDevices: StateFlow<Boolean> = _isScanningDevices.asStateFlow()

    // Automatic Wi-Fi detection and router linking states
    private val _wifiInfo = MutableStateFlow(WifiNetworkDetector.detectCurrentWifi(application))
    val wifiInfo: StateFlow<WifiNetworkInfo> = _wifiInfo.asStateFlow()

    private val _autoLinkStatus = MutableStateFlow<AutoLinkStatus>(AutoLinkStatus.Idle)
    val autoLinkStatus: StateFlow<AutoLinkStatus> = _autoLinkStatus.asStateFlow()

    private val _authDialogSignature = MutableStateFlow<DiscoveredRouterSignature?>(null)
    val authDialogSignature: StateFlow<DiscoveredRouterSignature?> = _authDialogSignature.asStateFlow()

    init {
        SecurityNotificationManager.initNotificationChannels(application)
        triggerAutoDetectionAndLinking()
    }

    fun triggerAutoDetectionAndLinking() {
        viewModelScope.launch {
            try {
                _wifiInfo.value = WifiNetworkDetector.detectCurrentWifi(getApplication())
                if (!_wifiInfo.value.isConnected) {
                    _autoLinkStatus.value = AutoLinkStatus.WifiNotConnected
                    return@launch
                }

                _autoLinkStatus.value = AutoLinkStatus.Detecting("Discovering Gateway at ${_wifiInfo.value.gatewayIp}...")
                val status = autoRouterLinker.executeAutoDetectionAndLinking()
                _autoLinkStatus.value = status

                if (status is AutoLinkStatus.AuthRequired) {
                    _authDialogSignature.value = status.signature
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                _autoLinkStatus.value = AutoLinkStatus.Error(e.message ?: "Failed to probe network")
            }
        }
    }

    fun dismissAuthDialog() {
        _authDialogSignature.value = null
    }

    fun authorizeDiscoveredRouter(signature: DiscoveredRouterSignature, username: String, passOrToken: String) {
        viewModelScope.launch {
            _isConnecting.value = true
            val currentWifi = _wifiInfo.value
            val result = autoRouterLinker.authorizeAndLink(signature, currentWifi, username, passOrToken)
            _isConnecting.value = false
            if (result.isSuccess) {
                val linkedRouter = result.getOrThrow()
                _authDialogSignature.value = null
                _autoLinkStatus.value = AutoLinkStatus.Linked(
                    router = linkedRouter,
                    isLimited = !linkedRouter.isPreAccessSupported,
                    message = "Router ${linkedRouter.name} successfully authorized and linked."
                )
            } else {
                _autoLinkStatus.value = AutoLinkStatus.Error(
                    result.exceptionOrNull()?.message ?: "Authorization failed"
                )
            }
        }
    }

    fun selectDevice(device: DeviceEntity?) {
        _selectedDevice.value = device
    }

    fun toggleTheme() {
        _darkThemeEnabled.value = !_darkThemeEnabled.value
    }

    fun setLanguage(language: AppLanguage) {
        LanguageManager.currentLanguage = language
    }

    fun login(email: String, name: String, role: String = "OWNER") {
        viewModelScope.launch {
            repository.login(email, name, role)
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun confirmOwnershipConsent() {
        viewModelScope.launch {
            val net = activeNetwork.value ?: return@launch
            repository.saveNetworkOwnershipConsent(net)
        }
    }

    fun approveRequest(requestId: String) {
        viewModelScope.launch {
            repository.approvePendingRequest(requestId)
            SecurityNotificationManager.cancelNotification(getApplication(), requestId)
        }
    }

    fun rejectRequest(requestId: String) {
        viewModelScope.launch {
            repository.rejectPendingRequest(requestId)
            SecurityNotificationManager.cancelNotification(getApplication(), requestId)
        }
    }

    fun revokeDevice(deviceId: String) {
        viewModelScope.launch {
            repository.revokeDeviceAccess(deviceId)
        }
    }

    fun unblockDevice(deviceId: Any) {
        viewModelScope.launch {
            repository.unblockDeviceAccess(deviceId.toString())
        }
    }

    fun disconnectActiveRouter() {
        viewModelScope.launch {
            repository.disconnectActiveRouter()
        }
    }

    fun updateDevice(device: DeviceEntity) {
        viewModelScope.launch {
            repository.updateDeviceDetails(device)
            if (_selectedDevice.value?.id == device.id) {
                _selectedDevice.value = device
            }
        }
    }

    fun updatePolicy(policy: SecurityPolicyEntity) {
        viewModelScope.launch {
            repository.updateSecurityPolicy(policy)
        }
    }

    fun createGuestPass(guestName: String, durationHours: Int) {
        viewModelScope.launch {
            repository.createGuestPass(guestName, durationHours)
        }
    }

    fun revokeGuestPass(passId: String) {
        viewModelScope.launch {
            repository.revokeGuestPass(passId)
        }
    }

    fun runDiagnostics() {
        viewModelScope.launch {
            val health = repository.runRouterDiagnostics()
            _lastDiagnosticHealth.value = health
        }
    }

    fun connectNewRouter(router: RouterEntity) {
        viewModelScope.launch {
            _isConnecting.value = true
            val result = repository.connectRouter(router)
            _connectionTestState.value = result.getOrNull()
            _isConnecting.value = false
            triggerAutoDetectionAndLinking()
        }
    }

    fun submitCompatibilityRequest(manufacturer: String, model: String, ispName: String, email: String, notes: String) {
        viewModelScope.launch {
            val req = CompatibilityRequestEntity(
                id = "req_comp_" + System.currentTimeMillis(),
                manufacturer = manufacturer,
                model = model,
                ispName = ispName,
                userEmail = email,
                notes = notes
            )
            repository.submitCompatibilityRequest(req)
        }
    }

    fun addCompatibilityModel(model: CompatibilityModelEntity) {
        viewModelScope.launch {
            repository.addCompatibilityModel(model)
        }
    }

    fun triggerSimulatedPendingNotification() {
        SecurityNotificationManager.showApprovalNotification(
            context = getApplication(),
            requestId = "req_101",
            hostname = "Galaxy-A54-Guest",
            macAddress = "2A:B5:1C:DF:88:99",
            ipAddress = "192.168.1.155"
        )
    }

    fun switchActiveRouter(routerId: String) {
        viewModelScope.launch {
            repository.switchActiveRouter(routerId)
        }
    }

    fun removeRouter(routerId: String) {
        viewModelScope.launch {
            repository.removeRouter(routerId)
        }
    }

    fun saveParentalProfile(profile: ParentalProfileEntity) {
        viewModelScope.launch {
            repository.saveParentalProfile(profile)
        }
    }

    fun deleteParentalProfile(id: String) {
        viewModelScope.launch {
            repository.deleteParentalProfile(id)
        }
    }

    fun remediateSecurityFinding(findingId: String) {
        viewModelScope.launch {
            repository.remediateSecurityFinding(findingId)
        }
    }

    fun scanSubnetDevices() {
        viewModelScope.launch {
            _isScanningDevices.value = true
            try {
                val currentWifi = _wifiInfo.value
                val networkId = activeNetwork.value?.id ?: "net_default"
                val liveDevices = SubnetDeviceScanner.scanLocalSubnet(
                    context = getApplication(),
                    localIp = currentWifi.localIp,
                    networkId = networkId
                )
                if (liveDevices.isNotEmpty()) {
                    repository.updateLiveDevices(liveDevices)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isScanningDevices.value = false
            }
        }
    }
}
