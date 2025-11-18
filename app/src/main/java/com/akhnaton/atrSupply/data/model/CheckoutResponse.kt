package com.akhnaton.atrSupply.data.model

import com.google.gson.annotations.SerializedName

data class CheckoutResponse(
    @SerializedName("order_numbers")
    val orderNumbers: List<String>,
    val msg : String
)

