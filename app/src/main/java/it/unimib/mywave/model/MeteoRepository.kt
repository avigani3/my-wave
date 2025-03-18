package it.unimib.mywave.model

import android.content.ContentValues
import android.util.Log
import it.unimib.mywave.data.OpenMeteoApiResponse
import it.unimib.mywave.service.OpenMeteoService
import it.unimib.mywave.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MeteoRepository {

    fun getOpenMeteoData(
        latitude: Double,
        longitude: Double,
        configurations: Map<String, List<String>>,
        onSuccess: (OpenMeteoApiResponse?) -> Unit = {},
        onNoData: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        val timezone = "auto"
        val current = configurations["current"]
        val daily = configurations["daily"]
        val hourly = configurations["hourly"]

        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.OPEN_METEO_BASE_URL)
            .build()
            .create(OpenMeteoService::class.java)

        val retrofitData = retrofitBuilder.getData(
            latitude,
            longitude,
            current,
            daily,
            hourly,
            timezone,
        )

        retrofitData.enqueue(object : Callback<OpenMeteoApiResponse?> {
            override fun onResponse(
                call: Call<OpenMeteoApiResponse?>,
                response: Response<OpenMeteoApiResponse?>
            ) {
                Log.d(ContentValues.TAG, "onResponse: $response")
                if (response.errorBody() != null) {
                    onNoData.invoke()
                }
                onSuccess.invoke(response.body())

            }

            override fun onFailure(call: Call<OpenMeteoApiResponse?>, t: Throwable) {
                onError.invoke(t.message ?: "Unknown error")
            }
        })
    }
}