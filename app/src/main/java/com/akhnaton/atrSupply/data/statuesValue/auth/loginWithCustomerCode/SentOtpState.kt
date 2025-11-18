package com.akhnaton.atrSupply.data.statuesValue.auth.loginWithCustomerCode

sealed class SentOtpState {
    object Idle : SentOtpState()
    object Loading : SentOtpState()
    data class Success(val state: Int,val message : String) : SentOtpState()
    data class Error(val error: String) : SentOtpState()
}
