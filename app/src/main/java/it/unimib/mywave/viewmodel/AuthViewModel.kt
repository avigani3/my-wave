package it.unimib.mywave.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import it.unimib.mywave.data.AuthUIEvent
import it.unimib.mywave.data.AuthUIState
import it.unimib.mywave.data.validator.Validator
import it.unimib.mywave.graphs.AuthScreen
import it.unimib.mywave.graphs.Graph
import it.unimib.mywave.model.AuthRepository
import it.unimib.mywave.utils.ServiceLocator
import kotlinx.coroutines.launch

class AuthViewModel(
    private val navController: NavController,
    private val repository: AuthRepository = ServiceLocator.getAuthRepository()
) : ViewModel() {

    private val TAG = AuthViewModel::class.simpleName
    val hasUser: Boolean
        get() = repository.hasUser()

    fun getUserEmail(): String = repository.getUserEmail()
    var authUIState = mutableStateOf(AuthUIState())
        private set

    //VALIDATOR
    private var loginValidationsPassed = mutableStateOf(false)
    var signUpValidationsPassed = mutableStateOf(false)
    var recoveryPassValidate = mutableStateOf(false)
    var authInProgress = mutableStateOf(false)


    fun onEvent(event: AuthUIEvent) {
        when (event) {
            //LOGIN
            is AuthUIEvent.LoginEmailChanged -> {
                authUIState.value = authUIState.value.copy(
                    loginEmail = event.email
                )
                validateLoginUIDataWithRules()
            }

            is AuthUIEvent.LoginPasswordChanged -> {
                authUIState.value = authUIState.value.copy(
                    loginPassword = event.password
                )
                validateLoginUIDataWithRules()
            }

            is AuthUIEvent.LoginButtonClicked -> {
                login()
            }

            //RECOVERY
            is AuthUIEvent.RecoveryEmailChanged -> {
                authUIState.value = authUIState.value.copy(
                    recoveryEmail = event.email
                )
                validateRecoveryUIDataWithRules()
            }

            is AuthUIEvent.RecoveryButtonClicked -> {
                recoveryPass()
            }

            //SIGNUP
            is AuthUIEvent.SignNickNameChanged -> {
                authUIState.value = authUIState.value.copy(
                    signNickName = event.nickName
                )
                validateSignUpWithRules()
            }

            is AuthUIEvent.SignEmailChanged -> {
                authUIState.value = authUIState.value.copy(
                    signEmail = event.email
                )
                validateSignUpWithRules()
            }

            is AuthUIEvent.SignPasswordChange -> {
                authUIState.value = authUIState.value.copy(
                    signPassword = event.password
                )
                validateSignUpWithRules()
            }

            is AuthUIEvent.SignRepeatPasswordChange -> {
                authUIState.value = authUIState.value.copy(
                    signRepeatPassword = event.repeatPassword
                )
                validateSignUpWithRules()
            }

            is AuthUIEvent.SignButtonClicked -> {
                signUp()
            }
        }
    }

    private fun validateLoginUIDataWithRules() {
        val emailResult = Validator.validateEmail(
            email = authUIState.value.loginEmail
        )
        val passwordResult = Validator.validatePassword(
            password = authUIState.value.loginPassword
        )

        authUIState.value = authUIState.value.copy(
            loginEmailError = emailResult.status,
            loginPasswordError = passwordResult.status
        )


        Log.d(TAG, " isSuccessful = ${passwordResult.status}")
        loginValidationsPassed.value = emailResult.status && passwordResult.status
    }

    private fun validateRecoveryUIDataWithRules() {
        val recoveryResult = Validator.validateRecoveryEmail(
            recoveryEmail = authUIState.value.recoveryEmail
        )

        authUIState.value = authUIState.value.copy(
            recoveryEmailError = recoveryResult.status
        )

        recoveryPassValidate.value = recoveryResult.status
    }

    private fun validateSignUpWithRules() {

        val nickNameResult = Validator.validateNickName(
            nickName = authUIState.value.signNickName
        )
        val emailResult = Validator.validateEmail(
            email = authUIState.value.signEmail
        )
        val passwordResult = Validator.validatePassword(
            password = authUIState.value.signPassword
        )
        val repeatPasswordResult = Validator.validatePassword(
            password = authUIState.value.signRepeatPassword
        )

        authUIState.value = authUIState.value.copy(
            signNickNameError = nickNameResult.status,
            signEmailError = emailResult.status,
            signPasswordError = passwordResult.status,
            signRepeatPasswordError = passwordResult.status,
        )

        signUpValidationsPassed.value = nickNameResult.status &&
                emailResult.status && passwordResult.status && repeatPasswordResult.status
    }

    private fun signUp() = viewModelScope.launch {
        try {
            if (!signUpValidationsPassed.value) {
                throw IllegalArgumentException("Email and password can not be empty!")
            }
            if (authUIState.value.signPassword != authUIState.value.signRepeatPassword) {
                throw IllegalArgumentException("Password do not match")
                Log.d(TAG, "Not match")
            }
            authInProgress.value = true
            authUIState.value = authUIState.value.copy(signError = null)
            repository.signUp(
                authUIState.value.signEmail,
                authUIState.value.signPassword
            ) { isSuccessful ->
                if (isSuccessful) {
                    navController.navigate(route = Graph.HOME) {
                        launchSingleTop = true
                        popUpTo(route = AuthScreen.SignUpScreen) {
                            inclusive = true
                        }
                    }
                } else {
                    authUIState.value = authUIState.value.copy(signError = "Check data")
                }
            }
        } catch (e: Exception) {
            authUIState.value = authUIState.value.copy(signError = e.localizedMessage)
            e.printStackTrace()
        } finally {
            authInProgress.value = false
        }
    }

    private fun login() = viewModelScope.launch {
        try {
            if (!loginValidationsPassed.value) {
                throw IllegalArgumentException("Email and password can not be empty!")
            }
            authInProgress.value = true
            authUIState.value = authUIState.value.copy(loginError = null)
            repository.login(
                authUIState.value.loginEmail,
                authUIState.value.loginPassword
            ) { isSuccessful ->
                if (isSuccessful) {
                    navController.navigate(route = Graph.HOME) {
                        launchSingleTop = true
                        popUpTo(route = AuthScreen.LoginScreen) {
                            inclusive = true
                        }
                    }
                } else {
                    authUIState.value = authUIState.value.copy(loginError = "Account not Exist")
                }
            }
        } catch (e: Exception) {
            authUIState.value = authUIState.value.copy(loginError = e.localizedMessage)
            e.printStackTrace()
        } finally {
            authInProgress.value = false
        }
    }

    private fun recoveryPass() = viewModelScope.launch {
        try {
            if (!recoveryPassValidate.value) {
                throw IllegalArgumentException("Email can not be empty!")
            }
            authInProgress.value = true
            authUIState.value = authUIState.value.copy(recoveryError = null)
            repository.recoveryPass(
                authUIState.value.recoveryEmail
            ) { isSuccessful ->
                if (isSuccessful) {
                    navController.navigate(route = AuthScreen.LoginScreen)
                }
            }
        } catch (e: Exception) {
            authUIState.value = authUIState.value.copy(recoveryError = e.localizedMessage)
            e.printStackTrace()
        } finally {
            authInProgress.value = false
        }
    }

    fun logout() {
        repository.logout()
    }


}
