package eu.huberisation.moncarnetcovid

import android.app.Application
import eu.huberisation.moncarnetcovid.data.AppDatabase
import eu.huberisation.moncarnetcovid.data.CertificatRepository

class MonCarnetCovidApplication: Application() {
    private val database by lazy { AppDatabase.getDatabase(this) }

    val certificatRepository by lazy {
        CertificatRepository(database.certificatDao())
    }
}