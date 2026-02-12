package com.akhnaton.atrapp.data.interfaces.login_with_customer_code

import com.akhnaton.atrapp.data.model.AppSettingResponse
import com.akhnaton.atrapp.data.model.auth.RegisterResponseData
import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.shared.ConstantLinks
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ILoginWithCustomerCode {

    @POST(ConstantLinks.SENT_OTP)
    @FormUrlEncoded
    suspend fun sentOtp(
        @Field("customer_code") code : String,
        @Field("phone_number") phone : String,
        @Field("email") email : String
    ): Response<BaseModel<Any>>

    @POST(ConstantLinks.VALIDATE_OTP)
    @FormUrlEncoded
    suspend fun validateOtp(
        @Field("customer_code") code : String,
        @Field("phone_number") phone : String,
        @Field("otp")otp : String
    ): Response<BaseModel<Any>>

    @POST(ConstantLinks.REGISTER_FROM_LINE)
    @FormUrlEncoded
    suspend fun registerFromLine(
        @Field("customer_code") code : String,
        @Field("phone_number")  phone : String,
        @Field("first_name")   firstName : String,
        @Field("last_name")    lastName : String,
        @Field("email")        email : String,
        @Field("fb_token")     fbToken : String,
        @Field("password")     password : String
    ):Response<BaseModel<RegisterResponseData>>

    @GET(ConstantLinks.APP_SETTINGS)
    suspend fun getAppSetting(): Response<BaseModel<AppSettingResponse>>
}