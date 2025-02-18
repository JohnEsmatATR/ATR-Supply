package com.akhnaton.atrapp.domain

import com.akhnaton.atrapp.data.interfaces.reviews.IReviews
import com.akhnaton.atrapp.shared.RetrofitClient

class ReviewsRepository {
    private val retrofit = RetrofitClient.getInstance(IReviews::class.java)

    suspend fun getReviews(
        version: String,
        productId: String,
    ) = retrofit.getReviews(
        version,
        productId,
    )

    suspend fun addReview(
        version: String,
        itemId: String,
        reviewComment: String,
        reviewValue: String,
    ) = retrofit.addReview(
        version,
        itemId,
        reviewComment,
        reviewValue,
    )

}