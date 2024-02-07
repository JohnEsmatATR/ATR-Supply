package com.akhnaton.atrapp.data.model

import java.io.Serializable


data class CategoriesModel(
    val categories: List<CategoryModel>,
) : Serializable

data class CategoryModel(
    val id: Int,
    val name_en: String,
    val name_ar: String,
    val is_available: String,
    val image: String,
    val created_at: String,
    val updated_at: String,
) : Serializable