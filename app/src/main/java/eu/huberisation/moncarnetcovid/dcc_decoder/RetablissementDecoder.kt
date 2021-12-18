package eu.huberisation.moncarnetcovid.dcc_decoder

import com.upokecenter.cbor.CBORObject
import eu.huberisation.moncarnetcovid.entities.Retablissement
import java.text.SimpleDateFormat
import java.util.*

object RetablissementDecoder {
    private const val KEY_DATE = "df"

    fun fromMessage(retablissement: CBORObject): Retablissement {
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE)
            .parse(
                retablissement.getString(KEY_DATE)
            )

        return Retablissement(date)
    }
}