package com.akhnaton.atrapp.data.statuesValue.nav.profile.orderHistory.orderDetails

import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.data.model.orderHistory.OrderDetailsModel
import com.akhnaton.atrapp.data.model.orderHistory.OrderHistoryModel

sealed class MyOrderDetailsStatus {

    data object Idle : MyOrderDetailsStatus()
    data object Loading : MyOrderDetailsStatus()
    data class GetMyOrderDetails(val data: BaseModel<List<OrderDetailsModel>>) : MyOrderDetailsStatus()
    data class Error(val error: String?) : MyOrderDetailsStatus()
}