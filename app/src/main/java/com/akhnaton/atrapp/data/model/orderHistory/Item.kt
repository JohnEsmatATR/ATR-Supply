package com.akhnaton.atrapp.data.model.orderHistory

data class Item(
    val DESCRIPTION: String,
    val QUANTITY: Int,
    val TAX_VALUE: Int,
    val TITLE: String,
    val TOTAL_TAX: Int,
    val TOTAL_UNIT_PRICE_WITHOUT_TAX: Int,
    val TOTAL_UNIT_PRICE_WITH_TAX: Int,
    val UNIT_PRICE_WITHOUT_TAX: Int,
    val UNIT_PRICE_WITH_TAX: Int
)