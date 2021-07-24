package eu.huberisation.moncarnetcovid.data

import eu.huberisation.moncarnetcovid.model.Certificat
import eu.huberisation.moncarnetcovid.model.TypeCertificat

class CertificatRepository(private val certificatDao: CertificatDao) {
    fun recupererCertificats() = certificatDao.getAll()

    suspend fun recupererCertificat(id: Long) = certificatDao.get(id)

    suspend fun supprimerCertificat(certificat: Certificat) = certificatDao.delete(certificat)

    suspend fun creerCertificat(
        type: TypeCertificat,
        code: String,
    ) {
        val certificat = Certificat.fromCode(code, type)
        return certificatDao.insert(certificat)
    }
}