package com.akhnaton.atrapp.ui.auth.forgetPassword.checkOtp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrapp.data.statuesValue.auth.forgetPassword.chackOtp.CheckOtpIntent
import com.akhnaton.atrapp.data.statuesValue.auth.forgetPassword.chackOtp.CheckOtpStatus
import com.akhnaton.atrapp.domain.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class CheckOTPViewModel : ViewModel() {

    val sendOtpIntent = Channel<CheckOtpIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<CheckOtpStatus>(CheckOtpStatus.Idle)

    val state: StateFlow<CheckOtpStatus> get() = _state

    init {
        makeHomeObserve()
    }

    private fun makeHomeObserve() {
        viewModelScope.launch {
            sendOtpIntent.consumeAsFlow().collect {
                when (it) {
                    is CheckOtpIntent.SendOtp -> sendOtp(
                        it.email,
                        it.otp,
                    )
                }
            }
        }
    }


    private fun sendOtp(
        email: String,
        otp: String,
    ) {
        viewModelScope.launch {
            _state.value = CheckOtpStatus.Loading
            _state.value = try {
                val response = AuthRepository().checkOtp(
                    email,
                    otp,
                )
                if (response.code() == 200) {
                    CheckOtpStatus.CheckOtp(response.body()!!)
                } else {
                    CheckOtpStatus.Error(response.body()!!.message)
                }

            } catch (e: Exception) {
                CheckOtpStatus.Error(e.message)
            }

        }
    }


}

