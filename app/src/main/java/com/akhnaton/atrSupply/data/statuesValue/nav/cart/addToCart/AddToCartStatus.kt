package com.akhnaton.atrSupply.data.statuesValue.nav.cart.addToCart

import com.akhnaton.atrSupply.data.model.AddToCartResponse
import com.akhnaton.atrSupply.data.model.common.BaseModel

sealed class AddToCartStatus {
    data object Idle : AddToCartStatus()
    data object Loading : AddToCartStatus()
    data class AddToCart(val data: BaseModel<AddToCartResponse>) : AddToCartStatus()
    data class Error(val error: String?) : AddToCartStatus()
}