package eu.huberisation.moncarnetcovid.entities

import com.google.zxing.BarcodeFormat

abstract class AbstractCertificat(open val code: String, val id: Long?) {
    abstract val type: TypeCertificat
    abstract val detenteur: String
    abstract val isValid: Boolean

    fun getBarcodeFormat(): BarcodeFormat {
        return when (type) {
            TypeCertificat.TEST, TypeCertificat.EUROPEEN -> BarcodeFormat.QR_CODE
            TypeCertificat.VACCINATION -> BarcodeFormat.DATA_MATRIX
        }
    }
}