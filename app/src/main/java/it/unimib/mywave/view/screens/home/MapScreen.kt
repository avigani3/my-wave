package it.unimib.mywave.view.screens.home

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import it.unimib.mywave.R
import it.unimib.mywave.graphs.BottomBarScreen
import it.unimib.mywave.viewmodel.MapViewModel
import kotlinx.coroutines.launch
import java.io.IOException


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun MapScreen(
    navController: NavHostController,
    mapViewModel: MapViewModel = viewModel(),
) {


    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(41.8719, 12.5674), 5f)
    }

    var locationName by rememberSaveable { mutableStateOf("") } // Query for SearchBar
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var showDialog by rememberSaveable { mutableStateOf(false) }
    var showCreationDialog by rememberSaveable { mutableStateOf(false) }
    var newSpotCoord by rememberSaveable { mutableStateOf(LatLng(0.0, 0.0)) }
    var newSpotName by rememberSaveable { mutableStateOf("") }

    var autoCompletePredictions by rememberSaveable { mutableStateOf(listOf<AutocompletePrediction>()) }
    var selectedElement by rememberSaveable { mutableStateOf(false) }



    Scaffold(
        modifier = Modifier
            .padding(0.dp, 0.dp, 0.dp, 80.dp),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {


        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapLongClick = { latLng ->
                newSpotCoord = latLng
                showCreationDialog = true
            }
        ) {
            val markers by mapViewModel.markers.observeAsState()
            Log.d("MapScreen", "Markers: $markers")

            LaunchedEffect(mapViewModel) {
                coroutineScope.launch {
                    mapViewModel.loadMarkersFromDatabase()

                }

            }

            markers?.forEach { marker ->
                Marker(
                    position = LatLng(marker.latitude, marker.longitude),
                    title = marker.name,
                    draggable = false,
                    snippet = setSnippet(marker.type),
                    onInfoWindowLongClick = {
                        if (marker.type == 0) {
                            navController.navigate("details/${marker.key}/${marker.name}/${marker.latitude}/${marker.longitude}")
                        } else {
                            newSpotName = marker.name
                            newSpotCoord = LatLng(marker.latitude, marker.longitude)
                            it.hideInfoWindow()
                            showCreationDialog = true
                        }


                    },
                    onClick = {
                        it.showInfoWindow()
                        true
                    },
                    icon = defaultColoredMarker(marker.type)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                query = locationName,
                onQueryChange = {
                    locationName = it
                    selectedElement = false
                    coroutineScope.launch {
                        mapViewModel.searchAutoComplete(it) { predictions ->
                            autoCompletePredictions = predictions
                        }
                    }
                },
                onSearch = {
                    getMapLocation(locationName, context, cameraPositionState)
                    keyboardController?.hide()
                    focusManager.clearFocus()
                },
                active = locationName.isNotEmpty() && !selectedElement,
                onActiveChange = {
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.search),
                        textDecoration = TextDecoration.Underline
                    )
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
                },
                trailingIcon = {
                    if (locationName.isNotEmpty()) {
                        Icon(
                            modifier = Modifier.clickable {
                                if (locationName.isNotEmpty()) {
                                    locationName = ""
                                }
                            },
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close icon"
                        )
                    }

                },
            ) {
                for (prediction in autoCompletePredictions) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            locationName = prediction
                                .getPrimaryText(null)
                                .toString()
                            getMapLocation(locationName, context, cameraPositionState)
                            selectedElement = true
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }) {
                        Text(
                            modifier = Modifier.padding(10.dp),
                            text = prediction.getPrimaryText(null).toString(),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }


            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 0.dp, 0.dp, 80.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        IconButton(
            modifier = Modifier
                .height(120.dp)
                .width(70.dp)
                .padding(8.dp, 15.dp),
            onClick = {
                showDialog = true
            }) {
            Icon(
                modifier = Modifier
                    .fillMaxSize(),
                imageVector = Icons.Default.Info,
                contentDescription = "SpotDialog.kt",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                // Chiudi il dialog quando viene richiesto il dismiss
                showDialog = false
            },
            title = {
                Text(text = stringResource(R.string.how_to_save_a_spot))
            },
            text = {
                Text(text = stringResource(R.string.how_to_save_a_spot_text))
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Chiudi il dialog quando viene cliccato il pulsante di conferma
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(text = "Ok")
                }
            }
        )
    }

    if (showCreationDialog) {
        val savedMessage = stringResource(R.string.saved_to_favourite_spots)
        val seeListMessage = stringResource(R.string.see_list)
        AlertDialog(
            onDismissRequest = {
                newSpotName = ""
                showCreationDialog = false
            },
            title = {
                Text(text = stringResource(R.string.save_spot))
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = newSpotName,
                        onValueChange = { newSpotName = it },
                        label = { Text(stringResource(R.string.name)) },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.secondary,
                            focusedLabelColor = MaterialTheme.colorScheme.secondary,
                            cursorColor = MaterialTheme.colorScheme.secondary,
                        )
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        newSpotName = ""
                        showCreationDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    val cancelButton = stringResource(R.string.cancel)
                    Text(text = cancelButton)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            mapViewModel.addMarker(newSpotName, newSpotCoord)

                            val result = snackbarHostState
                                .showSnackbar(
                                    message = savedMessage,
                                    actionLabel = seeListMessage,
                                    duration = SnackbarDuration.Short

                                )
                            when (result) {
                                SnackbarResult.ActionPerformed -> {
                                    // See list
                                    navController.navigate(BottomBarScreen.Spots.route)
                                }

                                SnackbarResult.Dismissed -> {
                                    // Nothing
                                }
                            }
                            newSpotName = ""
                        }

                        showCreationDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    val saveButton = stringResource(R.string.save)
                    Text(text = saveButton)
                }
            }
        )
    }
}


private fun getMapLocation(
    location: String,
    context: Context,
    cameraPositionState: CameraPositionState
) {

    val geocoder = Geocoder(context)
    try {
        val addresses: List<Address>? = geocoder.getFromLocationName(location, 1)
        if (!addresses.isNullOrEmpty()) {
            val latLng = LatLng(addresses[0].latitude, addresses[0].longitude)
            cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 15f)
        } else {
            Toast.makeText(context, "Location not found", Toast.LENGTH_SHORT).show()
        }
    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(context, "Error retrieving location", Toast.LENGTH_SHORT).show()
    }
}
/*fun searchPlaces(
    query: String,
    context: Context,
    callback: (List<AutocompletePrediction>) -> Unit
) {
    var locationRepository = LocationRepository(context)
    locationRepository.searchPlaces(query, callback)
}

fun handlePlacePredictions(
    predictions: List<AutocompletePrediction>,
    locationRepository: LocationRepository,
    mapViewModel: MapViewModel
) {
    for (prediction in predictions) {
        val placeId = prediction.placeId
        locationRepository.getPlaceDetail(placeId) { newMarker ->
            if (newMarker != null) {
                mapViewModel.addMarkerRecommend(newMarker)
            }
        }
    }
}*/


private fun defaultColoredMarker(type: Int): BitmapDescriptor {
    return when (type) {
        1 -> defaultMarker(BitmapDescriptorFactory.HUE_RED)
        else -> defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
    }
}

private fun setSnippet(type: Int): String {
    return when (type) {
        1 -> "Recommended, long click to save"
        else -> "Long click here to view details"
    }
}






