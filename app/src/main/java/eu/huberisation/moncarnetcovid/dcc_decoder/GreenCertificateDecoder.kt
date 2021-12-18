package eu.huberisation.moncarnetcovid.dcc_decoder

import com.upokecenter.cbor.CBORObject
import eu.huberisation.moncarnetcovid.dcc_decoder.model.GreenCertificate

object GreenCertificateDecoder {
    private const val KEY_VACCINATION = "v"
    private const val KEY_TEST = "t"
    private const val KEY_RETABLISSEMENT = "r"

    fun fromCborMessage(message: CBORObject) = GreenCertificate(
        DetenteurDecoder.fromCborObject(message),
        message[KEY_VACCINATION]?.let { VaccinationDecoder.fromMessage(it[0]) },
        message[KEY_TEST]?.let { TestDecoder.fromMessage(it[0]) },
        message[KEY_RETABLISSEMENT]?.let { RetablissementDecoder.fromMessage(it[0]) },
    )
}