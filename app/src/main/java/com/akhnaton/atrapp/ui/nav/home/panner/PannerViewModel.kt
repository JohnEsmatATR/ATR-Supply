package com.akhnaton.atrapp.ui.nav.home.panner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrapp.data.statuesValue.nav.panner.PannerIntent
import com.akhnaton.atrapp.data.statuesValue.nav.panner.PannerState
import com.akhnaton.atrapp.domain.PannerRepository
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
                val response = repository.getPanner()
                val banners = response.data?.banners ?: emptyList()
                _state.value = PannerState.Success(banners)
            } catch (e: Exception) {
                _state.value = PannerState.Error(e.localizedMessage ?: "حدث خطأ غير متوقع")
            }
        }
    }
}