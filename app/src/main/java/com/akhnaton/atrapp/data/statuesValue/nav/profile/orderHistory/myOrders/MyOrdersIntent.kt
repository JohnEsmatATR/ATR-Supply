package com.akhnaton.atrapp.data.statuesValue.nav.profile.orderHistory.myOrders

sealed class MyOrdersIntent {

    data object GetMyOrders : MyOrdersIntent()
}