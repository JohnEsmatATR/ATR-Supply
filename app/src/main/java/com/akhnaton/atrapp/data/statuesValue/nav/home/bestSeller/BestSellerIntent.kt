package com.akhnaton.atrapp.data.statuesValue.nav.home.bestSeller

sealed class BestSellerIntent {

    data class GetBestSeller(val version: String) : BestSellerIntent()
}