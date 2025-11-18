package com.akhnaton.atrSupply.data.interfaces.cart

import com.akhnaton.atrSupply.data.model.AddToCartResponse
import com.akhnaton.atrSupply.data.model.CartApiResponse
import com.akhnaton.atrSupply.data.model.CheckoutResponse
import com.akhnaton.atrSupply.data.model.PaymentModel
import com.akhnaton.atrSupply.data.model.common.BaseModel
import com.akhnaton.atrSupply.shared.ConstantLinks
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ICart {

    @FormUrlEncoded
    @POST(ConstantLinks.ADD_TO_CART)
    suspend fun addToCart(
        @Field("item_id") productId: Int?,
        @Field("quantity") quantity: Int?,
        @Field("order_type")category: String
    ): Response<BaseModel<AddToCartResponse>>

    @POST(ConstantLinks.GET_MY_CART)
    @FormUrlEncoded
    suspend fun getMyCart(
    @Field("language") language : String
    ):  Response<CartApiResponse>

    @FormUrlEncoded
    @POST(ConstantLinks.CHECKOUT)
    suspend fun checkout(
        @Field("payment_id") paymentId: Int,
        @Field("order_type") category: String
    ): Response<BaseModel<CheckoutResponse>>


    @FormUrlEncoded
    @POST(ConstantLinks.DELETE_FROM_CART)
    suspend fun deleteFromCart(
        @Field("item_id") productId: Int?,
        @Field("quantity") quantity: Int?,
    ): Response<BaseModel<AddToCartResponse>>


    @POST(ConstantLinks.PAYMENT_TYPE)
    suspend fun getPaymentType(): Response<BaseModel<List<PaymentModel>>>


}