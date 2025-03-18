package it.unimib.mywave.view.screens.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.North
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.unimib.mywave.R
import it.unimib.mywave.data.OpenMeteoApiResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionalDataCard(
    onClick: () -> Unit,
    data: OpenMeteoApiResponse,
    selectedDay: Int,
    element: Int
) {
    var direction = false
    val dataSet: List<Number>?
    when (element) {
        R.string.wave_period -> dataSet = data.daily?.wavePeriodMax
        R.string.wind_wave_height -> dataSet = data.daily?.windWaveHeightMax
        R.string.wind_wave_direction -> {
            dataSet = data.daily?.windWaveDirectionDominant
            direction = true
        }

        R.string.wind_wave_period -> dataSet = data.daily?.windWavePeriodMax
        R.string.wind_wave_peak_period -> dataSet = data.daily?.windWavePeakPeriodMax
        R.string.swell_wave_height -> dataSet = data.daily?.swellWaveHeightMax
        R.string.swell_wave_direction -> {
            dataSet = data.daily?.swellWaveDirectionDominant
            direction = true
        }

        R.string.swell_wave_period -> dataSet = data.daily?.swellWavePeriodMax
        R.string.swell_wave_peak_period -> dataSet = data.daily?.swellWavePeakPeriodMax
        else -> {
            dataSet = null
        }
    }
    val units = when (element) {
        R.string.wave_period -> data.dailyUnits?.wavePeriodMax
        R.string.wind_wave_height -> data.dailyUnits?.windWaveHeightMax
        R.string.wind_wave_direction -> data.dailyUnits?.windWaveDirectionDominant
        R.string.wind_wave_period -> data.dailyUnits?.windWavePeriodMax
        R.string.wind_wave_peak_period -> data.dailyUnits?.windWavePeakPeriodMax
        R.string.swell_wave_height -> data.dailyUnits?.swellWaveHeightMax
        R.string.swell_wave_direction -> data.dailyUnits?.swellWaveDirectionDominant
        R.string.swell_wave_period -> data.dailyUnits?.swellWavePeriodMax
        R.string.swell_wave_peak_period -> data.dailyUnits?.swellWavePeakPeriodMax
        else -> {
            null
        }
    }

    val singleData = if (dataSet?.get(selectedDay) != null) {
        dataSet[selectedDay]
    } else {
        0
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 8.dp),
        onClick = {
            onClick()
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp, 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(element),
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onTertiary
            )
            if (direction) {
                Column {
                    Row {
                        Text(
                            text = singleData.toString() +
                                    units,
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                        Icon(
                            imageVector = Icons.Outlined.North,
                            contentDescription = null,
                            modifier = Modifier.rotate(
                                singleData.toFloat()
                            ),
                        )
                    }

                }
            } else {
                Text(
                    text = singleData.toString() +
                            " " + units,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onTertiary
                )
            }
        }
    }
}