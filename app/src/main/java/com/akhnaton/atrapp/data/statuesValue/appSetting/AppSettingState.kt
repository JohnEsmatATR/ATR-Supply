package com.akhnaton.atrapp.data.statuesValue.appSetting

import com.akhnaton.atrapp.data.model.AppSettingResponse
import com.akhnaton.atrapp.data.model.common.BaseModel

sealed class AppSettingState {
    object Idle : AppSettingState()
    object Loading : AppSettingState()
    data class Success(val data: BaseModel<AppSettingResponse>) : AppSettingState()
    data class Error(val error: String) : AppSettingState()
}

