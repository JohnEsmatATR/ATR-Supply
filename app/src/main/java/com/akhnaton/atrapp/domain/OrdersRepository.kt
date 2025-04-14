package com.akhnaton.atrapp.domain

import com.akhnaton.atrapp.data.interfaces.orderHistory.IOrders
import com.akhnaton.atrapp.data.interfaces.reviews.IReviews
import com.akhnaton.atrapp.shared.RetrofitClient

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