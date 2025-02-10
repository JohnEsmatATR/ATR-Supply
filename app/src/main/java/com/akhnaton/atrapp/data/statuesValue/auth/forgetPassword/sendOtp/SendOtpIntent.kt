package com.akhnaton.atrapp.data.statuesValue.auth.forgetPassword.sendOtp

sealed class SendOtpIntent {

    data class SendOtp(
        val email: String,
    ) : SendOtpIntent()
}