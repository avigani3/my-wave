package it.unimib.mywave.view.screens.details

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.North
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.unimib.mywave.data.OpenMeteoApiResponse
import it.unimib.mywave.ui.theme.DetailColorDiscreet
import it.unimib.mywave.ui.theme.DetailColorGood
import it.unimib.mywave.ui.theme.DetailColorPoor
import it.unimib.mywave.ui.theme.DetailColorPoorDiscreet
import it.unimib.mywave.utils.Utils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastOverview(
    data: OpenMeteoApiResponse,
    onDaySelect: (x: Int) -> Unit
) {

    Box(
        modifier = Modifier
            .padding(16.dp, 0.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
        ) {
            data.daily?.time?.forEachIndexed { index, _ ->
                val timeString = Utils.formatDate(data.daily.time[index])
                val imageDirection = if (data.daily.waveDirectionDominant?.get(index) != null) {
                    data.daily.waveDirectionDominant[index].toFloat()
                } else {
                    0f
                }

                val waveHeight = if (data.daily.waveHeightMax?.get(index) != null) {
                    data.daily.waveHeightMax[index].toFloat()
                } else {
                    0f
                }

                val colorHeight = if (waveHeight < 1.1) {
                    DetailColorPoor
                } else if (waveHeight < 2.2) {
                    DetailColorPoorDiscreet
                } else if (waveHeight < 3.3) {
                    DetailColorDiscreet
                } else {
                    DetailColorGood
                }

                Card(
                    modifier = Modifier
                        .padding(8.dp, 6.dp)
                        .fillMaxWidth(),
                    onClick = { onDaySelect(index) },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ),
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = timeString,
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 16.sp
                        )
                        Text(
                            text = data.daily.waveHeightMax?.get(index)
                                .toString() + " " + data.dailyUnits!!.waveHeightMax,
                            style = MaterialTheme.typography.titleSmall,
                            fontSize = 16.sp,
                            color = colorHeight
                        )
                        Icon(
                            imageVector = Icons.Outlined.North,
                            contentDescription = null,
                            modifier = Modifier.rotate(
                                imageDirection
                            ),
                        )

                    }
                }

            }
        }

    }
}