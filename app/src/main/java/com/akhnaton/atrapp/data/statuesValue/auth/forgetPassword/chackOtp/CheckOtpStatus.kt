package com.akhnaton.atrapp.data.statuesValue.auth.forgetPassword.chackOtp

import com.akhnaton.atrapp.data.model.common.BaseModel

sealed class CheckOtpStatus {

    data object Idle : CheckOtpStatus()
    data object Loading : CheckOtpStatus()
    data class CheckOtp(val data: BaseModel<List<String>>) : CheckOtpStatus()
    data class Error(val error: String?) : CheckOtpStatus()

}