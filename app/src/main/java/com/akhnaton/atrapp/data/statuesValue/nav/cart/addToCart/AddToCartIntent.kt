package com.akhnaton.atrapp.data.statuesValue.nav.cart.addToCart


sealed class AddToCartIntent {
    data class AddProductToCart(
        val productId: Int?,
        val quantity: Int?,
    ) : AddToCartIntent()

}