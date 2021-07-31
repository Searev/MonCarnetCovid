package eu.huberisation.moncarnetcovid.decoder

import android.os.Build
import com.upokecenter.cbor.CBORObject
import eu.huberisation.moncarnetcovid.entities.TypeCertificat
import eu.huberisation.moncarnetcovid.exceptions.CertificatInvalideException
import java.text.SimpleDateFormat
import java.util.*

object CborDecoder {
    private const val KEY_CBOR_BODY = -260
    private const val KEY_DETENTEUR = "nam"
    private const val KEY_PRENOM = "gn"
    private const val KEY_NOM = "fn"
    private const val KEY_VACCINATION = "v"
    private const val KEY_VACCINATION_DATE = "dt"
    private const val KEY_TEST = "t"
    private const val KEY_TEST_RESULTAT = "tr"
    private const val KEY_TEST_DATE = "sc"
    private const val KEY_RETABLISSEMENT = "r"
    private const val KEY_RETABLISSEMENT_DATE = "df"
    private const val TEST_RESULTAT_NEGATIF = "260415000"

    fun decode(input: ByteArray): CborDecoderCertificat {
        val message = CBORObject.DecodeFromBytes(input)
        val corpsMessage = message[KEY_CBOR_BODY][1]
        val detenteur = corpsMessage[KEY_DETENTEUR]

        val typeEtDate = when {
            corpsMessage[KEY_VACCINATION] != null -> Pair(
                TypeCertificat.VACCINATION,
                getDateFromString(cborToString(corpsMessage[KEY_VACCINATION][0][KEY_VACCINATION_DATE]))
            )
            corpsMessage[KEY_TEST] != null -> Pair(
                TypeCertificat.TEST,
                getDateFromISOString(cborToString(corpsMessage[KEY_TEST][0][KEY_TEST_DATE]))
            )
            corpsMessage[KEY_RETABLISSEMENT] != null -> Pair(
                TypeCertificat.RETABLISSEMENT,
                getDateFromString(cborToString(corpsMessage[KEY_RETABLISSEMENT][0][KEY_RETABLISSEMENT_DATE]))
            )
            else -> throw CertificatInvalideException()
        }

        val resultatTest = if (typeEtDate.first == TypeCertificat.TEST) {
             cborToString(corpsMessage[KEY_TEST][0][KEY_TEST_RESULTAT]) != TEST_RESULTAT_NEGATIF
        } else null

        return CborDecoderCertificat(
            cborToString(detenteur[KEY_PRENOM]),
            cborToString(detenteur[KEY_NOM]),
            typeEtDate.first,
            typeEtDate.second,
            resultatTest
        )
    }

    private fun cborToString(objet: CBORObject) = objet.toString().replace("\"", "")

    private fun getDateFromISOString(date: String): Date? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.FRANCE).parse(date)
            } else {
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.FRANCE).parse(date)
            }
        } catch (e: Throwable) {
            null
        }
    }

    private fun getDateFromString(date: String): Date? {
        return SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE).parse(date)
    }

    data class CborDecoderCertificat(
        val prenom: String,
        val nom: String,
        val type: TypeCertificat,
        val date: Date?,
        val resultat: Boolean?
    )
}
