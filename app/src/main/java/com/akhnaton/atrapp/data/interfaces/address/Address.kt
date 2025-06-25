package com.akhnaton.atrapp.data.interfaces.address

import com.akhnaton.atrapp.data.model.AddressModel
import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.shared.ConstantLinks
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Query

interface Address {
    @GET(ConstantLinks.CUSTOMER_ADDRESS)
    suspend fun getAllAddress(
        @Query("customer_code") customerCode: Int
    ): Response<BaseModel<List<AddressModel>>>



}