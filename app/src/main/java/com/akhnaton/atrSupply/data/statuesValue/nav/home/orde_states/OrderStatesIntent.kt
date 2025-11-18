package com.akhnaton.atrSupply.data.statuesValue.nav.home.orde_states

sealed class OrderStatesIntent {
    data object GetOrderState : OrderStatesIntent()
}