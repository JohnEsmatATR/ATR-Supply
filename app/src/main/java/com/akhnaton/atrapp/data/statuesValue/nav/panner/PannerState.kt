package com.akhnaton.atrapp.data.statuesValue.nav.panner

sealed class PannerState {
    data object Loading : PannerState()

    data class Success(val banners: List<String>) : PannerState()

    data class Error(val message: String) : PannerState()
}
