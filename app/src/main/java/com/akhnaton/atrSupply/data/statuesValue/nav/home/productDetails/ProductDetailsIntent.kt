package com.akhnaton.atrSupply.data.statuesValue.nav.home.productDetails

sealed class ProductDetailsIntent {

    data class GetProductDetails(val bestSeller: Int,val categories: String) : ProductDetailsIntent()
}