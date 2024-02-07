package com.akhnaton.atrapp.data.interfaces

import com.akhnaton.atrapp.shared.ConstantLinks
import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.data.model.common.UserModel
import com.akhnaton.atrapp.data.model.auth.ErrorModel
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface IApis {

    @FormUrlEncoded
    @POST(ConstantLinks.LOGIN)
    suspend fun login(
        @Field("phone") phone: String,
    ): Response<BaseModel<String, ErrorModel>>

    @FormUrlEncoded
    @POST(ConstantLinks.CHECK_OTP)
    suspend fun otp(
        @Field("otp") otp: String,
        @Field("phone") phone: String,
    ): Response<BaseModel<UserModel, ErrorModel>>


}