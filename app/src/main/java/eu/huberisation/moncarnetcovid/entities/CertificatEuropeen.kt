package eu.huberisation.moncarnetcovid.entities

import eu.huberisation.moncarnetcovid.decoder.Base45Decoder
import eu.huberisation.moncarnetcovid.decoder.CborDecoder
import eu.huberisation.moncarnetcovid.decoder.CompressorDecoder
import eu.huberisation.moncarnetcovid.decoder.CoseDecoder
import eu.huberisation.moncarnetcovid.exceptions.CertificatInvalideException
import java.util.*

@OptIn(ExperimentalUnsignedTypes::class)
class CertificatEuropeen(code: String, id: Long? = null): Certificat(code, id) {
    override val type: TypeCertificat
    override val europeen = true
    override val detenteur: String
    override val dateEmission: Date?
    val resultatTest: Boolean?

    init {
        val codeSansPrefix = retirerPefixe()
        val base45Decoded = Base45Decoder.decode(codeSansPrefix)
        val cose = CompressorDecoder.decode(base45Decoded)
        val coseData = CoseDecoder.decode(cose)
        val certData = coseData?.let { CborDecoder.decode(it.cbor) } ?: throw CertificatInvalideException()

        detenteur = certData.let { "${it.prenom} ${it.nom}" } ?: ""
        type = certData.type
        dateEmission = certData.date
        resultatTest = if (type === TypeCertificat.TEST) certData.resultat else null
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