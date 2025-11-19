package com.akhnaton.atrapp.data.statuesValue.nav.cart.addToCart

import com.akhnaton.atrapp.data.model.AddToCartResponse
import com.akhnaton.atrapp.data.model.common.BaseModel

sealed class AddToCartStatus {
    data object Idle : AddToCartStatus()
    data object Loading : AddToCartStatus()
    data class AddToCart(val data: BaseModel<AddToCartResponse>) : AddToCartStatus()
    data class Error(val error: String?) : AddToCartStatus()
}