package com.akhnaton.atrapp.data.statuesValue.nav.cart.paymentType

sealed class PaymentIndent {
    data object Checkout : PaymentIndent()
}