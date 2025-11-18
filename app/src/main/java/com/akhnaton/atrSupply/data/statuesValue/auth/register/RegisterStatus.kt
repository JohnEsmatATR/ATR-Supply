package com.akhnaton.atrSupply.data.statuesValue.auth.register

import com.akhnaton.atrSupply.data.model.common.BaseModel
import com.akhnaton.atrSupply.data.model.auth.LoginModel

sealed class RegisterStatus {

    data object Idle : RegisterStatus()
    data object Loading : RegisterStatus()
    data class Register(val data: BaseModel<List<LoginModel>>) : RegisterStatus()
    data class Error(val error: String?) : RegisterStatus()

}