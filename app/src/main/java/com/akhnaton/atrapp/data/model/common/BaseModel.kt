package com.akhnaton.atrapp.data.model.common

data class BaseModel<T>(
    val message: String = "",
    val status: Int = 0,
    var data: T?,
)