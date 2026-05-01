package com.saiprasath.contextcam.ui.gallery

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.saiprasath.contextcam.data.model.Photo
import com.saiprasath.contextcam.databinding.FragmentGalleryBinding
import kotlinx.coroutines.launch

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GalleryViewModel by viewModels()
    private lateinit var adapter: PhotoAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = PhotoAdapter(
            onClick = { photo -> openDetail(photo) },
            onLongClick = { photo -> confirmDelete(photo) }
        )

        binding.rvPhotos.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvPhotos.adapter = adapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setQuery(newText.orEmpty())
                return true
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.photos.collect { photos ->
                    adapter.submitList(photos)
                    binding.tvEmpty.visibility = if (photos.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }
    }

    private fun openDetail(photo: Photo) {
        val intent = Intent(requireContext(), PhotoDetailActivity::class.java)
        intent.putExtra(PhotoDetailActivity.EXTRA_PHOTO_ID, photo.id)
        startActivity(intent)
    }

    private fun confirmDelete(photo: Photo) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete photo?")
            .setMessage("This will remove the photo and its context.")
            .setPositiveButton("Delete") { _, _ -> viewModel.delete(photo) }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
