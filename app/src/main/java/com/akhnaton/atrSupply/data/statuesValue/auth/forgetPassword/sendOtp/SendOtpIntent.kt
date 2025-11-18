package com.akhnaton.atrSupply.data.statuesValue.auth.forgetPassword.sendOtp

sealed class SendOtpIntent {

    data class SendOtp(
        val email: String,
    ) : SendOtpIntent()
}