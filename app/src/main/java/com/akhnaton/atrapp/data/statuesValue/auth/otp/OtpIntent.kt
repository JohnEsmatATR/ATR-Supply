package com.akhnaton.atrapp.data.statuesValue.auth.otp

sealed class OtpIntent {

    data class Otp(
        val otp: String,
        val phone: String,
    ) : OtpIntent()
}