package com.akhnaton.atrSupply.domain

import com.akhnaton.atrSupply.data.interfaces.reviews.IReviews
import com.akhnaton.atrSupply.shared.RetrofitClient

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