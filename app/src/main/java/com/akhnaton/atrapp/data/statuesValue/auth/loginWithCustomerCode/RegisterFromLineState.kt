package com.akhnaton.atrapp.data.statuesValue.auth.loginWithCustomerCode

sealed class RegisterFromLineState {
    object Idle : RegisterFromLineState()
    object Loading : RegisterFromLineState()
    data class Success(val state: Int,val message : String) : RegisterFromLineState()
    data class Error(val error: String) : RegisterFromLineState()
}