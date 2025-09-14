package com.akhnaton.atrapp.data.interfaces.reviews

import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.data.model.review.ReviewModel
import com.akhnaton.atrapp.shared.ConstantLinks
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface IReviews {

    @FormUrlEncoded
    @POST(ConstantLinks.GET_REVIEWS)
    suspend fun getReviews(
        @Field("product_id") productId: String,
    ): Response<BaseModel<List<ReviewModel>>>

    @FormUrlEncoded
    @POST(ConstantLinks.ADD_REVIEW)
    suspend fun addReview(
        @Field("item_id") itemId: String,
        @Field("review_comment") reviewComment: String,
        @Field("review_value") reviewValue: String,
    ): Response<BaseModel<List<ReviewModel>>>

}