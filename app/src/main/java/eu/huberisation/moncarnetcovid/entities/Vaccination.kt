package eu.huberisation.moncarnetcovid.entities

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

data class Vaccination(
    val dateInjection: Date?,
    val nbDoses: Int,
    val nbDosesTotal: Int,
    val fabriquant: String,
    val produit: String,
): Serializable {
    val nomVaccin: String
        get() = "$produit | $fabriquant"

    val ratioDoses: String
        get() = "$nbDoses/$nbDosesTotal"

    val parcoursVaccinalTermine: Boolean
        get() = (nbDoses == nbDosesTotal)

    val dateFormatee: String
        get() = dateInjection?.let {
        SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(it)
    } ?: ""
}