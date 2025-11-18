package com.akhnaton.atrSupply.data.interfaces.address

import com.akhnaton.atrSupply.data.model.AddressModel
import com.akhnaton.atrSupply.data.model.common.BaseModel
import com.akhnaton.atrSupply.shared.ConstantLinks
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface Address {
    @GET(ConstantLinks.CUSTOMER_ADDRESS)
    suspend fun getAllAddress(
    ): Response<BaseModel<List<AddressModel>>>

    @FormUrlEncoded
    @POST(ConstantLinks.CHANGE_DEFAULT_ADDRESS)
    suspend fun changeDefaultSite(
        @Field("party_site_id") partySiteId: String,
    ): Response<BaseModel<List<AddressModel>>>



}