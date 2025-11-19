package com.akhnaton.atrapp.data.statuesValue.auth.forgetPassword.chackOtp

sealed class CheckOtpIntent {

    data class SendOtp(
        val email: String,
        val otp: String,
    ) : CheckOtpIntent()
}