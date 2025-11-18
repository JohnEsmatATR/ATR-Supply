package com.akhnaton.atrSupply.data.model.auth

import com.akhnaton.atrSupply.data.model.AddressModel


data class LoginModel(
    val id: String,
    val first_name: String = "",
    val last_name: String = "",
    val phone: String = "",
    val email: String = "",
    val address: AddressModel = AddressModel(),
    val customer_code: String = "",
    val customer_id: String = "",
    val warehouse_id: String = "",
    val customer_category_code: String = "",
    val customer_branch: String = "",
    val customer_type: String = "",
    val party_site_id: String = "",
    val line_id: String = "",
    val is_approved: Int = 0,
    val token: String = "",
)