package com.akhnaton.atrSupply.data.statuesValue.auth.forgetPassword.sendOtp

import com.akhnaton.atrSupply.data.model.auth.forgetPassword.SendOtpModel
import com.akhnaton.atrSupply.data.model.common.BaseModel

sealed class SendOtpStatus {

    data object Idle : SendOtpStatus()
    data object Loading : SendOtpStatus()
    data class SendOtp(val data: BaseModel<SendOtpModel>) : SendOtpStatus()
    data class Error(val error: String?) : SendOtpStatus()

}