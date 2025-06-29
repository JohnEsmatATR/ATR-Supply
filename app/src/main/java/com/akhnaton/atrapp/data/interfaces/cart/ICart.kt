package com.akhnaton.atrapp.data.interfaces.cart

import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.shared.ConstantLinks
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ICart {

    @FormUrlEncoded
    @POST(ConstantLinks.ADD_TO_CART)
    suspend fun addToCart(
        @Field("item_id") productId: Int?,
        @Field("quantity") quantity: Int?,
    ): Response<BaseModel<ArrayList<String>>>

    @POST(ConstantLinks.GET_MY_CART)
    suspend fun getMyCart(): Response<BaseModel<List<ProductModel>>>

    @GET(ConstantLinks.CHECKOUT)
    suspend fun checkout(): Response<BaseModel<List<ProductModel>>>

    @FormUrlEncoded
    @POST(ConstantLinks.DELETE_FROM_CART)
    suspend fun deleteFromCart(
        @Field("item_id") productId: Int?,
        @Field("quantity") quantity: Int?,
    ): Response<BaseModel<ArrayList<String>>>


}