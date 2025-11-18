package com.akhnaton.atrSupply.shared

import okhttp3.Interceptor
import okhttp3.Response

class ApiInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val modifiedRequest = request.newBuilder()
            .addHeader("Accept", "application/json")
            .build()


        return chain.proceed(modifiedRequest)
    }
}
