package eu.huberisation.moncarnetcovid.dcc_decoder

import com.upokecenter.cbor.CBORObject
import eu.huberisation.moncarnetcovid.entities.Detenteur
import java.text.SimpleDateFormat
import java.util.*

object DetenteurDecoder {
    private const val KEY_DETENTEUR = "nam"
    private const val KEY_DATE_DE_NAISSANCE = "dob"
    private const val KEY_PRENOM = "gn"
    private const val KEY_NOM = "fn"

    fun fromCborObject(message: CBORObject): Detenteur {
        val detenteur = message[KEY_DETENTEUR]
        val nom = formatName(detenteur.getString(KEY_NOM))
        val prenom = formatName(detenteur.getString(KEY_PRENOM).capitalize())

        val formatDateCertificat = SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE)
        val formatDatecible = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
        val date = message.getString(KEY_DATE_DE_NAISSANCE)
        val dateDeNaissance = formatDateCertificat.parse(date)
        return Detenteur(
            "$prenom $nom",
            dateDeNaissance?.let { formatDatecible.format(it) } ?: date
        )
    }

    private fun formatName(nom: String) = nom.lowercase().replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }
}