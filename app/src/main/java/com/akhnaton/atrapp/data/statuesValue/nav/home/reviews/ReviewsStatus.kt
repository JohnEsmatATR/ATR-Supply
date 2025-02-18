package com.akhnaton.atrapp.data.statuesValue.nav.home.reviews

import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.data.model.auth.ErrorModel
import com.akhnaton.atrapp.data.model.review.ReviewModel

sealed class ReviewsStatus {

    data object Idle : ReviewsStatus()
    data object Loading : ReviewsStatus()
    data class GetReviews(val data: BaseModel<List<ReviewModel>>) : ReviewsStatus()
    data class AddReview(val data: BaseModel<List<ReviewModel>>) : ReviewsStatus()
    data class Error(val error: String?) : ReviewsStatus()
}