package it.unimib.mywave.view.screens.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.unimib.mywave.R
import it.unimib.mywave.data.OpenMeteoApiResponse


@Composable
fun OptionalDataDialog(
    showDialog: Boolean,
    data: OpenMeteoApiResponse?,
    selectedDay: Int,
    element: Int,
    onDismiss: () -> Unit,
) {

    if (showDialog) {
        Surface(shadowElevation = 8.dp) {
            AlertDialog(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(stringResource(element), fontWeight = FontWeight.Bold)
                        IconButton(
                            modifier = Modifier
                                .height(40.dp),
                            onClick = {
                                onDismiss()
                            }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "SpotDialog.kt",
                            )
                        }
                    }
                },
                onDismissRequest = {
                    onDismiss()
                },
                text = {
                    Column {
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
                                    text = stringResource(element),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        for (i in 0..23 step 4) {
                            Divider()
                            HourlyOptionalElementOverview(
                                data = data!!,
                                selectedDay = selectedDay,
                                hour = i,
                                element = element
                            )
                        }

                    }
                },
                confirmButton = {})

        }
    }
}

