package com.akhnaton.atrSupply.ui.auth.customer_code.code

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrSupply.data.statuesValue.appSetting.AppSettingIntent
import com.akhnaton.atrSupply.data.statuesValue.appSetting.AppSettingState
import com.akhnaton.atrSupply.domain.SentOtpRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class AppSettingViewModel : ViewModel() {
    private val _state = MutableStateFlow<AppSettingState>(AppSettingState.Idle)
    val state: StateFlow<AppSettingState> = _state

    val intent = Channel<AppSettingIntent>(Channel.UNLIMITED)

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect { appIntent ->
                when (appIntent) {
                    is AppSettingIntent.GetAppSetting -> {
                        getAppSetting()
                    }
                }
            }
        }
    }

    private fun getAppSetting() {
        viewModelScope.launch {
            _state.value = AppSettingState.Loading
            try {
                val response = SentOtpRepository().appSetting()
                if (response.isSuccessful && response.body() != null) {
                    _state.value = AppSettingState.Success(response.body()!!)
                } else {
                    _state.value = AppSettingState.Error("فشل تحميل البيانات: ${response.message()}")
                }
            } catch (e: Exception) {
                _state.value = AppSettingState.Error(e.message ?: "خطأ غير معروف")
            }
        }
    }
}