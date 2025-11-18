package com.akhnaton.atrSupply.data.statuesValue.nav.cart.addToCart


sealed class AddToCartIntent {
    data class AddProductToCart(
        val productId: Int?,
        val quantity: Int?,
        val category: String
    ) : AddToCartIntent()

    data class deleteProductToCart(
        val productId: Int?,
        val quantity: Int?,
    ) : AddToCartIntent()



}