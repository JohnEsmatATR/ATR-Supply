package com.akhnaton.atrSupply.data.model.orderHistory

import com.google.gson.annotations.SerializedName

data class OrderDetailsModel(
    val id: Int,
    @SerializedName("TITLE")
    val name: String,
    @SerializedName("DESCRIPTION")
    val description: String,
    @SerializedName("QUANTITY")
    val quantity: Int,
    @SerializedName("ITEM_TOTAL_PRICE")
    val price: Double,
    val discountPrice: Double,
    @SerializedName("IMAGE_URL")
    val img: String,
)