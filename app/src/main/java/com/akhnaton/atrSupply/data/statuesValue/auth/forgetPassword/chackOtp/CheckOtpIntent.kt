package com.akhnaton.atrSupply.data.statuesValue.auth.forgetPassword.chackOtp

sealed class CheckOtpIntent {

    data class SendOtp(
        val email: String,
        val otp: String,
    ) : CheckOtpIntent()
}