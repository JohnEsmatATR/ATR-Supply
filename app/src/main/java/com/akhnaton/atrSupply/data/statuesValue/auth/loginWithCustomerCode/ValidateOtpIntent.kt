package com.akhnaton.atrSupply.data.statuesValue.auth.loginWithCustomerCode

sealed class ValidateOtpIntent {
    data class ValidateOtp(val code : String, val phone : String , val otp : String):ValidateOtpIntent()
}