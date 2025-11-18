package com.akhnaton.atrSupply.data.statuesValue.nav.profile.orderHistory.orderDetails

import com.akhnaton.atrSupply.data.model.common.BaseModel
import com.akhnaton.atrSupply.data.model.orderHistory.OrderDetailsModel

sealed class MyOrderDetailsStatus {

    data object Idle : MyOrderDetailsStatus()
    data object Loading : MyOrderDetailsStatus()
    data class GetMyOrderDetails(val data: BaseModel<List<OrderDetailsModel>>) : MyOrderDetailsStatus()
    data class Error(val error: String?) : MyOrderDetailsStatus()
}