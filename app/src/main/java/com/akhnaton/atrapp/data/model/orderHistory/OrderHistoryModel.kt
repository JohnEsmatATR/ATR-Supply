package com.akhnaton.atrapp.data.model.orderHistory

data class OrderHistoryModel(
    val id: Int,
    val price: Double,
    val receivedCount: String,
    val returnCount: String,
    val address: String,
    val orderStatus: String,
    val orderStatusCode: Int
)
