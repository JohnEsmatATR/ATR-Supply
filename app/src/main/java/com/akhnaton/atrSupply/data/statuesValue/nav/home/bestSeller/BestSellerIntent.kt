package com.akhnaton.atrSupply.data.statuesValue.nav.home.bestSeller

sealed class BestSellerIntent {

    data class GetBestSeller(val bestSeller: Int) : BestSellerIntent()
}