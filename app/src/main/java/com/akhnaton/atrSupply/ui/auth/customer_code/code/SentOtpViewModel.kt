package com.akhnaton.atrSupply.ui.auth.customer_code.code

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrSupply.data.statuesValue.auth.loginWithCustomerCode.SentOtpIntent
import com.akhnaton.atrSupply.data.statuesValue.auth.loginWithCustomerCode.SentOtpState
import com.akhnaton.atrSupply.domain.SentOtpRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class SentOtpViewModel() : ViewModel() {

    val otpIntent = Channel<SentOtpIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<SentOtpState>(SentOtpState.Idle)
    val state: StateFlow<SentOtpState> = _state

    init {
        handleIntents()
    }

    private fun handleIntents() {
        viewModelScope.launch {
            otpIntent.consumeAsFlow().collect { intent ->
                when (intent) {
                    is SentOtpIntent.SentCode -> sendOtp(intent.code, intent.phone)
                }
            }
        }
    }

    private fun sendOtp(code: String, phone: String) {
        viewModelScope.launch {
            _state.value = SentOtpState.Loading
            try {
                val response = SentOtpRepository().sentOtp(code, phone)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == 200) {
                        _state.value = SentOtpState.Success(body.status,body.data?.toString() ?: "OTP Sent")
                    } else {
                        _state.value = SentOtpState.Error(body?.message ?: "Unknown error")
                    }
                } else {
                    _state.value = SentOtpState.Error("Error Code: ${response.code()}")
                }
            } catch (e: Exception) {
                _state.value = SentOtpState.Error(e.message ?: "Exception occurred")
            }
        }
    }
}
