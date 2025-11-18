package com.akhnaton.atrSupply.data.interfaces.order_states

import com.akhnaton.atrSupply.data.model.common.BaseModel
import com.akhnaton.atrSupply.data.model.order_states.OrderStatesModel
import com.akhnaton.atrSupply.shared.ConstantLinks
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.GET

interface OrderStates {
    @GET(ConstantLinks.MY_ORDERS)
    suspend fun getOrderStates(
        @Field("ORIG_SYS_DOCUMENT_REF") id: String,
    ) : Response<BaseModel<List<OrderStatesModel>>>
}