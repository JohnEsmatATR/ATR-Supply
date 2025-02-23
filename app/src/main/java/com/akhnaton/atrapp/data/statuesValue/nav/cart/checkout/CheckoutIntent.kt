package com.akhnaton.atrapp.data.statuesValue.nav.cart.checkout


sealed class CheckoutIntent {

    data object Checkout : CheckoutIntent()

}