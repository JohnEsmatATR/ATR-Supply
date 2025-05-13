package com.akhnaton.atrapp.data.interfaces

import com.akhnaton.atrapp.shared.ConstantLinks
import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.data.model.common.UserModel
import com.akhnaton.atrapp.data.model.auth.ErrorModel
import com.akhnaton.atrapp.data.model.auth.LoginModel
import com.akhnaton.atrapp.data.model.auth.forgetPassword.SendOtpModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface IAuth {

    @FormUrlEncoded
    @POST(ConstantLinks.LOGIN)
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<BaseModel<LoginModel>>


    @Multipart
    @POST(ConstantLinks.REGISTER)
    suspend fun register (
        @Part("first_name") firstName: RequestBody,
        @Part("last_name") lastName: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone_number") phone: RequestBody,
        @Part("password") password: RequestBody,
        @Part("address_title") addressTitle: RequestBody,
        @Part("address") address: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("firebase_token") gender: RequestBody,
        @Part attach_identity: MultipartBody.Part,
        @Part attach_coomercial_register: MultipartBody.Part,
        @Part attach_ownership: MultipartBody.Part,
        @Part attach_tax: MultipartBody.Part,
        @Part attach_license: MultipartBody.Part,
    ) : Response<BaseModel<List<LoginModel>>>


    @FormUrlEncoded
    @POST(ConstantLinks.SEND_OTP)
    suspend fun sendOtp(
        @Field("email") email: String,
    ): Response<BaseModel<SendOtpModel>>

    @FormUrlEncoded
    @POST(ConstantLinks.CHECK_OTP)
    suspend fun otp(
        @Field("email") email: String,
        @Field("otp") otp: String,
    ): Response<BaseModel<List<String>>>

    @FormUrlEncoded
    @POST(ConstantLinks.CHANGE_PASSWORD)
    suspend fun changePassword(
        @Field("email") email: String,
        @Field("otp") otp: String,
        @Field("password") password: String,
    ): Response<BaseModel<List<String>>>

}