package it.unimib.mywave.view.screens.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.unimib.mywave.R
import it.unimib.mywave.data.OpenMeteoApiResponse
import it.unimib.mywave.utils.Utils

@Composable
fun SingleDayDetails(
    data: OpenMeteoApiResponse,
    selectedDay: Int,
    changeSelectedDay: (newDay: Int) -> Unit
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var selectedElement by rememberSaveable { mutableIntStateOf(0) }
    var showAdditionalInfo by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(16.dp, 0.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                var dropDownExpanded by remember { mutableStateOf(false) }
                TextButton(
                    onClick = { dropDownExpanded = true },
                ) {
                    Text(
                        text = Utils.formatDate(data.daily!!.time[selectedDay]),
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Normal,
                        ),
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Start,
                    )
                    DropdownMenu(expanded = dropDownExpanded,
                        onDismissRequest = { dropDownExpanded = false }) {
                        data.daily.time.forEachIndexed { index, _ ->
                            val dateString = Utils.formatDate(data.daily.time[index])
                            DropdownMenuItem(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                text = {
                                    Text(
                                        text = dateString,
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                },
                                onClick = {
                                    changeSelectedDay(index)
                                    dropDownExpanded = false
                                })
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(id = R.string.hour),
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(id = R.string.wave_height),
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(id = R.string.wave_direction),
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Divider()
            for (i in 0..23 step 4) {
                HourlyOverview(data = data, selectedDay = selectedDay, hour = i)
                Divider()
            }
            Spacer(modifier = Modifier.padding(0.dp, 8.dp))
            if (showAdditionalInfo) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.optional_data),
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Normal,
                        ),
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }

            if (data.daily?.wavePeriodMax != null) {
                showAdditionalInfo = true
                OptionalDataCard(onClick = {
                    selectedElement = R.string.wave_period
                    showDialog = true
                }, data = data, selectedDay = selectedDay, element = R.string.wave_period)
            }
            if (data.daily?.windWaveHeightMax != null) {
                showAdditionalInfo = true
                OptionalDataCard(onClick = {
                    selectedElement = R.string.wind_wave_height
                    showDialog = true
                }, data = data, selectedDay = selectedDay, element = R.string.wind_wave_height)
            }
            if (data.daily?.windWaveDirectionDominant != null) {
                showAdditionalInfo = true
                OptionalDataCard(onClick = {
                    selectedElement = R.string.wind_wave_direction
                    showDialog = true
                }, data = data, selectedDay = selectedDay, element = R.string.wind_wave_direction)
            }
            if (data.daily?.windWavePeriodMax != null) {
                showAdditionalInfo = true
                OptionalDataCard(onClick = {
                    selectedElement = R.string.wind_wave_period
                    showDialog = true
                }, data = data, selectedDay = selectedDay, element = R.string.wind_wave_period)
            }
            if (data.daily?.windWavePeakPeriodMax != null) {
                showAdditionalInfo = true
                OptionalDataCard(onClick = {
                    selectedElement = R.string.wind_wave_peak_period
                    showDialog = true
                }, data = data, selectedDay = selectedDay, element = R.string.wind_wave_peak_period)
            }
            if (data.daily?.swellWaveHeightMax != null) {
                showAdditionalInfo = true
                OptionalDataCard(onClick = {
                    selectedElement = R.string.swell_wave_height
                    showDialog = true
                }, data = data, selectedDay = selectedDay, element = R.string.swell_wave_height)
            }
            if (data.daily?.swellWaveDirectionDominant != null) {
                showAdditionalInfo = true
                OptionalDataCard(onClick = {
                    selectedElement = R.string.swell_wave_direction
                    showDialog = true
                }, data = data, selectedDay = selectedDay, element = R.string.swell_wave_direction)
            }
            if (data.daily?.swellWavePeriodMax != null) {
                showAdditionalInfo = true
                OptionalDataCard(onClick = {
                    selectedElement = R.string.swell_wave_period
                    showDialog = true
                }, data = data, selectedDay = selectedDay, element = R.string.swell_wave_period)
            }
            if (data.daily?.swellWavePeakPeriodMax != null) {
                showAdditionalInfo = true
                OptionalDataCard(
                    onClick = {
                        selectedElement = R.string.swell_wave_peak_period
                        showDialog = true
                    },
                    data = data,
                    selectedDay = selectedDay,
                    element = R.string.swell_wave_peak_period
                )
            }
        }
        OptionalDataDialog(
            showDialog = showDialog,
            data = data,
            selectedDay = selectedDay,
            element = selectedElement,
            onDismiss = { showDialog = false }
        )
    }
}

