package it.unimib.mywave.model

import android.content.Context
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import it.unimib.mywave.data.MarkerData

class LocationRepository(context: Context) {

    private val placesClient: PlacesClient

    init {
        Places.initialize(context, "AIzaSyA-YYHncox58pKny5owqSrADBxGbc6Jlsw")
        placesClient = Places.createClient(context)
    }

    fun searchPlaces(query: String, callback: (List<AutocompletePrediction>) -> Unit) {
        val bias = RectangularBounds.newInstance(
            LatLng(-85.0, -180.0),  // SW lat, lng
            LatLng(85.0, -180.0)   // NE lat, lng
        )

        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .setLocationBias(bias)
            .build()
        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                callback(response.autocompletePredictions)
            }
            .addOnFailureListener { exception ->
                Log.e("testPlace", "Error getting place predictions: ${exception.message}")
            }
    }

    fun getPlaceDetail(placeId: String, callback: (MarkerData?) -> Unit) {
        val placeDetailsRequest = FetchPlaceRequest.builder(placeId, listOf(Place.Field.LAT_LNG, Place.Field.NAME))
            .build()

        placesClient.fetchPlace(placeDetailsRequest)
            .addOnSuccessListener { response: FetchPlaceResponse ->
                val place = response.place
                val latLng = place.latLng
                val placeName = place.name ?: "Spot"
                Log.i("test", "PlaceName: $placeName, posizione: $latLng")

                val newMarker = MarkerData(placeId, placeName, latLng?.latitude ?: 0.0, latLng?.longitude ?: 0.0, 1, "")

                callback(newMarker)
            }
            .addOnFailureListener { exception ->
                Log.e("testPlace", "Error getting place details: ${exception.message}")
                callback(null)
            }
    }
}
