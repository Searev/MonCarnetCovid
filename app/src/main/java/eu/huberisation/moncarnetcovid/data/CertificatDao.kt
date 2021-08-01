package eu.huberisation.moncarnetcovid.data

import androidx.room.*
import eu.huberisation.moncarnetcovid.data.model.CertificatDbEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CertificatDao {
    @Query("SELECT * FROM Certificat")
    fun getAll(): Flow<List<CertificatDbEntity>>

    @Query("SELECT * FROM Certificat WHERE id=:id")
    suspend fun get(id: Long): CertificatDbEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(certificat: CertificatDbEntity)

    @Query("DELETE FROM Certificat WHERE id=:id")
    suspend fun delete(id: Long)
}