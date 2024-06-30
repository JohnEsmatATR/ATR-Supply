package com.akhnaton.atrapp.data.statuesValue.nav.cart.getMyCart


sealed class CartIntent {

    data object GetMyCart : CartIntent()

}