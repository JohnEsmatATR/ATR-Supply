package com.akhnaton.atrapp.data.model

import java.io.Serializable


data class ListCategoryModel(
    val categories: List<CategoryModel>,
) : Serializable

data class CategoryModel(
    val ID: Int = 0,
    val TITLE: String = "",
    val IMAGE_URL: String = "",
) : Serializable