package com.akhnaton.atrapp.data.statuesValue.auth.forgetPassword.sendOtp

import com.akhnaton.atrapp.data.model.auth.forgetPassword.SendOtpModel
import com.akhnaton.atrapp.data.model.common.BaseModel

sealed class SendOtpStatus {

    data object Idle : SendOtpStatus()
    data object Loading : SendOtpStatus()
    data class SendOtp(val data: BaseModel<SendOtpModel>) : SendOtpStatus()
    data class Error(val error: String?) : SendOtpStatus()

}