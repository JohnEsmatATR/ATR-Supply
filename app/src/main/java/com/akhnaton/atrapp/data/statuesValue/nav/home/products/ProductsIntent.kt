package com.akhnaton.atrapp.data.statuesValue.nav.home.products

sealed class ProductsIntent {

    data class GetProducts(
        val categoryId: Int,
        val page: Int,
        val categoriesName: String
    ) : ProductsIntent()
}