package com.akhnaton.atrapp.data.model

data class AddressesModel(
    val addresses: List<AddressModel> = ArrayList(),
)

data class AddNewAddressModel(
    val addresses: AddressModel = AddressModel(),
)

data class AddressModel(
    val ID: Int = 0,
    val LONGITUDE: Int = 0,
    val LATITUDE: Int = 0,
    val TITLE: String = "",
    val city_id: Int = 0,
    val area_id: Int = 0,
    val receiver_name: String = "",
    val receiver_phone: String = "",
    val user_id: Int = 0,
    val country_name: String = "",
    val city_name: String = "",
    val area_name: String = "",
    val floor_number: Int = 0,
    val apartment_number: Int = 0,
    val ADDRESS: String = "",
    val landmark: String = "",
    var prime: Int = 0,
)