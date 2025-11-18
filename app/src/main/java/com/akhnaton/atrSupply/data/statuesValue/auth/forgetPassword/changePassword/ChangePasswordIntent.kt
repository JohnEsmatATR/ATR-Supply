package com.akhnaton.atrSupply.data.statuesValue.auth.forgetPassword.changePassword

sealed class ChangePasswordIntent {

    data class SendOtp(
        val email: String,
        val otp: String,
        val password: String,
    ) : ChangePasswordIntent()
}