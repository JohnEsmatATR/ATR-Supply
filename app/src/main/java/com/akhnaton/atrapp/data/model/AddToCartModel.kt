package com.akhnaton.atrapp.data.model

import com.google.gson.JsonElement

data class AddToCartModel(
    val message: String = "",
    val status: Int = 0,
    val data: JsonElement? = null
)