package com.akhnaton.atrSupply.domain


import com.akhnaton.atrSupply.data.interfaces.address.Address
import com.akhnaton.atrSupply.data.model.AddressModel
import com.akhnaton.atrSupply.data.model.common.BaseModel
import com.akhnaton.atrSupply.shared.RetrofitClient
import retrofit2.Response

class AddressRepository {
    private val retrofit = RetrofitClient.getInstance(Address::class.java)


    suspend fun getMyAddresses(): Response<BaseModel<List<AddressModel>>> = retrofit.getAllAddress()

    suspend fun changeDefaultSite(partySiteId : String): Response<BaseModel<List<AddressModel>>> = retrofit.changeDefaultSite(partySiteId)
}