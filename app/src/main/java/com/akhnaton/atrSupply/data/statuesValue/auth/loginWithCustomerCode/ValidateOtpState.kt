package com.akhnaton.atrSupply.data.statuesValue.auth.loginWithCustomerCode

sealed class ValidateOtpState {
    object Idle : ValidateOtpState()
    object Loading : ValidateOtpState()
    data class Success(var state: Int,val message : String) : ValidateOtpState()
    data class Error(val error: String) : ValidateOtpState()
}