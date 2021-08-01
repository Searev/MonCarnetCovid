package eu.huberisation.moncarnetcovid.data.migrations

import androidx.annotation.VisibleForTesting
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@VisibleForTesting
object Migration0to1: Migration(0, 1) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS `Certificat` (`code` TEXT NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT);")
    }
}