package com.akhnaton.atrapp.data.interfaces

import com.akhnaton.atrapp.data.model.common.CreditBaseResponse
import retrofit2.http.GET
import retrofit2.Response

interface IProfile {
    @GET("User/get_user_credit_limit_info")
    suspend fun getCreditInfo(): Response<CreditBaseResponse>
}