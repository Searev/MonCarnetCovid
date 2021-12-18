package eu.huberisation.moncarnetcovid.helper

import android.graphics.Bitmap
import com.journeyapps.barcodescanner.BarcodeEncoder
import eu.huberisation.moncarnetcovid.entities.Certificat

object BarcodeHelper {
    fun genererBitmap(certificat: Certificat, size: Int): Bitmap? {
        return BarcodeEncoder().encodeBitmap(
            certificat.code,
            certificat.getBarcodeFormat(),
            size,
            size
        )
    }
}