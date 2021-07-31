package eu.huberisation.moncarnetcovid.entities

import eu.huberisation.moncarnetcovid.exceptions.CertificatInvalideException
import java.text.SimpleDateFormat
import java.util.*

class CertificatTest(code: String, id: Long?): Certificat(code, id) {
    override val dateEmission: Date?
    override val type = TypeCertificat.TEST
    override val europeen = false
    val resultatTest: String

    override val detenteur: String

    init {
        val resultats = validationRegex.find(code) ?: throw CertificatInvalideException()

        val nom = resultats.groups[INDEX_NOM]?.value?.replace("/", ", ") ?: ""
        val prenom = resultats.groups[INDEX_PRENOM]?.value ?: ""
        detenteur = "$prenom $nom"
        resultatTest = resultats.groups[INDEX_RESULTAT_TEST]?.value ?: ""
        dateEmission = resultats.groups[INDEX_DATE_ANALYSE]?.value?.let {
            SimpleDateFormat("ddMMyyyyHHmm", Locale.US).parse(it)
        }

    }

    companion object {
        private const val INDEX_PRENOM = 3
        private const val INDEX_NOM = 4
        private const val INDEX_RESULTAT_TEST = 8
        private const val INDEX_DATE_ANALYSE = 9

        private val validationRegex: Regex = "^[A-Z\\d]{4}" // Characters 0 to 3 are ignored. They represent the document format version.
            .plus("([A-Z\\d]{4})") // 1 - Characters 4 to 7 represent the document signing authority.
            .plus("([A-Z\\d]{4})") // 2 - Characters 8 to 11 represent the id of the certificate used to sign the document.
            .plus("[A-Z\\d]{8}") // Characters 12 to 19 are ignored.
            .plus("B2") // Characters 20 and 21 represent the wallet certificate type (sanitary, ...)
            .plus("[A-Z\\d]{4}") // Characters 22 to 25 are ignored.
            .plus("F0([^\\x1D\\x1E]+)[\\x1D\\x1E]") // 3 - We capture the field F0. It must have at least one character.
            .plus("F1([^\\x1D\\x1E]+)[\\x1D\\x1E]") // 4 - We capture the field F1. It must have at least one character.
            .plus("F2(\\d{8})") // 5 - We capture the field F2. It can only contain digits.
            .plus("F3([FMU]{1})") // 6 - We capture the field F3. It can only contain "F", "M" or "U".
            .plus("F4([A-Z\\d]{3,7})\\x1D?") // 7 - We capture the field F4. It can contain 3 to 7 uppercase letters and/or digits.
            // It can also be ended by the GS ASCII char (29) if the field reaches its max length.
            .plus("F5([PNIX]{1})") // 8 - We capture the field F5. It can only contain "P", "N", "I" or "X".
            .plus("F6(\\d{12})") // 9 - We capture the field F6. It can only contain digits.
            .plus("\\x1F{1}") // This character is separating the message from its signature.
            .plus("([A-Z\\d\\=]+)$").toRegex() // 10 - This is the message signature.

        fun correspond(code: String) = validationRegex.matches(code)
    }
}