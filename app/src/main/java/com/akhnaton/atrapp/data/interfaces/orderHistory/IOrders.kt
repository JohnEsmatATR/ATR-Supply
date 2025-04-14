package com.akhnaton.atrapp.data.interfaces.orderHistory

import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.data.model.orderHistory.OrderDetailsModel
import com.akhnaton.atrapp.data.model.orderHistory.OrderHistoryModel
import com.akhnaton.atrapp.shared.ConstantLinks
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface IOrders {

    @GET(ConstantLinks.MY_ORDERS)
    suspend fun getMyOrders(): Response<BaseModel<List<OrderHistoryModel>>>

    @FormUrlEncoded
    @POST(ConstantLinks.MY_ORDERS_DETAILS)
    suspend fun getMyOrderDetails(
        @Field("ORIG_SYS_DOCUMENT_REF") id: String,
    ): Response<BaseModel<List<OrderDetailsModel>>>

}