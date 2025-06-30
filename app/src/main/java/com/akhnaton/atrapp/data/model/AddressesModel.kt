package com.akhnaton.atrapp.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializer

data class AddressesModel(
    val addresses: List<AddressModel> = ArrayList(),
)

data class AddNewAddressModel(
    val addresses: AddressModel = AddressModel(),
)

data class AddressModel(
    @SerializedName("PARTY_SITE_ID")
    val ID: String? = "",
    val LONGITUDE: String = "",
    val LATITUDE: String = "",
    @SerializedName("SITE_ADDRESS")
    val TITLE: String?="" ,
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
    @SerializedName("IS_DEFAULT")
    var prime: Int = 0,
)
