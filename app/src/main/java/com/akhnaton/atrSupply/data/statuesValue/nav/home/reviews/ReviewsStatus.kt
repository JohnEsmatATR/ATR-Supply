package com.akhnaton.atrSupply.data.statuesValue.nav.home.reviews

import com.akhnaton.atrSupply.data.model.common.BaseModel
import com.akhnaton.atrSupply.data.model.review.ReviewModel

sealed class ReviewsStatus {

    data object Idle : ReviewsStatus()
    data object Loading : ReviewsStatus()
    data class GetReviews(val data: BaseModel<List<ReviewModel>>) : ReviewsStatus()
    data class AddReview(val data: BaseModel<List<ReviewModel>>) : ReviewsStatus()
    data class Error(val error: String?) : ReviewsStatus()
}