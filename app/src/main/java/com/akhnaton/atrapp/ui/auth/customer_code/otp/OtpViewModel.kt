package com.akhnaton.atrapp.ui.auth.customer_code.otp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OtpViewModel : ViewModel() {

    private val _isOtpComplete = MutableLiveData(false)
    val isOtpComplete: LiveData<Boolean> get() = _isOtpComplete

    fun checkOtp(inputs: List<String>) {
        _isOtpComplete.value = inputs.all { it.length == 1 }
    }
}