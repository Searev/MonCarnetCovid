package eu.huberisation.moncarnetcovid.entities

import com.google.zxing.BarcodeFormat
import java.io.Serializable

abstract class Certificat(val code: String, val id: Long?): Serializable {
    abstract val type: TypeCertificat
    abstract val europeen: Boolean
    abstract val detenteur: Detenteur
    abstract val test: Test?
    abstract val vaccination: Vaccination?
    abstract val retablissement: Retablissement?

    fun getBarcodeFormat() = if (europeen) BarcodeFormat.QR_CODE else BarcodeFormat.DATA_MATRIX
}