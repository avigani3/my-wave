package it.unimib.mywave.data

data class AuthUIState(
    //LOGIN
    var loginEmail  :String = "",
    var loginPassword  :String = "",

    var loginEmailError :Boolean = false,
    var loginPasswordError : Boolean = false,
    var loginError : String? = null,

    //RECOVERY
    var recoveryEmail  :String = "",
    var recoveryEmailError :Boolean = false,
    var recoveryError : String? = null,


    //SIGNUP
    var signNickName  :String = "",
    var signEmail  :String = "",
    var signPassword  :String = "",
    var signRepeatPassword  :String = "",

    var signNickNameError : Boolean = false,
    var signEmailError :Boolean = false,
    var signPasswordError : Boolean = false,
    var signRepeatPasswordError : Boolean = false,
    var signError : String? = null
)