package com.akhnaton.atrapp.domain


import com.akhnaton.atrapp.data.interfaces.address.Address
import com.akhnaton.atrapp.data.model.AddressModel
import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.shared.RetrofitClient
import retrofit2.Response

class AddressRepository {
    private val retrofit = RetrofitClient.getInstance(Address::class.java)


    suspend fun getMyAddresses(): Response<BaseModel<List<AddressModel>>> = retrofit.getAllAddress()

    suspend fun changeDefaultSite(partySiteId : String): Response<BaseModel<AddressModel>> = retrofit.changeDefaultSite(partySiteId)
}