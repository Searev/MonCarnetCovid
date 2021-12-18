package eu.huberisation.moncarnetcovid.data

import eu.huberisation.moncarnetcovid.data.model.CertificatDbEntity
import eu.huberisation.moncarnetcovid.entities.Certificat
import eu.huberisation.moncarnetcovid.entities.CertificatFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class CertificatRepository(private val certificatDao: CertificatDao) {
    fun recupererCertificats() = certificatDao.getAll().map { certificats ->
        certificats.map {
            CertificatFactory.creerCertificatDepuisCode(it.code, it.id)
        }
    }

    fun recupererCertificat(id: Long): Flow<Certificat> {
        return certificatDao
            .get(id)
            .filterNotNull()
            .map { CertificatFactory.creerCertificatDepuisCode(it.code, id) }
    }

    suspend fun supprimerCertificat(certificat: Certificat) {
        certificat.id?.let { certificatDao.delete(it) }
    }

    suspend fun creerCertificat(
        certificat: Certificat,
    ) {
        val certificatDto = CertificatDbEntity(certificat.code)
        return certificatDao.insert(certificatDto)
    }
}