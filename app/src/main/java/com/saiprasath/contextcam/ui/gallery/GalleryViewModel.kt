package com.saiprasath.contextcam.ui.gallery

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.saiprasath.contextcam.data.db.AppDatabase
import com.saiprasath.contextcam.data.model.Photo
import com.saiprasath.contextcam.data.repository.PhotoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GalleryViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = PhotoRepository(AppDatabase.getInstance(app).photoDao())
    private val searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val photos: StateFlow<List<Photo>> = searchQuery
        .debounce(300)
        .flatMapLatest { repo.search(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setQuery(q: String) { searchQuery.value = q }

    fun delete(photo: Photo) {
        viewModelScope.launch { repo.delete(photo) }
    }
}
