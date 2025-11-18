package com.akhnaton.atrSupply.data.model.common

import com.akhnaton.atrSupply.data.model.PaginationModel

data class BaseModel<T>(
    val message: String = "",
    val status: Int = 0,
    val data: T? = null,
    val pagination: PaginationModel? = null,
    val total: String? = null
)