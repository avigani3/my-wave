package it.unimib.mywave.service

import it.unimib.mywave.data.OpenMeteoApiResponse
import retrofit2.Call
import retrofit2.http.GET
import it.unimib.mywave.utils.Constants.OPEN_METEO_REQUEST_URL
import retrofit2.http.Query

interface OpenMeteoService {

    @GET(OPEN_METEO_REQUEST_URL)
    fun getData(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("current") current: List<String>? = null,
        @Query("daily") daily: List<String>? = null,
        @Query("hourly") hourly: List<String>? = null,
        @Query("timezone") timezone: String? = null,
        @Query("length_unit") lengthUnit: String? = null,
        @Query("timeformat") timeFormat: String? = null,
    ): Call<OpenMeteoApiResponse>
}