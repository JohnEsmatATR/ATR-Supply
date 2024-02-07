package com.akhnaton.atrapp.data.statuesValue.auth.login

import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.data.model.auth.ErrorModel

sealed class LoginStatus {

    object Idle : LoginStatus()
    object Loading : LoginStatus()
    data class Login(val data: BaseModel<String, ErrorModel>) : LoginStatus()
    data class Error(val error: String?) : LoginStatus()

}