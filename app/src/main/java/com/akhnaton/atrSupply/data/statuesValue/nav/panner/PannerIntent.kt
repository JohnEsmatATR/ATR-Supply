package com.akhnaton.atrSupply.data.statuesValue.nav.panner

sealed class PannerIntent {
    data object getPanners: PannerIntent()
}