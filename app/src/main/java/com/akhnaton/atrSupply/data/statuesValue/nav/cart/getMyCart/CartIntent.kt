package com.akhnaton.atrSupply.data.statuesValue.nav.cart.getMyCart


sealed class CartIntent {

    data class GetMyCart(val language : String) : CartIntent()

}