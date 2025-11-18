package com.akhnaton.atrSupply.data.interfaces.orderHistory

import com.akhnaton.atrSupply.data.model.common.BaseModel
import com.akhnaton.atrSupply.data.model.orderHistory.OrderDetailsModel
import com.akhnaton.atrSupply.data.model.orderHistory.OrderHistoryModel
import com.akhnaton.atrSupply.shared.ConstantLinks
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
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