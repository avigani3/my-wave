package it.unimib.mywave.view.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import it.unimib.mywave.R
import it.unimib.mywave.utils.Utils
import it.unimib.mywave.view.components.HeadingTextComponent
import it.unimib.mywave.view.components.SpotCard
import it.unimib.mywave.view.components.SpotSearchBar
import it.unimib.mywave.viewmodel.MapViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SpotScreen(
    navController: NavHostController,
    mapViewModel: MapViewModel = viewModel()
) {

    val markers by mapViewModel.markers.observeAsState()
    val coroutineScope = rememberCoroutineScope()
    var itemsLoaded by remember { mutableStateOf(false) } //simula caricamento dati
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        delay(500)
        itemsLoaded = true
    }
    LaunchedEffect(mapViewModel) {
        coroutineScope.launch {
            mapViewModel.loadMarkersFromDatabaseFilter(null)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp, 0.dp, 8.dp, 85.dp),
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            HeadingTextComponent(
                value = stringResource(id = R.string.my_favourite_spots),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(5.dp))
            var searchQuery by remember { mutableStateOf("") }

            // Barra di ricerca
            SpotSearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it }
            ) { query ->
                searchQuery = query
                coroutineScope.launch {
                    mapViewModel.loadMarkersFromDatabaseFilter(query)
                }
            }
            if (Utils.isOffline(context)) {
                OfflineContent()
            }
            if (markers == null || !itemsLoaded) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(36.dp)
                        .height(10.dp)
                )
            } else {
                if (markers!!.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.surf),
                            contentDescription = stringResource(R.string.no_spot),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = stringResource(R.string.no_spots_found),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Light,
                            modifier = Modifier
                                .padding(16.dp)
                        )
                    }
                } else {
                    LazyColumn {
                        itemsIndexed(markers!!.reversed()) { _, marker ->
                            SpotCard(marker, onDeleteClick = {
                                // Chiamato quando l'utente clicca sull'icona di eliminazione
                                coroutineScope.launch {
                                    mapViewModel.deleteMarker(marker, false)
                                }
                            },
                                onUpdateClick = { newName: String ->
                                    // Chiamato quando l'utente aggiorna il nome dello spot
                                    coroutineScope.launch {
                                        mapViewModel.updateMarkerName(marker, newName)
                                    }
                                },
                                onSpotCardClick = {
                                    // Chiamato quando l'utente clicca sulla card
                                    navController.navigate("details/${marker.key}/${marker.name}/${marker.latitude}/${marker.longitude}")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

