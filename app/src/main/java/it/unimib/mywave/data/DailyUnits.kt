package it.unimib.mywave.data

import com.google.gson.annotations.SerializedName

data class DailyUnits(
    @SerializedName("swell_wave_direction_dominant")
    val swellWaveDirectionDominant: String,
    @SerializedName("swell_wave_height_max")
    val swellWaveHeightMax: String,
    @SerializedName("swell_wave_peak_period_max")
    val swellWavePeakPeriodMax: String,
    @SerializedName("swell_wave_period_max")
    val swellWavePeriodMax: String,
    val time: String,
    @SerializedName("wave_direction_dominant")
    val waveDirectionDominant: String,
    @SerializedName("wave_height_max")
    val waveHeightMax: String,
    @SerializedName("wave_period_max")
    val wavePeriodMax: String,
    @SerializedName("wind_wave_direction_dominant")
    val windWaveDirectionDominant: String,
    @SerializedName("wind_wave_height_max")
    val windWaveHeightMax: String,
    @SerializedName("wind_wave_peak_period_max")
    val windWavePeakPeriodMax: String,
    @SerializedName("wind_wave_period_max")
    val windWavePeriodMax: String
)