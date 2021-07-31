package eu.huberisation.moncarnetcovid.data

import androidx.room.TypeConverter
import eu.huberisation.moncarnetcovid.entities.TypeCertificat
import java.util.*

class Converters {
    @TypeConverter
    fun depuisTypeCertificat(type: TypeCertificat) = type.ordinal

    fun versTypeCertificat(ordinal: Int?) = ordinal?.let { TypeCertificat.values()[it] }

    @TypeConverter
    fun fromDate(value: Date?): Long? = value?.time

    @TypeConverter
    fun toDate(value: Long?): Date? = value?.let { Date(value) }
}