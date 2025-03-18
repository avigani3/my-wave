package it.unimib.mywave.data.validator

object Validator {
    fun validateNickName(nickName:String) : ValidationResult {
        return  ValidationResult(
            (!nickName.isNullOrEmpty())
        )
    }
    fun validateEmail(email:String) : ValidationResult {
        return  ValidationResult(
            (!email.isNullOrEmpty())
        )
    }
    fun validatePassword(password:String) : ValidationResult {
        return  ValidationResult(
            (!password.isNullOrEmpty() && password.length >=4)
        )
    }
    fun validateRecoveryEmail(recoveryEmail:String) : ValidationResult {
        return  ValidationResult(
            (!recoveryEmail.isNullOrEmpty())
        )
    }

}

data class ValidationResult(
    val status :Boolean = false
)