package it.unimib.mywave.view.screens.details

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import it.unimib.mywave.R
import it.unimib.mywave.data.MarkerData
import it.unimib.mywave.view.components.HeadingTextComponent
import it.unimib.mywave.view.screens.home.BottomBar
import it.unimib.mywave.view.screens.home.OfflineContent
import it.unimib.mywave.viewmodel.DetailsViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ViewDetailsScreen(
    navController: NavHostController,
    marker: MarkerData,
    detailsViewModel: DetailsViewModel = viewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val data by detailsViewModel.apiResponse.observeAsState()
    val noDataError by detailsViewModel.apiNoData
    var dataLoading: Job?
    var dataLoaded by remember { mutableStateOf(false) }
    var selectedDay by remember { mutableIntStateOf(0) }
    val graphView by remember { mutableStateOf(false) }
    val errorMessage = stringResource(id = R.string.error_opening_maps)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    LaunchedEffect(detailsViewModel) {
        dataLoading = coroutineScope.launch {
            detailsViewModel.getData(marker.latitude, marker.longitude)
        }
        dataLoading!!.invokeOnCompletion {
            dataLoaded = true
        }
    }


    Scaffold(
        modifier = Modifier
            .padding(0.dp, 0.dp, 0.dp, 0.dp),
        bottomBar = { BottomBar(navController = navController) },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                title = {

                    HeadingTextComponent(
                        value = stringResource(id = R.string.view_det),
                        color = Color.White
                    )
                },
                navigationIcon = {
                    if (navController.previousBackStackEntry != null) {
                        IconButton(
                            onClick = { navController.popBackStack() },
                        ) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = "Back",
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = {
                        detailsViewModel.navigateUsingGoogleMaps(
                            context,
                            marker.latitude,
                            marker.longitude
                        ) {

                            coroutineScope.launch {
                                val result = snackBarHostState
                                    .showSnackbar(
                                        message = errorMessage,
                                        // Defaults to SnackBarDuration.Short
                                        duration = SnackbarDuration.Short
                                    )
                                when (result) {
                                    SnackbarResult.ActionPerformed -> {
                                        // Nothing
                                    }

                                    SnackbarResult.Dismissed -> {
                                        // Nothing
                                    }
                                }
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.TravelExplore,
                            contentDescription = "Show on map",
                            modifier = Modifier.size(42.dp)
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
        {

            if (isOffline()) {
                OfflineContent()
            }
            if (data == null && !noDataError) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ElevatedCard(
                            modifier = Modifier
                                .padding(16.dp, 0.dp)
                                .fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
                            ) {
                                HeadingTextComponent(
                                    value = marker.name,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                    if (dataLoaded && !noDataError) {
                        Spacer(modifier = Modifier.height(25.dp))
                        Row(
                            modifier = Modifier
                                .padding(16.dp, 0.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = stringResource(id = R.string.forecast),
                                modifier = Modifier
                                    .weight(1f),
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontStyle = FontStyle.Normal,
                                ),
                                color = MaterialTheme.colorScheme.onBackground,
                                textAlign = TextAlign.Center,
                            )
                            /*Switch(
                                checked = graphView,
                                onCheckedChange = {
                                    graphView = it
                                },
                                thumbContent = if (graphView) {
                                    {
                                        Icon(
                                            imageVector = Icons.Outlined.BarChart,
                                            contentDescription = null,
                                            modifier = Modifier.size(SwitchDefaults.IconSize),
                                        )
                                    }
                                } else {
                                    {
                                        Icon(
                                            imageVector = Icons.Outlined.GridView,
                                            contentDescription = null,
                                            modifier = Modifier.size(SwitchDefaults.IconSize),
                                        )
                                    }
                                }
                            )*/
                        }


                        ForecastOverview(
                            data!!
                        ) { x -> selectedDay = x }
                        if (!graphView) {
                            SingleDayDetails(
                                data!!,
                                selectedDay
                            ) { x -> selectedDay = x }
                        }
                    } else if (noDataError) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            DataError(stringResource(id = R.string.error_no_data_for_this_spot))
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            DataError(stringResource(id = R.string.error_retrieving_data))
                        }
                    }
                    Spacer(modifier = Modifier.height(50.dp))
                }

            }

        }
    }
}

@Composable
fun isOffline(): Boolean {
    val context = LocalContext.current
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    return networkInfo != null && !(networkInfo.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            networkInfo.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
            networkInfo.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
}