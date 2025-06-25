package com.akhnaton.atrapp.data.statuesValue.nav.home.address

sealed class AddressIntent {
    data class GetMyAddresses(val customerId: Int) : AddressIntent()

    data class AddUserAddress(
        val token: String?,
        val address: String?,
        val landmark: String?,
        val receiverName: String?,
        val receiverPhone: String?,
        val floorNumber: Int?,
        val apartmentNumber: Int?,
        val cityId: Int?,
        val countryId: Int?,
        val areaId: Int?,
        val prime: Int?
    ) : AddressIntent()

    data class MakeAddressPrime(
        val token: String?,
        val addressId: Int?
    ) : AddressIntent()
}
