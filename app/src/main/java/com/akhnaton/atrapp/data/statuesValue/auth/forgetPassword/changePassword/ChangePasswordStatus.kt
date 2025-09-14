package com.akhnaton.atrapp.data.statuesValue.auth.forgetPassword.changePassword

import com.akhnaton.atrapp.data.model.common.BaseModel

sealed class ChangePasswordStatus {

    data object Idle : ChangePasswordStatus()
    data object Loading : ChangePasswordStatus()
    data class ChangePassword(val data: BaseModel<List<String>>) : ChangePasswordStatus()
    data class Error(val error: String?) : ChangePasswordStatus()

}