@file:JvmName("AuthNavGraphKt")

package it.unimib.mywave.graphs

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import it.unimib.mywave.view.screens.auth.LoginScreen
import it.unimib.mywave.view.screens.auth.RecoveryPasswordScreen
import it.unimib.mywave.view.screens.auth.SignUpScreen
import it.unimib.mywave.viewmodel.AuthViewModel
import it.unimib.mywave.viewmodel.AuthViewModelFactory

object AuthScreen {
    const val SignUpScreen = "SIGN_UP"
    const val LoginScreen = "LOGIN"
    const val RecoveryScreen = "RECOVERY"
}

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = AuthScreen.LoginScreen
    ) {
        composable(route = AuthScreen.LoginScreen) {
            val authViewModel: AuthViewModel =
                viewModel(factory = AuthViewModelFactory(navController))
            LoginScreen(navController, authViewModel)
        }
        composable(route = AuthScreen.SignUpScreen) {
            val authViewModel: AuthViewModel =
                viewModel(factory = AuthViewModelFactory(navController))
            SignUpScreen(navController,authViewModel)
        }
        composable(route = AuthScreen.RecoveryScreen) {
            val authViewModel: AuthViewModel =
                viewModel(factory = AuthViewModelFactory(navController))
            RecoveryPasswordScreen(navController, authViewModel)
        }
    }
}


