package eu.huberisation.moncarnetcovid.dcc_decoder


import com.upokecenter.cbor.CBORObject
import eu.huberisation.moncarnetcovid.dcc_decoder.model.GreenCertificate

object CborDecoder {
    private const val KEY_CBOR_BODY = -260

    fun decode(input: ByteArray): GreenCertificate {
        val message = CBORObject.DecodeFromBytes(input)
        val corpsMessage = message[KEY_CBOR_BODY][1]
        return GreenCertificateDecoder.fromCborMessage(corpsMessage)
    }
}
