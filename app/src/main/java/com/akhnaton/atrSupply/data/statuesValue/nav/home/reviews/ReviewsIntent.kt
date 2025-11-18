package com.akhnaton.atrSupply.data.statuesValue.nav.home.reviews

sealed class ReviewsIntent {

    data class GetReviews(
        val productId: String,
    ) : ReviewsIntent()

    data class AddReview(
        val itemId: String,
        val reviewComment: String,
        val reviewValue: String,
    ) : ReviewsIntent()
}