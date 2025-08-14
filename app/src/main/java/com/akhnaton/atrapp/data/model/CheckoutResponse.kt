package com.akhnaton.atrapp.data.model

import com.google.gson.annotations.SerializedName

data class CheckoutResponse(
    @SerializedName("order_numbers")
    val orderNumbers: String = ""
)
