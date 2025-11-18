package com.akhnaton.atrSupply.data.statuesValue.nav.cart.checkout

import com.akhnaton.atrSupply.data.model.CheckoutResponse
import com.akhnaton.atrSupply.data.model.common.BaseModel

sealed class CheckoutStatus {

    data object Idle : CheckoutStatus()
    data object Loading : CheckoutStatus()
    data class Checkout(val data: BaseModel<CheckoutResponse>) : CheckoutStatus()
    data class Error(val error: String?) : CheckoutStatus()
}