package eu.huberisation.moncarnetcovid.entities

import com.google.zxing.BarcodeFormat
import java.io.Serializable
import java.util.*

abstract class Certificat(val code: String, val id: Long?): Serializable {
    abstract val type: TypeCertificat
    abstract val europeen: Boolean
    abstract val detenteur: String
    abstract val dateEmission: Date?

    fun getBarcodeFormat() = if (europeen) BarcodeFormat.QR_CODE else BarcodeFormat.DATA_MATRIX
}