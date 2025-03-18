package it.unimib.mywave.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import it.unimib.mywave.data.MarkerData
import it.unimib.mywave.model.LocationRepository
import it.unimib.mywave.model.MapRepository
import it.unimib.mywave.utils.ServiceLocator
import kotlinx.coroutines.launch

class MapViewModel(
    private val repository: MapRepository = ServiceLocator.getMapRepository(),
    private val locRepository: LocationRepository = ServiceLocator.getLocationRepository()
) : ViewModel() {

    private val _markers = MutableLiveData<List<MarkerData>>(mutableListOf())
    val markers: LiveData<List<MarkerData>> get() = _markers

    fun addMarker(locationName: String, latLng: LatLng) {
        val locName = locationName.ifBlank { "Spot" }

        viewModelScope.launch {
            repository.addMarker(locName, latLng)
            loadMarkersFromDatabase()
        }
    }

    fun deleteMarker(marker: MarkerData, includeRecommended: Boolean = true) {
        viewModelScope.launch {
            repository.deleteMarker(marker.key)
            if (includeRecommended) {
                loadMarkersFromDatabase()
            } else {
                loadMarkersFromDatabaseFilter(null)
            }
        }
    }

    fun updateMarkerName(marker: MarkerData, newName: String) {
        viewModelScope.launch {
            repository.updateMarkerName(marker.key, newName)
            loadMarkersFromDatabaseFilter(null)
        }
    }

    suspend fun loadMarkersFromDatabase() {
        _markers.value = repository.loadMarkersFromDatabase()

        locRepository.searchPlaces("surf spot") { predictions ->
            handlePlacePredictions(predictions, locRepository)
        }
    }

    fun searchAutoComplete(query: String, callback: (List<AutocompletePrediction>) -> Unit) {
        locRepository.searchPlaces(query, callback)
    }

    suspend fun loadMarkersFromDatabaseFilter(filterByName: String? = null) {
        _markers.value = repository.loadMarkersFromDatabaseFilter(filterByName)
    }

    private fun addMarkerRecommend(newMarker: MarkerData) {
        val currentMarkers = _markers.value!!.toMutableList()

        val containsDuplicate = currentMarkers.any {
            it.latitude == newMarker.latitude && it.longitude == newMarker.longitude
        }

        if (!containsDuplicate) {
            currentMarkers.add(newMarker)
            _markers.value = currentMarkers
        }
    }

    private fun handlePlacePredictions(
        predictions: List<AutocompletePrediction>,
        locationRepository: LocationRepository
    ) {
        for (prediction in predictions) {
            val placeId = prediction.placeId
            locationRepository.getPlaceDetail(placeId) { newMarker ->
                if (newMarker != null) {
                    addMarkerRecommend(newMarker)
                }
            }
        }
    }


}