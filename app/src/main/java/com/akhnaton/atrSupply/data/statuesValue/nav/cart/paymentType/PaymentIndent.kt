package com.akhnaton.atrSupply.data.statuesValue.nav.cart.paymentType

sealed class PaymentIndent {
    data object Checkout : PaymentIndent()
}