package it.unimib.mywave.data

import com.google.gson.annotations.SerializedName

data class Current(
    val interval: Int,
    @SerializedName("swell_wave_direction")
    val swellWaveDirection: Int?,
    @SerializedName("swell_wave_height")
    val swellWaveHeight: Double?,
    @SerializedName("swell_wave_peak_period")
    val swellWavePeakPeriod: Double?,
    @SerializedName("swell_wave_period")
    val swellWavePeriod: Double?,
    val time: String,
    @SerializedName("wave_direction")
    val waveDirection: Int?,
    @SerializedName("wave_height")
    val waveHeight: Double?,
    @SerializedName("wave_period")
    val wavePeriod: Double?,
    @SerializedName("wind_wave_direction")
    val windWaveDirection: Int?,
    @SerializedName("wind_wave_height")
    val windWaveHeight: Double?,
    @SerializedName("wind_wave_peak_period")
    val windWavePeakPeriod: Double?,
    @SerializedName("wind_wave_period")
    val windWavePeriod: Double?
)