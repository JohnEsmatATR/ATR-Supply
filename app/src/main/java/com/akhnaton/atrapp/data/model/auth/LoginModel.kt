package com.akhnaton.atrapp.data.model.auth

import com.akhnaton.atrapp.data.model.AddressModel


data class LoginModel(
    val id: String,
    val first_name: String,
    val last_name: String,
    val email: String,
    val address: List<AddressModel>,
    val is_approved: Int,
    val token: String,
)