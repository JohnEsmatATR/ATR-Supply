package com.akhnaton.atrapp.data.statuesValue.nav.home.address

sealed class AddressIntent {
    data object GetMyAddresses : AddressIntent()

    data class MakeAddressPrime(val partySiteId: String) : AddressIntent()
}
