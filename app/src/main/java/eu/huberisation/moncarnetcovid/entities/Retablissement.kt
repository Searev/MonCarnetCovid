package eu.huberisation.moncarnetcovid.entities

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

data class Retablissement(val date: Date?): Serializable {
    fun getDate() = date?.let { SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(it) } ?: ""
}