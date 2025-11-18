package com.akhnaton.atrSupply.data.statuesValue.nav.cart.getMyCart

import com.akhnaton.atrSupply.data.model.CartApiResponse

sealed class CartStatus {
    data object Idle : CartStatus()
    data object Loading : CartStatus()
    data class GetMyCart(val data: CartApiResponse) : CartStatus()
    data class Error(val error: String?) : CartStatus()
}
