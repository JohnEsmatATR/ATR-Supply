package com.akhnaton.atrapp.data.statuesValue.nav.cart.getMyCart

import com.akhnaton.atrapp.data.model.CartResponse
import com.akhnaton.atrapp.data.model.common.BaseModel

sealed class CartStatus {

    data object Idle : CartStatus()
    data object Loading : CartStatus()
    data class GetMyCart(val data: BaseModel<List<CartResponse>>) : CartStatus()
    data class Error(val error: String?) : CartStatus()
}