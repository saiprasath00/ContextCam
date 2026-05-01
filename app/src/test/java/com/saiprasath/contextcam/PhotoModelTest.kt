package com.saiprasath.contextcam

import com.saiprasath.contextcam.data.model.Photo
import org.junit.Assert.*
import org.junit.Test

class PhotoModelTest {

    @Test
    fun `photo defaults are set correctly`() {
        val photo = Photo(filePath = "/sdcard/test.jpg")
        assertEquals("", photo.note)
        assertEquals("", photo.locationName)
        assertEquals(0.0, photo.latitude, 0.001)
        assertEquals(0.0, photo.longitude, 0.001)
        assertTrue(photo.takenAt > 0)
    }

    @Test
    fun `photo with all fields retains values`() {
        val photo = Photo(
            filePath = "/sdcard/img.jpg",
            note = "Beach day",
            locationName = "Pondicherry, India",
            latitude = 11.9416,
            longitude = 79.8083,
            weather = "Sunny"
        )
        assertEquals("Beach day", photo.note)
        assertEquals("Pondicherry, India", photo.locationName)
        assertEquals(11.9416, photo.latitude, 0.0001)
    }

    @Test
    fun `two photos with same data are equal`() {
        val ts = System.currentTimeMillis()
        val a = Photo(id = 1, filePath = "/f.jpg", takenAt = ts)
        val b = Photo(id = 1, filePath = "/f.jpg", takenAt = ts)
        assertEquals(a, b)
    }
}
