package com.akhnaton.atrSupply.domain

import com.akhnaton.atrSupply.data.interfaces.orderHistory.IOrders
import com.akhnaton.atrSupply.shared.RetrofitClient

class OrdersRepository {
    private val retrofit = RetrofitClient.getInstance(IOrders::class.java)

    suspend fun getMyOrders(
    ) = retrofit.getMyOrders()

    suspend fun getOrderDetails(
        orgSysId: String,
    ) = retrofit.getMyOrderDetails(
        orgSysId,
    )
}