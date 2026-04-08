package com.akhnaton.atrapp.data.model.auth

import com.google.gson.annotations.SerializedName

data class RegisterResponseData(
    @SerializedName("JWT")
    val jwt: String? = null
)
