package it.unimib.mywave.viewmodel

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.unimib.mywave.data.OpenMeteoApiResponse
import it.unimib.mywave.model.ConfigurationRepository
import it.unimib.mywave.model.MeteoRepository
import it.unimib.mywave.utils.ServiceLocator
import java.time.LocalDateTime

class DetailsViewModel (
    private val configurationRepository: ConfigurationRepository = ServiceLocator.getConfigurationRepository(),
    private val repository: MeteoRepository = ServiceLocator.getMeteoRepository()
) : ViewModel() {

    private var _apiResponse = MutableLiveData<OpenMeteoApiResponse?>(null)
    val apiResponse: LiveData<OpenMeteoApiResponse?> get() = _apiResponse
    val apiNoData: MutableState<Boolean> = mutableStateOf(false)

    suspend fun getData(latitude: Double, longitude: Double) {
        val configurations = configurationRepository.getConfigurations()
        if (_apiResponse.value == null ||
            LocalDateTime.parse(_apiResponse.value?.current?.time).isAfter(LocalDateTime.now().plusHours(3))) {
            repository.getOpenMeteoData(
                latitude,
                longitude,
                configurations,
                onSuccess = { response ->
                    _apiResponse.value = response
                },
                onNoData = {
                    apiNoData.value = true
                },
                onError = { error ->
                    Log.e(ContentValues.TAG, "Error: $error")
                })
        }
    }

    fun navigateUsingGoogleMaps(context: Context, lat: Double, lng: Double, onNoMaps: () -> Unit ){
        val geoUri = Uri.parse("geo:$lat,$lng")
        val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
        mapIntent.setPackage("com.google.android.apps.maps")

        if (mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        } else {
            onNoMaps.invoke()
        }
    }




}