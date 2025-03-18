
package it.unimib.mywave.model

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import it.unimib.mywave.data.MarkerData
import it.unimib.mywave.utils.Utils
import kotlinx.coroutines.tasks.await

class MapRepository(
) {
    private val database =
        Firebase.database("https://fir-prova-7fe59-default-rtdb.europe-west1.firebasedatabase.app/")
    private val markersRef = database.reference.child("markers")

    suspend fun addMarker(locationName: String, latLng: LatLng) {
        val markerRef = markersRef.push()
        val key = markerRef.key ?: ""
        val userId = Utils.getUserKey() ?: ""
        val marker = MarkerData(key, locationName, latLng.latitude, latLng.longitude, 0, userId)

        markerRef.setValue(marker).await()
        Log.i("MapRepository", "Marker saved successfully")
    }

    suspend fun deleteMarker(markerKey: String) {
        markersRef.child(markerKey).removeValue().await()
        Log.i("MapRepository", "Marker deleted successfully")
    }

    suspend fun updateMarkerName(markerKey: String, newName: String) {
        val updatedName = if (newName.isBlank()) "Spot" else newName
        markersRef.child(markerKey).child("name").setValue(updatedName).await()
        Log.i("MapRepository", "Marker name updated successfully")
    }

    suspend fun loadMarkersFromDatabase(): List<MarkerData> {
        val query = markersRef.orderByChild("userId").equalTo(Utils.getUserKey())
        val dataSnapshot = query.get().await()

        val loadedMarkers = mutableListOf<MarkerData>()
        for (childSnapshot in dataSnapshot.children) {
            val latitude = childSnapshot.child("latitude").value as Double
            val longitude = childSnapshot.child("longitude").value as Double
            val key = childSnapshot.child("key").value as String
            val name = childSnapshot.child("name").value as String
            val userId = childSnapshot.child("userId").value as String

            loadedMarkers.add(MarkerData(key, name, latitude, longitude, 0, userId))
        }
        return loadedMarkers
    }

    suspend fun loadMarkersFromDatabaseFilter(filterByName: String? = null): List<MarkerData> {
        val query = markersRef.orderByChild("userId").equalTo(Utils.getUserKey())
        val dataSnapshot = query.get().await()

        val loadedMarkers = mutableListOf<MarkerData>()
        for (childSnapshot in dataSnapshot.children) {
            val latitude = childSnapshot.child("latitude").value as Double
            val longitude = childSnapshot.child("longitude").value as Double
            val key = childSnapshot.child("key").value as String
            val name = childSnapshot.child("name").value as String

            if (filterByName == null || name.contains(filterByName, ignoreCase = true)) {
                loadedMarkers.add(MarkerData(key, name, latitude, longitude, 0, ""))
            }
        }
        return loadedMarkers
    }
}