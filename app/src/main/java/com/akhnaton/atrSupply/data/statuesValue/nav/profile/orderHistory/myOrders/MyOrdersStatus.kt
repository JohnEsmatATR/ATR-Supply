package com.akhnaton.atrSupply.data.statuesValue.nav.profile.orderHistory.myOrders

import com.akhnaton.atrSupply.data.model.common.BaseModel
import com.akhnaton.atrSupply.data.model.orderHistory.OrderHistoryModel

sealed class MyOrdersStatus {

    data object Idle : MyOrdersStatus()
    data object Loading : MyOrdersStatus()
    data class GetMyOrders(val data: BaseModel<List<OrderHistoryModel>>) : MyOrdersStatus()
    data class Error(val error: String?) : MyOrdersStatus()
}