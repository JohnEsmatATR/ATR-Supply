package com.akhnaton.atrapp.data.model

import java.io.Serializable


data class ListCategoryModel(
    val categories: List<CategoryModel>,
) : Serializable

data class CategoryModel(
    val ID: Int = 0,
    val TITLE: String = "",
    var IMAGE_URL: String = "",
) : Serializable {
    init {
        IMAGE_URL = convertToHttps(IMAGE_URL)
    }

    private fun convertToHttps(url: String): String {
        return if (url.startsWith("http://")) {
            url.replaceFirst("http://", "https://")
        } else if (url.startsWith("http:\\/\\/")) {
            url.replaceFirst("http:\\/\\/", "https:\\/\\/")
        } else {
            url
        }
    }
}