package com.akhnaton.atrapp.data.model.orderHistory

data class Pagination(
    val current_page: Int,
    val page_size: Int,
    val total_rows: String
)