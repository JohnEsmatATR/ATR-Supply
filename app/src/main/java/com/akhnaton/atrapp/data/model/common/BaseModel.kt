package com.akhnaton.atrapp.data.model.common

data class BaseModel<T,T2>(
    val message: String = "",
    val status: String = "",
    var data: T?,
    var errors: T2?,
)