package com.akhnaton.atrapp.data.interfaces.cart

import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.shared.ConstantLinks
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface IAddToCart {
    @FormUrlEncoded
    @POST(ConstantLinks.ADD_TO_CART)
    suspend fun addToCart(
        @Field("item_id") productId: Int?,
        @Field("quantity") quantity: Int?,
    ): Response<BaseModel<ArrayList<String>>>

}