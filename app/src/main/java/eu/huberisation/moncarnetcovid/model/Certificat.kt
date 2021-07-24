package eu.huberisation.moncarnetcovid.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.zxing.BarcodeFormat
import eu.huberisation.moncarnetcovid.data.Converters

@Entity
@TypeConverters(Converters::class)
data class Certificat (
    val type: TypeCertificat,
    val contenu: String,
    val detenteur: String?,
    val expiration: String?,
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
) {
    fun getBarcodeFormat(): BarcodeFormat {
        return when (type) {
            TypeCertificat.SANITAIRE -> BarcodeFormat.QR_CODE
            TypeCertificat.VACCINATION -> BarcodeFormat.DATA_MATRIX
        }
    }

    companion object {
        fun fromCode(code: String, type: TypeCertificat): Certificat {
            return when (type) {
                TypeCertificat.VACCINATION -> decodeCertificatTestEu(code)
                TypeCertificat.SANITAIRE -> decodeAttestationVaccinationFr(code)
            }
        }

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
                TypeCertificat.SANITAIRE,
                code,
                null,
                null
            )
        }
    }
}