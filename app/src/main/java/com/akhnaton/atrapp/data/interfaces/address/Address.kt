package com.akhnaton.atrapp.data.interfaces.address

import com.akhnaton.atrapp.data.model.AddressModel
import com.akhnaton.atrapp.data.model.auth.LoginModel
import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.shared.ConstantLinks
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface Address {
    @GET(ConstantLinks.CUSTOMER_ADDRESS)
    suspend fun getAllAddress(
    ): Response<BaseModel<List<AddressModel>>>

    @FormUrlEncoded
    @POST(ConstantLinks.CHANGE_DEFAULT_ADDRESS)
    suspend fun changeDefaultSite(
        @Field("party_site_id") partySiteId: String,
    ): Response<BaseModel<AddressModel>>



}