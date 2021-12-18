package eu.huberisation.moncarnetcovid.entities

import eu.huberisation.moncarnetcovid.dcc_decoder.Base45Decoder
import eu.huberisation.moncarnetcovid.dcc_decoder.CborDecoder
import eu.huberisation.moncarnetcovid.dcc_decoder.CompressorDecoder
import eu.huberisation.moncarnetcovid.dcc_decoder.CoseDecoder
import eu.huberisation.moncarnetcovid.exceptions.CertificatInvalideException
import java.io.Serializable

@OptIn(ExperimentalUnsignedTypes::class)
class CertificatEuropeen(code: String, id: Long? = null): Certificat(code, id), Serializable {
    override val type: TypeCertificat
    override val europeen = true
    override val detenteur: Detenteur
    override val test: Test?
    override val vaccination: Vaccination?
    override val retablissement: Retablissement?

    init {
        val codeSansPrefix = retirerPefixe()
        val base45Decoded = Base45Decoder.decode(codeSansPrefix)
        val cose = CompressorDecoder.decode(base45Decoded)
        val coseData = CoseDecoder.decode(cose)
        val greensCertificate = coseData?.let { CborDecoder.decode(it.cbor) } ?: throw CertificatInvalideException()

        detenteur = greensCertificate.detenteur
        type = greensCertificate.type
        test = greensCertificate.test
        vaccination = greensCertificate.vaccination
        retablissement = greensCertificate.retablissement
    }

    private fun retirerPefixe(): String {
        return if (code.startsWith(PREFIX_CERTIFICAT)) {
            code.drop(PREFIX_CERTIFICAT.length)
        } else {
            code
        }
    }

    companion object {
        const val PREFIX_CERTIFICAT = "HC1:"
    }
}