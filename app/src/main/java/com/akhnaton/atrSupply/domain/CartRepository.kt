package com.akhnaton.atrSupply.domain

import com.akhnaton.atrSupply.data.interfaces.cart.ICart
import com.akhnaton.atrSupply.shared.RetrofitClient


class CartRepository {
    private val retrofit = RetrofitClient.getInstance(ICart::class.java)

    suspend fun addProductToCart(
        productId: Int?,
        quantity: Int?,
        category: String
    ) = retrofit.addToCart(
        productId,
        quantity,
        category
    )

    suspend fun deleteProductFromCart(
        productId: Int?,
        quantity: Int?,
    ) = retrofit.deleteFromCart(
        productId,
        quantity,
    )


    suspend fun getMyCart(language : String
    ) = retrofit.getMyCart(language)

    suspend fun checkout(paymentId : Int ,  category: String) = retrofit.checkout(paymentId,category)

    suspend fun getPaymentType() = retrofit.getPaymentType()
}