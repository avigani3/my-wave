package it.unimib.mywave.view.screens.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.unimib.mywave.R
import it.unimib.mywave.data.OpenMeteoApiResponse

@Composable
fun HourlyOptionalElementOverview(
    data: OpenMeteoApiResponse,
    selectedDay: Int,
    hour: Int,
    element: Int
) {
    val hourIndex = (selectedDay * 24) + hour // 0..23
    val dataSet = when (element) {
        R.string.wave_period -> data.hourly?.wavePeriod
        R.string.wind_wave_height -> data.hourly?.windWaveHeight
        R.string.wind_wave_direction -> data.hourly?.windWaveDirection
        R.string.wind_wave_period -> data.hourly?.windWavePeriod
        R.string.wind_wave_peak_period -> data.hourly?.windWavePeakPeriod
        R.string.swell_wave_height -> data.hourly?.swellWaveHeight
        R.string.swell_wave_direction -> data.hourly?.swellWaveDirection
        R.string.swell_wave_period -> data.hourly?.swellWavePeriod
        R.string.swell_wave_peak_period -> data.hourly?.swellWavePeakPeriod
        else -> {
            null
        }
    }

    val units = when (element) {
        R.string.wave_period -> data.hourlyUnits?.wavePeriod
        R.string.wind_wave_height -> data.hourlyUnits?.windWaveHeight
        R.string.wind_wave_direction -> data.hourlyUnits?.windWaveDirection
        R.string.wind_wave_period -> data.hourlyUnits?.windWavePeriod
        R.string.wind_wave_peak_period -> data.hourlyUnits?.windWavePeakPeriod
        R.string.swell_wave_height -> data.hourlyUnits?.swellWaveHeight
        R.string.swell_wave_direction -> data.hourlyUnits?.swellWaveDirection
        R.string.swell_wave_period -> data.hourlyUnits?.swellWavePeriod
        R.string.swell_wave_peak_period -> data.hourlyUnits?.swellWavePeakPeriod
        else -> {
            null
        }
    }

    val singleData = if (dataSet?.get(hourIndex) != null) {
        dataSet[hourIndex]
    } else {
        0
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = data.hourly?.time?.get(hourIndex)?.substring(11) ?: "00:00",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "$singleData $units",
            style = MaterialTheme.typography.titleMedium
        )
    }

}
