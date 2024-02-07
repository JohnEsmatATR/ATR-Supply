package com.akhnaton.atrapp.domain

import com.akhnaton.atrapp.data.interfaces.IApis
import com.akhnaton.atrapp.shared.RetrofitClient

class AuthRepository {
    private val retrofit = RetrofitClient.getInstance(IApis::class.java)

    suspend fun login(
        phone: String,
    ) = retrofit.login(
        phone,
    )

    suspend fun otp(
        otp: String,
        phone: String,
    ) = retrofit.otp(
        otp,
        phone,
    )

}