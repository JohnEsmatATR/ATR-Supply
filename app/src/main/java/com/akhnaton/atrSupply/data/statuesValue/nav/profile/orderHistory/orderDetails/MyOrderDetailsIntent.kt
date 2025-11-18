package com.akhnaton.atrSupply.data.statuesValue.nav.profile.orderHistory.orderDetails

sealed class MyOrderDetailsIntent {

    data class GetMyOrderDetails(val orgSysId: String) : MyOrderDetailsIntent()
}