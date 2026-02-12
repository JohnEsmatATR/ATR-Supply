package com.akhnaton.atrapp.ui.auth.customer_code.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrapp.data.statuesValue.auth.loginWithCustomerCode.RegisterFromLineIntent
import com.akhnaton.atrapp.data.statuesValue.auth.loginWithCustomerCode.RegisterFromLineState
import com.akhnaton.atrapp.domain.SentOtpRepository
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class RegisterFromLineViewModel : ViewModel() {

    val registerIntent = Channel<RegisterFromLineIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<RegisterFromLineState>(RegisterFromLineState.Idle)
    val state: StateFlow<RegisterFromLineState> = _state

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            registerIntent.consumeAsFlow().collect { intent ->
                when (intent) {
                    is RegisterFromLineIntent.Register -> registerUser(
                        intent.code,
                        intent.phone,
                        intent.firstName,
                        intent.lastName,
                        intent.email,
                        intent.fbToken,
                        intent.password
                    )
                }
            }
        }
    }

    private fun registerUser(
        code: String,
        phone: String,
        firstName: String,
        lastName: String,
        email: String,
        fbToken: String,
        password: String
    ) {
        viewModelScope.launch {
            _state.value = RegisterFromLineState.Loading
            try {
                val response = SentOtpRepository().register(
                    code,
                    phone,
                    firstName,
                    lastName,
                    email,
                    fbToken,
                    password
                )
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    val token = body.data?.JWT

                    if (!token.isNullOrEmpty()) {
                        SharedPreferenceHelper.userToken = token
                        Log.d("JWT", "Token saved: $token")
                    } else {
                        Log.w("JWT", "No token returned from API")
                    }

                    _state.value = RegisterFromLineState.Success(
                        body.status,
                        body.message
                    )
                } else {
                    _state.value = RegisterFromLineState.Error(
                        "فشل التسجيل: ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                _state.value = RegisterFromLineState.Error(
                    e.message ?: "حصل خطأ غير معروف"
                )
            }
        }
    }
}