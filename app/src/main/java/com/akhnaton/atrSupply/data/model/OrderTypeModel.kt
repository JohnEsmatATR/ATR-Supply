package com.akhnaton.atrSupply.data.model

data class OrderTypeModel(
    val order_type: String,
    val order_type_index : String,
    val categories: List<CategoriesModel>
)

