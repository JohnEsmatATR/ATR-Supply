package com.akhnaton.atrSupply.domain

import com.akhnaton.atrSupply.data.interfaces.login_with_customer_code.ILoginWithCustomerCode
import com.akhnaton.atrSupply.shared.RetrofitClient

class SentOtpRepository {
    private val retrofit = RetrofitClient.getInstance(ILoginWithCustomerCode::class.java)

    suspend fun sentOtp(code: String, phone: String) = retrofit.sentOtp(code, phone)


    suspend fun validateOtp(code: String, phone: String, otp: String) =
        retrofit.validateOtp(code, phone, otp)

    suspend fun register(
        code: String,
        phone: String,
        firstName: String,
        lastName: String,
        email: String,
        fbToken: String,
        password: String
    ) = retrofit.registerFromLine(code, phone, firstName, lastName, email, fbToken, password)

    suspend fun appSetting() = retrofit.getAppSetting()
}