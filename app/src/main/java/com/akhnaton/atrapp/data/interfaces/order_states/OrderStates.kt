package com.akhnaton.atrapp.data.interfaces.order_states

import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.data.model.order_states.OrderStatesModel
import com.akhnaton.atrapp.shared.ConstantLinks
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.GET

interface OrderStates {
    @GET(ConstantLinks.MY_ORDERS)
    suspend fun getOrderStates(
        @Field("ORIG_SYS_DOCUMENT_REF") id: String,
    ) : Response<BaseModel<List<OrderStatesModel>>>
}