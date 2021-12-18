package eu.huberisation.moncarnetcovid.dcc_decoder

import COSE.HeaderKeys
import com.upokecenter.cbor.CBORObject

object CoseDecoder {
    fun decode(input: ByteArray): CoseData? {
        return try {
            val messageObject = CBORObject.DecodeFromBytes(input)
            val content = messageObject[2].GetByteString()
            val protectedHeader = messageObject[0].GetByteString()
            val unprotectedHeader = messageObject[1]
            val kid = getKid(protectedHeader, unprotectedHeader)
            val kidByteString = kid?.GetByteString()
            CoseData(content, kidByteString)
        } catch (e: Throwable) {
            null
        }
    }

    private fun getKid(protectedHeader: ByteArray, unprotectedHeader: CBORObject): CBORObject? {
        val key = HeaderKeys.KID.AsCBOR()
        return if (protectedHeader.isNotEmpty()) {
            try {
                val kid = CBORObject.DecodeFromBytes(protectedHeader).get(key)
                kid ?: unprotectedHeader.get(key)
            } catch (ex: Exception) {
                unprotectedHeader.get(key)
            }
        } else {
            unprotectedHeader.get(key)
        }
    }

    data class CoseData(
        val cbor: ByteArray,
        val kid: ByteArray? = null
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as CoseData

            if (!cbor.contentEquals(other.cbor)) return false
            if (kid != null) {
                if (other.kid == null) return false
                if (!kid.contentEquals(other.kid)) return false
            } else if (other.kid != null) return false

            return true
        }

        override fun hashCode(): Int {
            var result = cbor.contentHashCode()
            result = 31 * result + (kid?.contentHashCode() ?: 0)
            return result
        }
    }
}
