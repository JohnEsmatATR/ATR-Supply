package com.akhnaton.atrapp.data.statuesValue.nav.home.products

sealed class ProductsIntent {

    data class GetProducts(
        val version: String,
        val categoryId: Int,
    ) : ProductsIntent()
}