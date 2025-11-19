package com.akhnaton.atrapp.data.model.review

import java.io.Serializable


data class ReviewModel(
    val RATE: String = "",
    val REVIEWS_COUNT: String = "",
    val COMMENT: String = "",
    val USER_NAME: String = "",
) : Serializable