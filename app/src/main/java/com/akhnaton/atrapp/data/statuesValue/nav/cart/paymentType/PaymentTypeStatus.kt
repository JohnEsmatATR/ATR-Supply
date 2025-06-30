package com.akhnaton.atrapp.data.statuesValue.nav.cart.paymentType

import com.akhnaton.atrapp.data.model.PaymentModel
import com.akhnaton.atrapp.data.model.common.BaseModel

sealed class PaymentTypeStatus {

    data object Idle : PaymentTypeStatus()
    data object Loading : PaymentTypeStatus()
    data class Checkout(val data: BaseModel<List<PaymentModel>>) : PaymentTypeStatus()
    data class Error(val error: String?) : PaymentTypeStatus()
}