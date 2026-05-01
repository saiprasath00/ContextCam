package com.saiprasath.contextcam.ui.camera

import android.app.Application
import android.location.Geocoder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.saiprasath.contextcam.data.db.AppDatabase
import com.saiprasath.contextcam.data.model.Photo
import com.saiprasath.contextcam.data.repository.PhotoRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume

class CameraViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = PhotoRepository(AppDatabase.getInstance(app).photoDao())
    private val fusedLocation = LocationServices.getFusedLocationProviderClient(app)

    fun savePhoto(filePath: String, note: String) {
        viewModelScope.launch {
            val (lat, lng, locationName) = fetchLocation()
            repo.save(
                Photo(
                    filePath = filePath,
                    note = note,
                    locationName = locationName,
                    latitude = lat,
                    longitude = lng
                )
            )
        }
    }

    @Suppress("MissingPermission")
    private suspend fun fetchLocation(): Triple<Double, Double, String> =
        suspendCancellableCoroutine { cont ->
            fusedLocation.lastLocation.addOnSuccessListener { loc ->
                if (loc == null) {
                    cont.resume(Triple(0.0, 0.0, "Unknown location"))
                    return@addOnSuccessListener
                }
                val geocoder = Geocoder(getApplication(), Locale.getDefault())
                try {
                    @Suppress("DEPRECATION")
                    val addresses = geocoder.getFromLocation(loc.latitude, loc.longitude, 1)
                    val name = addresses?.firstOrNull()?.let { addr ->
                        listOfNotNull(addr.subLocality, addr.locality, addr.countryName)
                            .joinToString(", ")
                    } ?: "Lat ${loc.latitude.toInt()}, Lng ${loc.longitude.toInt()}"
                    cont.resume(Triple(loc.latitude, loc.longitude, name))
                } catch (e: Exception) {
                    cont.resume(Triple(loc.latitude, loc.longitude, "Unknown location"))
                }
            }.addOnFailureListener {
                cont.resume(Triple(0.0, 0.0, "Unknown location"))
            }
        }
}
