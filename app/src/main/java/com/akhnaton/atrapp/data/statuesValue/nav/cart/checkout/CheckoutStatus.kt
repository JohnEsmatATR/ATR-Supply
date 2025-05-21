package com.akhnaton.atrapp.data.statuesValue.nav.cart.checkout

import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.model.common.BaseModel

sealed class CheckoutStatus {

    data object Idle : CheckoutStatus()
    data object Loading : CheckoutStatus()
    data class Checkout(val data: BaseModel<List<ProductModel>>) : CheckoutStatus()
    data class Error(val error: String?) : CheckoutStatus()
}