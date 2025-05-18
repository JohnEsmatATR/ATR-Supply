package com.akhnaton.atrapp.domain

import com.akhnaton.atrapp.data.interfaces.cart.ICart
import com.akhnaton.atrapp.shared.RetrofitClient
import com.akhnaton.atrapp.shared.SharedPreferenceHelper


class CartRepository {
    private val retrofit = RetrofitClient.getInstance(ICart::class.java)

    suspend fun addProductToCart(
        productId: Int?,
        quantity: Int?,
    ) = retrofit.addToCart(
        productId,
        quantity,
    )

    suspend fun deleteProductFromCart(
        productId: Int?,
        quantity: Int?,
    ) = retrofit.deleteFromCart(
        productId,
        quantity,
    )


    suspend fun getMyCart(
    ) = retrofit.getMyCart()

    suspend fun checkout() = retrofit.checkout()
}