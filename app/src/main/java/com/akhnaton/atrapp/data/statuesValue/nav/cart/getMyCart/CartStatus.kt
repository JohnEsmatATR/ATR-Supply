package com.akhnaton.atrapp.data.statuesValue.nav.cart.getMyCart

import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.model.common.BaseModel

sealed class CartStatus {

    data object Idle : CartStatus()
    data object Loading : CartStatus()
    data class GetMyCart(val data: BaseModel<List<ProductModel>>) : CartStatus()
    data class Error(val error: String?) : CartStatus()
}