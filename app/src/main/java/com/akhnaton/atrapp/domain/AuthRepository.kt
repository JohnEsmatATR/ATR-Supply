package com.akhnaton.atrapp.domain

import com.akhnaton.atrapp.data.interfaces.IAuth
import com.akhnaton.atrapp.shared.RetrofitClient
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AuthRepository {
    private val retrofit = RetrofitClient.getInstance(IAuth::class.java)

    suspend fun login(
        email: String,
        password: String,
    ) = retrofit.login(
        email,
        password,
    )

    suspend fun register(
        first_name: RequestBody,
        last_name: RequestBody,
        email: RequestBody,
        phone_number: RequestBody,
        password: RequestBody,
        address_title: RequestBody,
        address: RequestBody,
        latitude: RequestBody,
        longitude: RequestBody,
        firebase_token: RequestBody,
        attach_identity: MultipartBody.Part,
        attach_coomercial_register: MultipartBody.Part,
        attach_ownership: MultipartBody.Part,
        attach_tax: MultipartBody.Part,
        attach_license: MultipartBody.Part,
    ) = retrofit.register(
        first_name,
        last_name,
        email,
        phone_number,
        password,
        address_title,
        address,
        latitude,
        longitude,
        firebase_token,
        attach_identity,
        attach_coomercial_register,
        attach_ownership,
        attach_tax,
        attach_license,
    )

    suspend fun sendOtp(
        email: String,
    ) = retrofit.sendOtp(
        email,
    )

    suspend fun checkOtp(
        email: String,
        otp: String,
    ) = retrofit.otp(
        email,
        otp,
    )

    suspend fun changePassword(
        email: String,
        otp: String,
        password: String,
    ) = retrofit.changePassword(
        email,
        otp,
        password,
    )

}