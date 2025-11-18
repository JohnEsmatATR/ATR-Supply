package com.akhnaton.atrSupply.data.statuesValue.nav.home.address

import com.akhnaton.atrSupply.data.model.AddressModel
import com.akhnaton.atrSupply.data.model.common.BaseModel


sealed class AddressStatus {
    object Idle : AddressStatus()
    object Loading : AddressStatus()

    data class GetMyAddresses(val result: BaseModel<List<AddressModel>>) : AddressStatus()

    data class MakeAddressPrime(val result: BaseModel<List<AddressModel>>) : AddressStatus()

    data class Error(val message: String?) : AddressStatus()
}
