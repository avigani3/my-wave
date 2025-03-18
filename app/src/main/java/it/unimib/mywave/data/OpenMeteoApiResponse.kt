package it.unimib.mywave.data

import com.google.gson.annotations.SerializedName

data class OpenMeteoApiResponse(
    val current: Current?,
    @SerializedName("current_units")
    val currentUnits: CurrentUnits?,
    val daily: Daily?,
    @SerializedName("daily_units")
    val dailyUnits: DailyUnits?,
    val elevation: Double,
    val generationTimeMs: Double,
    val hourly: Hourly?,
    @SerializedName("hourly_units")
    val hourlyUnits: HourlyUnits?,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timezoneAbbreviation: String,
    val utcOffsetSeconds: Int,
    var noDataError: Boolean = false
)