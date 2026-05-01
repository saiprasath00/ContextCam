package com.saiprasath.contextcam.data.db

import androidx.room.*
import com.saiprasath.contextcam.data.model.Photo
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photo: Photo): Long

    @Delete
    suspend fun delete(photo: Photo)

    @Query("SELECT * FROM photos ORDER BY takenAt DESC")
    fun getAllPhotos(): Flow<List<Photo>>

    @Query("""
        SELECT * FROM photos
        WHERE note LIKE '%' || :query || '%'
           OR locationName LIKE '%' || :query || '%'
           OR weather LIKE '%' || :query || '%'
        ORDER BY takenAt DESC
    """)
    fun search(query: String): Flow<List<Photo>>

    @Query("SELECT * FROM photos WHERE id = :id")
    suspend fun getById(id: Long): Photo?
}
