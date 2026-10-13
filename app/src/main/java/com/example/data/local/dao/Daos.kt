package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users LIMIT 1")
    fun getCurrentUser(): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("DELETE FROM users")
    suspend fun clearUser()
}

@Dao
interface NetworkDao {
    @Query("SELECT * FROM networks LIMIT 1")
    fun getActiveNetwork(): Flow<NetworkEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNetwork(network: NetworkEntity)

    @Update
    suspend fun updateNetwork(network: NetworkEntity)
}

@Dao
interface RouterDao {
    @Query("SELECT * FROM routers WHERE isActiveRouter = 1 LIMIT 1")
    fun getActiveRouter(): Flow<RouterEntity?>

    @Query("SELECT * FROM routers ORDER BY name ASC")
    fun getAllRouters(): Flow<List<RouterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRouter(router: RouterEntity)

    @Update
    suspend fun updateRouter(router: RouterEntity)

    @Query("UPDATE routers SET isActiveRouter = CASE WHEN id = :routerId THEN 1 ELSE 0 END")
    suspend fun switchActiveRouter(routerId: String)

    @Query("DELETE FROM routers WHERE id = :routerId")
    suspend fun deleteRouter(routerId: String)
}

@Dao
interface DeviceDao {
    @Query("SELECT * FROM devices ORDER BY lastObservedTimestamp DESC")
    fun getAllDevices(): Flow<List<DeviceEntity>>

    @Query("SELECT * FROM devices WHERE id = :id")
    fun getDeviceById(id: String): Flow<DeviceEntity?>

    @Query("SELECT * FROM devices WHERE authorizationState = :state ORDER BY lastObservedTimestamp DESC")
    fun getDevicesByState(state: String): Flow<List<DeviceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevices(devices: List<DeviceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevice(device: DeviceEntity)

    @Update
    suspend fun updateDevice(device: DeviceEntity)

    @Query("UPDATE devices SET authorizationState = :state WHERE id = :deviceId")
    suspend fun updateDeviceState(deviceId: String, state: String)

    @Query("DELETE FROM devices WHERE id = :deviceId")
    suspend fun deleteDevice(deviceId: String)
}

@Dao
interface ApprovalRequestDao {
    @Query("SELECT * FROM approval_requests WHERE status = 'PENDING' ORDER BY requestedTimestamp DESC")
    fun getPendingRequests(): Flow<List<ApprovalRequestEntity>>

    @Query("SELECT * FROM approval_requests ORDER BY requestedTimestamp DESC")
    fun getAllRequests(): Flow<List<ApprovalRequestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequest(request: ApprovalRequestEntity)

    @Update
    suspend fun updateRequest(request: ApprovalRequestEntity)

    @Query("UPDATE approval_requests SET status = :status, processedTimestamp = :timestamp, decisionReason = :reason WHERE id = :requestId")
    suspend fun resolveRequest(requestId: String, status: String, timestamp: Long, reason: String)
}

@Dao
interface SecurityEventDao {
    @Query("SELECT * FROM security_events ORDER BY timestamp DESC")
    fun getAllEvents(): Flow<List<SecurityEventEntity>>

    @Query("SELECT * FROM security_events WHERE severity = :severity ORDER BY timestamp DESC")
    fun getEventsBySeverity(severity: String): Flow<List<SecurityEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: SecurityEventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<SecurityEventEntity>)
}

@Dao
interface CompatibilityDao {
    @Query("SELECT * FROM compatibility_models ORDER BY manufacturer ASC, modelName ASC")
    fun getAllModels(): Flow<List<CompatibilityModelEntity>>

    @Query("SELECT * FROM compatibility_models WHERE manufacturer = :manufacturer ORDER BY modelName ASC")
    fun getModelsByManufacturer(manufacturer: String): Flow<List<CompatibilityModelEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModels(models: List<CompatibilityModelEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModel(model: CompatibilityModelEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompatibilityRequest(req: CompatibilityRequestEntity)

    @Query("SELECT * FROM compatibility_requests ORDER BY createdAt DESC")
    fun getAllCompatibilityRequests(): Flow<List<CompatibilityRequestEntity>>
}

@Dao
interface SecurityPolicyDao {
    @Query("SELECT * FROM security_policies LIMIT 1")
    fun getPolicy(): Flow<SecurityPolicyEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPolicy(policy: SecurityPolicyEntity)

    @Update
    suspend fun updatePolicy(policy: SecurityPolicyEntity)
}

@Dao
interface GuestPassDao {
    @Query("SELECT * FROM guest_passes ORDER BY createdAt DESC")
    fun getAllGuestPasses(): Flow<List<GuestPassEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGuestPass(pass: GuestPassEntity)

    @Update
    suspend fun updateGuestPass(pass: GuestPassEntity)

    @Query("UPDATE guest_passes SET isActive = 0 WHERE id = :passId")
    suspend fun revokeGuestPass(passId: String)
}

@Dao
interface IspDao {
    @Query("SELECT * FROM isp_organizations WHERE isActive = 1")
    fun getAllOrganizations(): Flow<List<IspOrganizationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrganizations(orgs: List<IspOrganizationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrganization(org: IspOrganizationEntity)

    @Query("SELECT * FROM isp_customers WHERE organizationId = :orgId ORDER BY customerName ASC")
    fun getCustomersByOrg(orgId: String): Flow<List<IspCustomerEntity>>

    @Query("SELECT * FROM isp_customers ORDER BY customerName ASC")
    fun getAllCustomers(): Flow<List<IspCustomerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomers(customers: List<IspCustomerEntity>)
}

@Dao
interface ParentalProfileDao {
    @Query("SELECT * FROM parental_profiles WHERE routerId = :routerId")
    fun getProfilesForRouter(routerId: String): Flow<List<ParentalProfileEntity>>

    @Query("SELECT * FROM parental_profiles ORDER BY profileName ASC")
    fun getAllProfiles(): Flow<List<ParentalProfileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ParentalProfileEntity)

    @Update
    suspend fun updateProfile(profile: ParentalProfileEntity)

    @Query("DELETE FROM parental_profiles WHERE id = :id")
    suspend fun deleteProfile(id: String)
}

@Dao
interface SecurityAuditDao {
    @Query("SELECT * FROM security_audit_findings WHERE routerId = :routerId ORDER BY id ASC")
    fun getFindingsForRouter(routerId: String): Flow<List<SecurityAuditFindingEntity>>

    @Query("SELECT * FROM security_audit_findings ORDER BY severity DESC, id ASC")
    fun getAllFindings(): Flow<List<SecurityAuditFindingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFindings(findings: List<SecurityAuditFindingEntity>)

    @Query("UPDATE security_audit_findings SET isRemediated = 1 WHERE id = :findingId")
    suspend fun markRemediated(findingId: String)
}
