package com.akhnaton.atrapp.data.model.orderHistory

data class OrderDetailsModel(
    val id: Int,
    val name: String,
    val quantity: Int,
    val price: Double,
    val discountPrice: Double
)