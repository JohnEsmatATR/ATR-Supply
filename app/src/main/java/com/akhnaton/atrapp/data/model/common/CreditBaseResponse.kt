package com.akhnaton.atrapp.data.model.common

import com.google.gson.annotations.SerializedName

data class CreditBaseResponse(
    @SerializedName("status") val status: Int? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val creditData: CreditDataModel? = null
)
