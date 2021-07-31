package eu.huberisation.moncarnetcovid.data

import eu.huberisation.moncarnetcovid.data.model.CertificatDto
import eu.huberisation.moncarnetcovid.entities.Certificat
import eu.huberisation.moncarnetcovid.entities.CertificatFactory
import kotlinx.coroutines.flow.map

class CertificatRepository(private val certificatDao: CertificatDao) {
    fun recupererCertificats() = certificatDao.getAll().map { certificats ->
        certificats.map {
            CertificatFactory.creerCertificatDepuisCode(it.code, it.id)
        }
    }

    suspend fun recupererCertificat(id: Long): Certificat {
        val certificat = certificatDao.get(id)
        return CertificatFactory.creerCertificatDepuisCode(certificat.code, id)
    }

    suspend fun supprimerCertificat(certificat: Certificat) {
        certificat.id?.let { certificatDao.delete(it) }
    }

    suspend fun creerCertificat(
        certificat: Certificat,
    ) {
        val certificatDto = CertificatDto(certificat.code)
        return certificatDao.insert(certificatDto)
    }
}