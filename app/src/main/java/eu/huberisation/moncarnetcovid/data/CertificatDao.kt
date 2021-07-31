package eu.huberisation.moncarnetcovid.data

import androidx.room.*
import eu.huberisation.moncarnetcovid.data.model.CertificatDto
import kotlinx.coroutines.flow.Flow

@Dao
interface CertificatDao {
    @Query("SELECT * FROM Certificat")
    fun getAll(): Flow<List<CertificatDto>>

    @Query("SELECT * FROM Certificat WHERE id=:id")
    suspend fun get(id: Long): CertificatDto

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(certificat: CertificatDto)

    @Query("DELETE FROM Certificat WHERE id=:id")
    suspend fun delete(id: Long)
}