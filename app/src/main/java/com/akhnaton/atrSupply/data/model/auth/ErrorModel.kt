package com.akhnaton.atrSupply.data.model.auth


data class ErrorModel(
    val phone: List<String>,
    val first_name: List<String>,
    val last_name: List<String>,
    val country_id: List<String>,
    val lat: List<String>,
    val lng: List<String>,
)