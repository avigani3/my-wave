package it.unimib.mywave.view.screens.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.North
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.unimib.mywave.R
import it.unimib.mywave.data.OpenMeteoApiResponse
import it.unimib.mywave.ui.theme.DetailColorDiscreet
import it.unimib.mywave.ui.theme.DetailColorGood
import it.unimib.mywave.ui.theme.DetailColorPoor
import it.unimib.mywave.ui.theme.DetailColorPoorDiscreet

@Composable
fun HourlyOverview(
    data: OpenMeteoApiResponse,
    selectedDay: Int,
    hour: Int,
) {
    val hourIndex = (selectedDay * 24) + hour // 0..23

    val waveDirection = if (data.hourly?.waveDirection?.get(hourIndex) != null) {
        data.hourly.waveDirection[hourIndex]
    } else {
        0
    }

    val waveHeight = if (data.hourly?.waveHeight?.get(hourIndex) != null) {
        data.hourly.waveHeight[hourIndex].toFloat()
    } else {
        0f
    }

    val imageDirection = if (data.hourly?.waveDirection?.get(hourIndex) != null) {
        data.hourly.waveDirection[hourIndex].toFloat()
    } else {
        0f
    }

    val textHeight: String

    val colorHeight: Color

    if (waveHeight < 1.1) {
        colorHeight = DetailColorPoor
        textHeight = stringResource(id = R.string.poor)
    } else if (waveHeight < 2.2) {
        colorHeight = DetailColorPoorDiscreet
        textHeight = stringResource(id = R.string.poor_discret)
    } else if (waveHeight < 3.3) {
        colorHeight = DetailColorDiscreet
        textHeight = stringResource(id = R.string.discreet)
    } else {
        colorHeight = DetailColorGood
        textHeight = stringResource(id = R.string.good)
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Card(
                    modifier = Modifier
                        .width(5.dp)
                        .height(35.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colorHeight
                    )
                ) {}
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = data.hourly?.time?.get(hourIndex)?.substring(11) ?: "00:00",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = waveHeight.toString() + " " + data.hourlyUnits?.waveHeight,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = textHeight,
                color = colorHeight,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Column {
            Row {
                Text(
                    text = waveDirection.toString() +
                            data.hourlyUnits!!.waveDirection,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
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
