package com.akhnaton.atrSupply.data.statuesValue.auth.login

import com.akhnaton.atrSupply.data.model.auth.LoginModel
import com.akhnaton.atrSupply.data.model.common.BaseModel

sealed class LoginStatus {

    data object Idle : LoginStatus()
    data object Loading : LoginStatus()
    data class Login(val data: BaseModel<LoginModel>) : LoginStatus()
    data class Error(val error: String?) : LoginStatus()

}