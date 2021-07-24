package eu.huberisation.moncarnetcovid.data

import androidx.room.*
import eu.huberisation.moncarnetcovid.model.Certificat
import kotlinx.coroutines.flow.Flow

@Dao
interface CertificatDao {
    @Query("SELECT * FROM Certificat")
    fun getAll(): Flow<List<Certificat>>

    @Query("SELECT * FROM Certificat WHERE id=:id")
    suspend fun get(id: Long): Certificat

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(certificat: Certificat)

    @Delete
    suspend fun delete(certificat: Certificat)
}