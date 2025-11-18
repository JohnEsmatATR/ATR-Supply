package com.akhnaton.atrSupply.data.statuesValue.auth.forgetPassword.changePassword

import com.akhnaton.atrSupply.data.model.common.BaseModel

sealed class ChangePasswordStatus {

    data object Idle : ChangePasswordStatus()
    data object Loading : ChangePasswordStatus()
    data class ChangePassword(val data: BaseModel<List<String>>) : ChangePasswordStatus()
    data class Error(val error: String?) : ChangePasswordStatus()

}