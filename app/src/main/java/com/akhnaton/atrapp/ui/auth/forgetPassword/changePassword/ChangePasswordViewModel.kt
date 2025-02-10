package com.akhnaton.atrapp.ui.auth.forgetPassword.changePassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrapp.data.statuesValue.auth.forgetPassword.changePassword.ChangePasswordIntent
import com.akhnaton.atrapp.data.statuesValue.auth.forgetPassword.changePassword.ChangePasswordStatus
import com.akhnaton.atrapp.data.statuesValue.auth.forgetPassword.sendOtp.SendOtpIntent
import com.akhnaton.atrapp.data.statuesValue.auth.forgetPassword.sendOtp.SendOtpStatus
import com.akhnaton.atrapp.domain.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class ChangePasswordViewModel : ViewModel() {

    val sendOtpIntent = Channel<ChangePasswordIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<ChangePasswordStatus>(ChangePasswordStatus.Idle)

    val state: StateFlow<ChangePasswordStatus> get() = _state

    init {
        makeHomeObserve()
    }

    private fun makeHomeObserve() {
        viewModelScope.launch {
            sendOtpIntent.consumeAsFlow().collect {
                when (it) {
                    is ChangePasswordIntent.SendOtp -> sendOtp(
                        it.email,
                        it.otp,
                        it.password,
                    )
                }
            }
        }
    }


    private fun sendOtp(
        email: String,
        otp: String,
        password: String,
    ) {
        viewModelScope.launch {
            _state.value = ChangePasswordStatus.Loading
            _state.value = try {
                val response = AuthRepository().changePassword(
                    email,
                    otp,
                    password,
                )
                if (response.code() == 200) {
                    ChangePasswordStatus.ChangePassword(response.body()!!)
                } else {
                    ChangePasswordStatus.Error(response.body()!!.message)
                }

            } catch (e: Exception) {
                ChangePasswordStatus.Error(e.message)
            }

        }
    }


}