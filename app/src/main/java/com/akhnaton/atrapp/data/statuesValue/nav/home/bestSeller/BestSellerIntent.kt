package com.akhnaton.atrapp.data.statuesValue.nav.home.bestSeller

sealed class BestSellerIntent {

    data object GetBestSeller : BestSellerIntent()
}