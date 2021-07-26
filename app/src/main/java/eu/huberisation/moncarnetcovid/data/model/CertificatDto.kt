package eu.huberisation.moncarnetcovid.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import eu.huberisation.moncarnetcovid.data.Converters
import eu.huberisation.moncarnetcovid.entities.TypeCertificat

@Entity
@TypeConverters(Converters::class)
data class Certificat (
    val code: String,
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
) {
    companion object {
        /*
        private fun decodeCertificatTestEu(code: String): Certificat {
            val codeSansEnTete = code.substring(26)
            val nom = codeSansEnTete.substringAfter("L0").substringBefore("L1")
            val prenom = codeSansEnTete.substringAfter("L1").substringBefore("L2")
            return Certificat(
                TypeCertificat.VACCINATION,
                code,
                "$prenom $nom",
                null
            )
        }

        private fun decodeAttestationVaccinationFr(code: String): Certificat {
            return Certificat(
                TypeCertificat.TEST,
                code,
                null,
                null
            )
        }*/
    }
}