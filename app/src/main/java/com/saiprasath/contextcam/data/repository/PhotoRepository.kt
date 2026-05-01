package com.saiprasath.contextcam.data.repository

import com.saiprasath.contextcam.data.db.PhotoDao
import com.saiprasath.contextcam.data.model.Photo
import kotlinx.coroutines.flow.Flow

class PhotoRepository(private val dao: PhotoDao) {

    fun getAllPhotos(): Flow<List<Photo>> = dao.getAllPhotos()

    fun search(query: String): Flow<List<Photo>> =
        if (query.isBlank()) dao.getAllPhotos() else dao.search(query)

    suspend fun save(photo: Photo): Long = dao.insert(photo)

    suspend fun delete(photo: Photo) = dao.delete(photo)

    suspend fun getById(id: Long): Photo? = dao.getById(id)
}
