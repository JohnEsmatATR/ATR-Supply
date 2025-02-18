package com.akhnaton.atrapp.data.interfaces.reviews

import com.akhnaton.atrapp.data.model.CategoryModel
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.data.model.review.ReviewModel
import com.akhnaton.atrapp.shared.ConstantLinks
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface IReviews {

    @FormUrlEncoded
    @POST(ConstantLinks.GET_REVIEWS)
    suspend fun getReviews(
        @Header("version") version: String,
        @Field("product_id") productId: String,
    ): Response<BaseModel<List<ReviewModel>>>

    @FormUrlEncoded
    @POST(ConstantLinks.ADD_REVIEW)
    suspend fun addReview(
        @Header("version") version: String,
        @Field("item_id") itemId: String,
        @Field("review_comment") reviewComment: String,
        @Field("review_value") reviewValue: String,
    ): Response<BaseModel<List<ReviewModel>>>

}