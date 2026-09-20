package com.akhnaton.atrapp.data.statuesValue.nav.profile.orderHistory.orderDetails

import MyOrderDetailsRes
import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.data.model.orderHistory.OrderDetailsModel

sealed class MyOrderDetailsStatus {

    data object Idle : MyOrderDetailsStatus()
    data object Loading : MyOrderDetailsStatus()
    data class GetMyOrderDetails(val ahmed: MyOrderDetailsRes) : MyOrderDetailsStatus()
    data class Error(val error: String?) : MyOrderDetailsStatus()
}