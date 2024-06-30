package com.akhnaton.atrapp.domain

import com.akhnaton.atrapp.data.interfaces.cart.ICart
import com.akhnaton.atrapp.shared.RetrofitClient
import com.akhnaton.atrapp.shared.SharedPreferenceHelper


class CartRepository {
    private val retrofit = RetrofitClient.getInstance(ICart::class.java)

    suspend fun getMyCart(
    ) = retrofit.getMyCart(
        SharedPreferenceHelper.userToken,
    )
}