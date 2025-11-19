package com.akhnaton.atrapp.data.model

import com.google.gson.annotations.SerializedName

data class PaymentModel(
    @SerializedName("payment_name")
    val paymentName : String?,

    @SerializedName("payment_id")
    val paymentId : Int
)