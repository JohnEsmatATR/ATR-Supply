package com.akhnaton.atrapp.data.statuesValue.auth.register

import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.data.model.auth.LoginModel

sealed class RegisterStatus {

    data object Idle : RegisterStatus()
    data object Loading : RegisterStatus()
    data class Register(val data: BaseModel<List<LoginModel>>) : RegisterStatus()
    data class Error(val error: String?) : RegisterStatus()

}