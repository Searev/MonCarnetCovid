package eu.huberisation.moncarnetcovid.dcc_decoder

import com.upokecenter.cbor.CBORObject

fun CBORObject.getString(field: String): String {
    return this[field].toString().replace("\"", "")
}

fun CBORObject.getInt(field: String): Int {
    return this[field].AsInt32()
}