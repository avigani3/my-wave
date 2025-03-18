package it.unimib.mywave.data

sealed class AuthUIEvent{

    data class LoginEmailChanged(val email:String): AuthUIEvent()
    data class LoginPasswordChanged(val password: String) : AuthUIEvent()
    object LoginButtonClicked : AuthUIEvent()

    data class RecoveryEmailChanged(val email:String): AuthUIEvent()
    object RecoveryButtonClicked : AuthUIEvent()


    data class SignNickNameChanged(val nickName: String) : AuthUIEvent()
    data class SignEmailChanged(val email: String) : AuthUIEvent()
    data class SignPasswordChange(val password: String) : AuthUIEvent()
    data class SignRepeatPasswordChange(val repeatPassword: String) : AuthUIEvent()
    object SignButtonClicked : AuthUIEvent()
}