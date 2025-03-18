package it.unimib.mywave.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import it.unimib.mywave.data.MarkerData
import it.unimib.mywave.view.screens.details.ViewDetailsScreen
import it.unimib.mywave.view.screens.home.InitialScreen
import it.unimib.mywave.view.screens.home.MapScreen
import it.unimib.mywave.view.screens.profile.ProfileScreen
import it.unimib.mywave.view.screens.home.SpotScreen

@Composable
fun HomeNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = BottomBarScreen.Spots.route
    ) {
        composable(route = BottomBarScreen.Map.route) {
            MapScreen(navController)
        }
        composable(route = BottomBarScreen.Spots.route) {
            SpotScreen(navController)
        }
        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen(navController)
        }
        composable(route = AuthScreen.LoginScreen) {
            InitialScreen()
        }
        composable(route = "details/{markerId}/{markerTitle}/{latitude}/{longitude}",
            arguments = listOf(
                navArgument("markerId") { NavType.StringType },
                navArgument("markerTitle") { NavType.StringType },
                navArgument("latitude") { NavType.StringType },
                navArgument("longitude") { NavType.StringType }
            )) {
            val marker = MarkerData(
                it.arguments?.getString("markerId")!!,
                it.arguments?.getString("markerTitle")!!,
                (it.arguments?.getString("latitude")!!).toDouble(),
                (it.arguments?.getString("longitude")!!).toDouble(),
                0, ""
            )
            ViewDetailsScreen(navController, marker)
        }
    }
}
