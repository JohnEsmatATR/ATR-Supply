package com.akhnaton.atrapp.data.model.common

import com.akhnaton.atrapp.data.model.PaginationModel

data class BaseModel<T>(
    val message: String = "",
    val status: Int = 0,
    var data: T?,
    var pagination: PaginationModel,
)