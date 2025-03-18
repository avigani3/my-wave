package it.unimib.mywave.view.screens.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import it.unimib.mywave.R
import it.unimib.mywave.graphs.AuthScreen
import it.unimib.mywave.graphs.Graph
import it.unimib.mywave.utils.ServiceLocator
import it.unimib.mywave.view.components.ClickableTextComponentProfile
import it.unimib.mywave.view.components.HeadingTextComponent
import it.unimib.mywave.viewmodel.AuthViewModel
import it.unimib.mywave.viewmodel.ConfigurationsViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    navController: NavController,
    configViewModel: ConfigurationsViewModel = viewModel(),
    authViewModel: AuthViewModel = ServiceLocator.getAuthViewModel(navController)
) {
    val coroutineScope = rememberCoroutineScope()
    val config by configViewModel.optionalConfig.observeAsState()

    LaunchedEffect(configViewModel) {
        coroutineScope.launch {
            configViewModel.getConfigurationsForProfile()
        }

    }

    fun changeConfig(x: String) {
        coroutineScope.launch { configViewModel.changeConfig(x) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(210.dp, 20.dp, 0.dp, 20.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 0.dp, 0.dp, 0.dp),
            painter = painterResource(id = R.drawable.profile),
            contentDescription = "Fishing",
            contentScale = ContentScale.FillHeight,
        )
    }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp, 0.dp, 8.dp, 6.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(15.dp))

            HeadingTextComponent(
                value = stringResource(R.string.my_profile),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(15.dp))

            Row {
                Text(
                    text = stringResource(id = R.string.profile_config_title),
                    modifier = Modifier
                        .weight(1f),
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Normal,
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Start,
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 5.dp),

                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                ),
            ) {
                if (config == null) {
                    CircularProgressIndicator()
                } else {
                    ConfigList(config = config, changeConfig = { changeConfig(it) })
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 7.dp),
            ) {
                Text(
                    text = "Details",
                    modifier = Modifier
                        .weight(1f),
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Normal,
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Start,
                )
            }

            Row(
                modifier = Modifier
                    .padding(8.dp, 8.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.email),
                    style = TextStyle(
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Bold
                    ),
                )
                Text(
                    text = " " + authViewModel.getUserEmail(),
                    style = TextStyle(
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Normal
                    ),
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            ClickableTextComponentProfile(labelValue = stringResource(id = R.string.auth_logout),
                isBold = true,
                onButtonClicked = {
                    authViewModel.logout()
                    navController.navigate(route = AuthScreen.LoginScreen) {
                        launchSingleTop = true
                        popUpTo(route = Graph.HOME) {
                            inclusive = true
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(80.dp))
        }


    }
}