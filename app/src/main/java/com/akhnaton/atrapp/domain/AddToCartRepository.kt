package com.akhnaton.atrapp.domain

import com.akhnaton.atrapp.data.interfaces.cart.IAddToCart
import com.akhnaton.atrapp.shared.RetrofitClient
import com.akhnaton.atrapp.shared.SharedPreferenceHelper


class AddToCartRepository {
    private val retrofit = RetrofitClient.getInstance(IAddToCart::class.java)

    suspend fun addProductToCart(
        productId: Int?,
        quantity: Int?,
    ) = retrofit.addToCart(
        SharedPreferenceHelper.userToken,
        productId,
        quantity,
    )

}