package com.akhnaton.atrapp.data.model

import com.akhnaton.atrapp.data.model.review.ReviewModel
import java.io.Serializable

data class ProductModel(
    val ID: Int = 0,
    val TITLE: String = "",
    val DESCRIPTION: String = "",
    var IMAGE_URL: String = "",
    val QUANTITY: Int = 0,
    var MY_QUANTITY: Int = 0,
    val WEIGHT: String = "",
    val QOUTA: Int = 0,
    val TAX: Double = 0.0,
    val IS_BEST_SELLER: Boolean = false,
    var IS_LIKED: Boolean = false,
    val PRICE_WITHOUT_TAX: Double = 0.0,
    val PRICE_DISCOUNT: Double = 0.0,
    val PRICE_DISCOUNT_PERCENTAGE: String = "",
    val PRICE_AFTER_DISCOUNT: Double = 0.0,
    val PRICE_WITH_TAX: Double = 0.0,
    val MY_QUANTITY_TOTAL_PRICE: Double = 0.0,
    val RATE: String = "",
    val REVIEWS: ReviewModel = ReviewModel(),
    val IN_STOCK: Boolean = false,
): Serializable