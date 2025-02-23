package com.akhnaton.atrapp.domain

import com.akhnaton.atrapp.data.interfaces.reviews.IReviews
import com.akhnaton.atrapp.shared.RetrofitClient

class ReviewsRepository {
    private val retrofit = RetrofitClient.getInstance(IReviews::class.java)

    suspend fun getReviews(
        productId: String,
    ) = retrofit.getReviews(
        productId,
    )

    suspend fun addReview(
        itemId: String,
        reviewComment: String,
        reviewValue: String,
    ) = retrofit.addReview(
        itemId,
        reviewComment,
        reviewValue,
    )

}