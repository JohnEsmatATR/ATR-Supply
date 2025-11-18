package com.akhnaton.atrSupply.data.interfaces

import com.akhnaton.atrSupply.data.model.OrderTypeModel
import com.akhnaton.atrSupply.data.model.common.BaseModel
import com.akhnaton.atrSupply.shared.ConstantLinks
import retrofit2.http.GET

interface GetCategories {
    @GET(ConstantLinks.ORDER_TYPES)
    suspend fun getCategories(): BaseModel<List<OrderTypeModel>>
}