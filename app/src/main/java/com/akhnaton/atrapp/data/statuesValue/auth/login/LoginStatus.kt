package com.akhnaton.atrapp.data.statuesValue.auth.login

import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.data.model.auth.ErrorModel
import com.akhnaton.atrapp.data.model.auth.LoginModel

sealed class LoginStatus {

    data object Idle : LoginStatus()
    data object Loading : LoginStatus()
    data class Login(val data: BaseModel<LoginModel>) : LoginStatus()
    data class Error(val error: String?) : LoginStatus()

}