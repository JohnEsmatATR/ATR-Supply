package com.akhnaton.atrapp.data.statuesValue.auth.loginWithCustomerCode

sealed class SentOtpIntent {
    data class SentCode(val code: String, val phone: String, val email: String) : SentOtpIntent()
}