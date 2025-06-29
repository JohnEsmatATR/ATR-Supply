package com.akhnaton.atrapp.data.statuesValue.nav.home.address

sealed class AddressIntent {
    data class GetMyAddresses(val customerId: Int) : AddressIntent()

    data class MakeAddressPrime(
        val token: String?,
        val addressId: Int?
    ) : AddressIntent()
}
