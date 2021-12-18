package eu.huberisation.moncarnetcovid.dcc_decoder.model

import eu.huberisation.moncarnetcovid.entities.*
import eu.huberisation.moncarnetcovid.exceptions.CertificatInvalideException
import java.util.*

data class GreenCertificate(
    val detenteur: Detenteur,
    val vaccination: Vaccination?,
    val test: Test?,
    val retablissement: Retablissement?
) {
    val type: TypeCertificat
        get() = when {
            vaccination != null -> TypeCertificat.VACCINATION
            test != null -> TypeCertificat.TEST
            retablissement != null -> TypeCertificat.RETABLISSEMENT
            else -> throw CertificatInvalideException()
        }

    val date: Date?
        get() = when {
            vaccination != null -> vaccination.dateInjection
            test != null -> test.date
            retablissement != null -> retablissement.date
            else -> null
        }
}
