package com.akhnaton.atrapp.data.model.common

import com.google.gson.annotations.SerializedName

data class UserModel(
    val user: UserDataModel = UserDataModel(),
    val token: String = "",
)

data class UserDataModel(
    val id: Int = 0,
    val first_name: String = "",
    val last_name: String = "",
    val email: String = "",
    val phone: String = "",
    val type: String = "",
    val first_time_login: String = "",
    val email_verified_at: String = "",
    val is_blocked: String = "",
    val created_at: String = "",
    val updated_at: String = "",
    val country_id: String = "",

    @SerializedName("credit_limit")
    val credit_limit: Double = 0.0,

    @SerializedName("used_credit")
    val used_credit: Double = 0.0,

    @SerializedName("available_credit")
    val available_credit: Double = 0.0
)