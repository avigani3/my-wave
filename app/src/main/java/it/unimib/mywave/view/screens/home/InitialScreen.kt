package it.unimib.mywave.view.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import it.unimib.mywave.R
import it.unimib.mywave.graphs.RootNavigationGraph
import it.unimib.mywave.view.screens.details.isOffline


@Composable
fun InitialScreen() {
    val isOffline = isOffline()
    if (isOffline) {
        OfflineContent()
    } else {
        RootNavigationGraph(navController = rememberNavController())
    }
}

@Composable
fun OfflineContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.offline),
            contentDescription = "Offline",
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Check Your Connection To Use The App",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Light,
            modifier = Modifier
                .padding(16.dp)
        )
    }
}
