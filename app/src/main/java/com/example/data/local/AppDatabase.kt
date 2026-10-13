package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.dao.ApprovalRequestDao
import com.example.data.local.dao.CompatibilityDao
import com.example.data.local.dao.DeviceDao
import com.example.data.local.dao.GuestPassDao
import com.example.data.local.dao.IspDao
import com.example.data.local.dao.NetworkDao
import com.example.data.local.dao.ParentalProfileDao
import com.example.data.local.dao.RouterDao
import com.example.data.local.dao.SecurityAuditDao
import com.example.data.local.dao.SecurityEventDao
import com.example.data.local.dao.SecurityPolicyDao
import com.example.data.local.dao.UserDao
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        NetworkEntity::class,
        RouterEntity::class,
        DeviceEntity::class,
        ApprovalRequestEntity::class,
        SecurityEventEntity::class,
        CompatibilityModelEntity::class,
        SecurityPolicyEntity::class,
        GuestPassEntity::class,
        CompatibilityRequestEntity::class,
        IspOrganizationEntity::class,
        IspCustomerEntity::class,
        ParentalProfileEntity::class,
        SecurityAuditFindingEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun networkDao(): NetworkDao
    abstract fun routerDao(): RouterDao
    abstract fun deviceDao(): DeviceDao
    abstract fun approvalRequestDao(): ApprovalRequestDao
    abstract fun securityEventDao(): SecurityEventDao
    abstract fun compatibilityDao(): CompatibilityDao
    abstract fun securityPolicyDao(): SecurityPolicyDao
    abstract fun guestPassDao(): GuestPassDao
    abstract fun ispDao(): IspDao
    abstract fun parentalProfileDao(): ParentalProfileDao
    abstract fun securityAuditDao(): SecurityAuditDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: run {
                    lateinit var builtDatabase: AppDatabase
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "universal_wifi_security_db"
                    )
                        .fallbackToDestructiveMigration()
                        .addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                CoroutineScope(Dispatchers.IO).launch {
                                    seedDatabase(builtDatabase)
                                }
                            }

                            override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                                super.onDestructiveMigration(db)
                                CoroutineScope(Dispatchers.IO).launch {
                                    seedDatabase(builtDatabase)
                                }
                            }
                        })
                        .build()
                    builtDatabase = instance
                    INSTANCE = instance
                    instance
                }
            }
        }

        private suspend fun seedDatabase(database: AppDatabase) {
            try {
                database.userDao().insertUser(DatabasePrepopulate.initialUser)
                database.networkDao().insertNetwork(DatabasePrepopulate.initialNetwork)
                database.routerDao().insertRouter(DatabasePrepopulate.initialRouter)
                database.routerDao().insertRouter(DatabasePrepopulate.initialSecondaryRouter)
                database.securityPolicyDao().insertPolicy(DatabasePrepopulate.initialPolicy)
                database.deviceDao().insertDevices(DatabasePrepopulate.initialDevices)
                database.approvalRequestDao().insertRequest(DatabasePrepopulate.initialApprovalRequests[0])
                database.securityEventDao().insertEvents(DatabasePrepopulate.initialSecurityEvents)
                database.compatibilityDao().insertModels(DatabasePrepopulate.initialCompatibilityCatalog)
                database.guestPassDao().insertGuestPass(DatabasePrepopulate.initialGuestPasses[0])
                database.ispDao().insertOrganizations(DatabasePrepopulate.initialIspOrganizations)
                database.ispDao().insertCustomers(DatabasePrepopulate.initialIspCustomers)
                database.parentalProfileDao().insertProfile(DatabasePrepopulate.initialParentalProfiles[0])
                database.securityAuditDao().insertFindings(DatabasePrepopulate.initialSecurityAuditFindings)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
