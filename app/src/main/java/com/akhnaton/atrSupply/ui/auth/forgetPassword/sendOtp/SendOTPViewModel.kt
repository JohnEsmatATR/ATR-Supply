package com.akhnaton.atrSupply.ui.auth.forgetPassword.sendOtp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrSupply.data.statuesValue.auth.forgetPassword.sendOtp.SendOtpIntent
import com.akhnaton.atrSupply.data.statuesValue.auth.forgetPassword.sendOtp.SendOtpStatus
import com.akhnaton.atrSupply.domain.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class SendOTPViewModel : ViewModel() {

    val sendOtpIntent = Channel<SendOtpIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<SendOtpStatus>(SendOtpStatus.Idle)

    val state: StateFlow<SendOtpStatus> get() = _state

    init {
        makeHomeObserve()
    }

    private fun makeHomeObserve() {
        viewModelScope.launch {
            sendOtpIntent.consumeAsFlow().collect {
                when (it) {
                    is SendOtpIntent.SendOtp -> sendOtp(
                        it.email,
                    )
                }
            }
        }
    }


    private fun sendOtp(
        email: String,
    ) {
        viewModelScope.launch {
            _state.value = SendOtpStatus.Loading
            _state.value = try {
                val response = AuthRepository().sendOtp(
                    email,
                )
                if (response.code() == 200) {
                    SendOtpStatus.SendOtp(response.body()!!)
                } else {
                    SendOtpStatus.Error(response.body()!!.message)
                }

            } catch (e: Exception) {
                SendOtpStatus.Error(e.message)
            }

        }
    }


}

