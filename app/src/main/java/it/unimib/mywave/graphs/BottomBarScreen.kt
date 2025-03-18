package it.unimib.mywave.graphs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val color: Color
) {
    object Map : BottomBarScreen(
        route = "MAP",
        title = "Map",
        icon = Icons.Rounded.Public,
        color = Color.White
    )

    object Spots : BottomBarScreen(
        route = "SPOTS",
        title = "Spots",
        icon = Icons.Rounded.Star,
        color = Color.White
    )

    object Profile : BottomBarScreen(
        route = "PROFILE",
        title = "Profile",
        icon = Icons.Rounded.Person,
        color = Color.White
    )

}
