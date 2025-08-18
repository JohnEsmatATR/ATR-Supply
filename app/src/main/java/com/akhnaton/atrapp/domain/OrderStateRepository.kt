package com.akhnaton.atrapp.domain

import com.akhnaton.atrapp.data.interfaces.order_states.OrderStates
import com.akhnaton.atrapp.shared.RetrofitClient

class OrderStateRepository {
    val retrofit =  RetrofitClient.getInstance(OrderStates::class.java)

    suspend fun getOrderStates(id : String) = retrofit.getOrderStates(id)
}