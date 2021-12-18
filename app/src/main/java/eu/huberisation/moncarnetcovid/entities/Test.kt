package eu.huberisation.moncarnetcovid.entities

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

data class Test(
    val date: Date?,
    val resultat: Boolean
): Serializable {
    val datePrelevement: String
        get() = date?.let {
            SimpleDateFormat("dd/MM/yyyy' à 'HH:mm", Locale.FRANCE).format(it)
        } ?: ""
}