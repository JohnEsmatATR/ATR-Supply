package com.akhnaton.atrapp.ui.auth.customer_code.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrapp.data.statuesValue.auth.loginWithCustomerCode.ValidateOtpIntent
import com.akhnaton.atrapp.data.statuesValue.auth.loginWithCustomerCode.ValidateOtpState
import com.akhnaton.atrapp.domain.SentOtpRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class ValidateOtpViewModel() : ViewModel() {

    val state = MutableStateFlow<ValidateOtpState>(ValidateOtpState.Idle)
    val otpIntent = Channel<ValidateOtpIntent>(Channel.UNLIMITED)

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            otpIntent.consumeAsFlow().collect { intent ->
                when (intent) {
                    is ValidateOtpIntent.ValidateOtp ->  validateOtp(intent.code, intent.phone, intent.otp)

                }
            }
        }
    }

    private fun validateOtp(code: String, phone: String, otp: String) {
        viewModelScope.launch {
            state.value = ValidateOtpState.Loading
            try {
                val response = SentOtpRepository().validateOtp(code, phone, otp)
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    state.value = ValidateOtpState.Success(
                        body.status,
                        body.message
                    )
                } else {
                    state.value =
                        ValidateOtpState.Error("فشل التحقق من الكود: ${response.message()}")
                }
            } catch (e: Exception) {
                state.value = ValidateOtpState.Error(e.message ?: "خطأ غير معروف")
            }
        }
    }
}
