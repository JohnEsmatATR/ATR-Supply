package com.akhnaton.atrapp.data.statuesValue.nav.panner

sealed class PannerIntent {
    data object getPanners: PannerIntent()
}