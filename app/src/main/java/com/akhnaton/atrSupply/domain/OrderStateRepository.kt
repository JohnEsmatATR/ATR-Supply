package com.akhnaton.atrSupply.domain

import com.akhnaton.atrSupply.data.interfaces.order_states.OrderStates
import com.akhnaton.atrSupply.shared.RetrofitClient

class OrderStateRepository {
    val retrofit =  RetrofitClient.getInstance(OrderStates::class.java)

    suspend fun getOrderStates(id : String) = retrofit.getOrderStates(id)
}