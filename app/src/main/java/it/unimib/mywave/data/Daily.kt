package it.unimib.mywave.data

import com.google.gson.annotations.SerializedName

data class Daily(
    @SerializedName("swell_wave_direction_dominant")
    val swellWaveDirectionDominant: List<Int>?,
    @SerializedName("swell_wave_height_max")
    val swellWaveHeightMax: List<Double>?,
    @SerializedName("swell_wave_peak_period_max")
    val swellWavePeakPeriodMax: List<Double>?,
    @SerializedName("swell_wave_period_max")
    val swellWavePeriodMax: List<Double>?,
    val time: List<String>,
    @SerializedName("wave_direction_dominant")
    val waveDirectionDominant: List<Int>?,
    @SerializedName("wave_height_max")
    val waveHeightMax: List<Double>?,
    @SerializedName("wave_period_max")
    val wavePeriodMax: List<Double>?,
    @SerializedName("wind_wave_direction_dominant")
    val windWaveDirectionDominant: List<Int>?,
    @SerializedName("wind_wave_height_max")
    val windWaveHeightMax: List<Double>?,
    @SerializedName("wind_wave_peak_period_max")
    val windWavePeakPeriodMax: List<Double>?,
    @SerializedName("wind_wave_period_max")
    val windWavePeriodMax: List<Double>?
)