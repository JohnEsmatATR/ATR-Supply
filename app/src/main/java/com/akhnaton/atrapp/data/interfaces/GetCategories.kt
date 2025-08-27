package com.akhnaton.atrapp.data.interfaces

import com.akhnaton.atrapp.data.model.OrderTypeModel
import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.shared.ConstantLinks
import retrofit2.http.GET

interface GetCategories {
    @GET(ConstantLinks.ORDER_TYPES)
    suspend fun getCategories(): BaseModel<List<OrderTypeModel>>
}