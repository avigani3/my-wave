package it.unimib.mywave.view.screens.details

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
import it.unimib.mywave.R

@Composable
fun DataError (
    message: String
){
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
            text = message,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Light,
            modifier = Modifier
                .padding(16.dp)
        )
    }
}
