package com.akhnaton.atrapp.data.statuesValue.nav.cart.checkout


sealed class CheckoutIntent {

    data class Checkout(val paymentId : Int,val category : String) : CheckoutIntent()

}