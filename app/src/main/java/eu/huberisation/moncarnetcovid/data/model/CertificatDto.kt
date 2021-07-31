package eu.huberisation.moncarnetcovid.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName="Certificat"
)
data class CertificatDto (
    val code: String,
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
)