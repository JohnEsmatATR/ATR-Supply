package com.akhnaton.atrapp.data.interfaces.cart

import com.akhnaton.atrapp.data.model.AddToCartResponse
import com.akhnaton.atrapp.data.model.CartResponse
import com.akhnaton.atrapp.data.model.CheckoutResponse
import com.akhnaton.atrapp.data.model.PaymentModel
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.shared.ConstantLinks
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
    suspend fun getMyCart(

    ): Response<BaseModel<List<CartResponse>>>

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