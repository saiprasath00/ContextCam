package com.saiprasath.contextcam.ui.gallery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.saiprasath.contextcam.data.db.AppDatabase
import com.saiprasath.contextcam.data.repository.PhotoRepository
import com.saiprasath.contextcam.databinding.ActivityPhotoDetailBinding
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class PhotoDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhotoDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val photoId = intent.getLongExtra(EXTRA_PHOTO_ID, -1)
        if (photoId == -1L) { finish(); return }

        val repo = PhotoRepository(AppDatabase.getInstance(this).photoDao())
        lifecycleScope.launch {
            val photo = repo.getById(photoId) ?: return@launch
            Glide.with(this@PhotoDetailActivity)
                .load(File(photo.filePath))
                .into(binding.ivPhoto)

            binding.tvNote.text = photo.note.ifBlank { "No note" }
            binding.tvLocation.text = photo.locationName.ifBlank { "Unknown location" }
            binding.tvDate.text = SimpleDateFormat("EEEE, MMM d yyyy  HH:mm", Locale.getDefault())
                .format(Date(photo.takenAt))
            binding.tvWeather.text = photo.weather.ifBlank { "" }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_PHOTO_ID = "extra_photo_id"
    }
}
