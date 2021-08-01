package eu.huberisation.moncarnetcovid.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName="Certificat"
)
data class CertificatDbEntity (
    @ColumnInfo(name="code") val code: String,
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
)