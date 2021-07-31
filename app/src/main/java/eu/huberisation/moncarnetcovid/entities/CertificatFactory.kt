package eu.huberisation.moncarnetcovid.entities

object CertificatFactory {
    fun creerCertificatDepuisCode(code: String, id: Long? = null): Certificat {
        return when {
            CertificatVaccination.correspond(code) -> CertificatVaccination(code, id)
            CertificatTest.correspond(code) -> CertificatTest(code, id)
            else -> CertificatEuropeen(code, id)
        }
    }
}