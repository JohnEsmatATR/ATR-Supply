package com.akhnaton.atrSupply.data.statuesValue.auth.register

import okhttp3.RequestBody
import okhttp3.MultipartBody

sealed class RegisterIntent {

    data class Register(
        val first_name: RequestBody,
        val last_name: RequestBody,
        val email: RequestBody,
        val phone_number: RequestBody,
        val password: RequestBody,
        val address_title: RequestBody,
        val address: RequestBody,
        val latitude: RequestBody,
        val longitude: RequestBody,
        val firebase_token: RequestBody,
        val attach_identity: MultipartBody.Part,
        val attach_coomercial_register: MultipartBody.Part,
        val attach_ownership: MultipartBody.Part,
        val attach_tax: MultipartBody.Part,
        val attach_license: MultipartBody.Part,
        val mapAddress :RequestBody
    ) : RegisterIntent()
}