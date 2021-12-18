package eu.huberisation.moncarnetcovid.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import eu.huberisation.moncarnetcovid.data.migrations.Migration0to1
import eu.huberisation.moncarnetcovid.data.model.CertificatDbEntity

@Database(
    entities = [CertificatDbEntity::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun certificatDao(): CertificatDao

    companion object {
        @Volatile private var instance: AppDatabase? = null
        private const val DATABASE_NAME = "MON_CARNET_COVID_DB"

        fun getDatabase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addMigrations(Migration0to1)
                .build()
        }
    }
}