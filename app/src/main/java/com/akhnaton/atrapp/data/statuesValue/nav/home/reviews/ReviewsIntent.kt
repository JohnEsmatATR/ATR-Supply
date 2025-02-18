package com.akhnaton.atrapp.data.statuesValue.nav.home.reviews

sealed class ReviewsIntent {

    data class GetReviews(
        val version: String,
        val productId: String,
    ) : ReviewsIntent()

    data class AddReview(
        val version: String,
        val itemId: String,
        val reviewComment: String,
        val reviewValue: String,
    ) : ReviewsIntent()
}