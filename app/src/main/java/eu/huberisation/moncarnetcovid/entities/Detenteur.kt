package eu.huberisation.moncarnetcovid.entities

import java.io.Serializable

data class Detenteur(
    val nom: String,
    val dateNaissance: String
): Serializable