package com.akhnaton.atrSupply.data.model

data class MapModel(
    val id: Int,
    val name_ar: String,
    val name_en: String,
    val governorate: String,
    val city_id: Int,
    val country_id: Int,
)
