package eu.huberisation.moncarnetcovid.dcc_decoder

import android.os.Build
import com.upokecenter.cbor.CBORObject
import eu.huberisation.moncarnetcovid.entities.Test
import java.text.SimpleDateFormat
import java.util.*

object TestDecoder {
    private const val KEY_RESULTAT = "tr"
    private const val KEY_DATE = "sc"
    private const val TEST_RESULTAT_NEGATIF = "260415000"

    fun fromMessage(test: CBORObject): Test {
        val date = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.FRANCE).parse(test.getString(
                    KEY_DATE
                ))
            } else {
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.FRANCE).parse(test.getString(
                    KEY_DATE
                ))
            }
        } catch (e: Throwable) {
            null
        }

        val resultat = test.getString(KEY_RESULTAT) != TEST_RESULTAT_NEGATIF

        return Test(date, resultat)
    }
}