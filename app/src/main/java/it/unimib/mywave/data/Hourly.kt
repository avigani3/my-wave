package it.unimib.mywave.data

import com.google.gson.annotations.SerializedName

data class Hourly(
    @SerializedName("swell_wave_direction")
    val swellWaveDirection: List<Int>?,
    @SerializedName("swell_wave_height")
    val swellWaveHeight: List<Double>?,
    @SerializedName("swell_wave_peak_period")
    val swellWavePeakPeriod: List<Double>?,
    @SerializedName("swell_wave_period")
    val swellWavePeriod: List<Double>?,
    val time: List<String>,
    @SerializedName("wave_direction")
    val waveDirection: List<Int>?,
    @SerializedName("wave_height")
    val waveHeight: List<Double>?,
    @SerializedName("wave_period")
    val wavePeriod: List<Double>?,
    @SerializedName("wind_wave_direction")
    val windWaveDirection: List<Int>?,
    @SerializedName("wind_wave_height")
    val windWaveHeight: List<Double>?,
    @SerializedName("wind_wave_peak_period")
    val windWavePeakPeriod: List<Double>?,
    @SerializedName("wind_wave_period")
    val windWavePeriod: List<Double>?
)