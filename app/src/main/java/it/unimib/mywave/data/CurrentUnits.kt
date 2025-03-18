package it.unimib.mywave.data

import com.google.gson.annotations.SerializedName

data class CurrentUnits(
    val interval: String,
    @SerializedName("swell_wave_direction")
    val swellWaveDirection: String,
    @SerializedName("swell_wave_height")
    val swellWaveHeight: String,
    @SerializedName("swell_wave_peak_period")
    val swellWavePeakPeriod: String,
    @SerializedName("swell_wave_period")
    val swellWavePeriod: String,
    val time: String,
    @SerializedName("wave_direction")
    val waveDirection: String,
    @SerializedName("wave_height")
    val waveHeight: String,
    @SerializedName("wave_period")
    val wavePeriod: String,
    @SerializedName("wind_wave_direction")
    val windWaveDirection: String,
    @SerializedName("wind_wave_height")
    val windWaveHeight: String,
    @SerializedName("wind_wave_peak_period")
    val windWavePeakPeriod: String,
    @SerializedName("wind_wave_period")
    val windWavePeriod: String
)