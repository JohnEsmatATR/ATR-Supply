package com.akhnaton.atrSupply.data.statuesValue.appSetting

import com.akhnaton.atrSupply.data.model.AppSettingResponse
import com.akhnaton.atrSupply.data.model.common.BaseModel

sealed class AppSettingState {
    object Idle : AppSettingState()
    object Loading : AppSettingState()
    data class Success(val data: BaseModel<AppSettingResponse>) : AppSettingState()
    data class Error(val error: String) : AppSettingState()
}

