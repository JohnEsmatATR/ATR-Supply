package com.akhnaton.atrSupply.ui.nav.home.panner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrSupply.data.statuesValue.nav.panner.PannerIntent
import com.akhnaton.atrSupply.data.statuesValue.nav.panner.PannerState
import com.akhnaton.atrSupply.domain.PannerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PannerViewModel(
    private val repository: PannerRepository = PannerRepository()
) : ViewModel() {

    private val _state = MutableStateFlow<PannerState>(PannerState.Loading)
    val state: StateFlow<PannerState> = _state

    fun handleIntent(intent: PannerIntent) {
        when (intent) {
            is PannerIntent.getPanners -> {
                getPanners()
            }
        }
    }

    private fun getPanners() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = PannerState.Loading
            try {
                val response = repository.getPanner()   // ده بيكون BaseModel<PannerResponse>
                _state.value = PannerState.Success(response)
            } catch (e: Exception) {
                _state.value = PannerState.Error(e.localizedMessage ?: "حدث خطأ غير متوقع")
            }
        }
    }


}