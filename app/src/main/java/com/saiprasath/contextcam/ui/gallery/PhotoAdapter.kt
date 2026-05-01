package com.saiprasath.contextcam.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.saiprasath.contextcam.data.model.Photo
import com.saiprasath.contextcam.databinding.ItemPhotoBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class PhotoAdapter(
    private val onClick: (Photo) -> Unit,
    private val onLongClick: (Photo) -> Unit
) : ListAdapter<Photo, PhotoAdapter.ViewHolder>(DIFF) {

    inner class ViewHolder(private val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: Photo) {
            Glide.with(binding.root)
                .load(File(photo.filePath))
                .centerCrop()
                .into(binding.ivThumb)

            binding.tvLocation.text = photo.locationName.ifBlank { "No location" }
            binding.tvDate.text = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
                .format(Date(photo.takenAt))
            binding.tvNote.text = photo.note.ifBlank { "" }

            binding.root.setOnClickListener { onClick(photo) }
            binding.root.setOnLongClickListener { onLongClick(photo); true }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(a: Photo, b: Photo) = a.id == b.id
            override fun areContentsTheSame(a: Photo, b: Photo) = a == b
        }
    }
}
