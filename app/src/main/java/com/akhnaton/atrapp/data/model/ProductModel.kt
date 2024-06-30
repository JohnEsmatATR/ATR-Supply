package com.akhnaton.atrapp.data.model

import java.io.Serializable


data class ListProductModel(
    val products: List<ProductModel>,
    val NextOffset: Int,
)

data class ProductModel(
    var ID: Int,
    val TITLE: String,
    val DESCRIPTION: String,
    var IMAGE_URL: String,
    val WEIGHT: String,
    val QUANTITY: Int,
    val QOUTA: Int,
    val TAX: Double,
    val IS_BEST_SELLER: Boolean,
    val IS_LIKED: Boolean,
    val PRICE_WITHOUT_TAX: Double,
    val PRICE_DISCOUNT: Double,
    val PRICE_DISCOUNT_PERCENTAGE: String,
    val PRICE_AFTER_DISCOUNT: Double,
    val PRICE_WITH_TAX: Double,
    val RATE: Double,
    val IN_STOCK: Boolean,
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