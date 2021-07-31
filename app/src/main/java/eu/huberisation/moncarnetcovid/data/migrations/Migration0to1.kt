package eu.huberisation.moncarnetcovid.data.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration0to1: Migration(0, 1) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS `Certificat` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `contenu` TEXT NOT NULL)")
    }
}