package com.akhnaton.atrapp.data.statuesValue.auth.otp

import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.data.model.common.UserModel
import com.akhnaton.atrapp.data.model.auth.ErrorModel

sealed class OtpStatus {

    object Idle : OtpStatus()
    object Loading : OtpStatus()
    data class Otp(val data: BaseModel<UserModel>) : OtpStatus()
    data class Error(val error: String?) : OtpStatus()

}