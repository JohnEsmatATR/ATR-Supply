package com.akhnaton.atrapp.shared

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

class SetupHttpClient {

    fun setupOkHttpClient(): OkHttpClient {

        val REQUEST_TIMEOUT = 100 // 10 minute

        val builder = OkHttpClient.Builder()
            .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)

        try {
            val trustAllCerts: Array<TrustManager> = arrayOf(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String,
                    ) {

                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String,
                    ) {

                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            )

            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory

            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)

            // verify hostname
            builder.hostnameVerifier { hostname, session ->
                hostname == "sales.atr-eg.com"   // prod
//                hostname == "sales.atr-eg.com"    // test
            }

            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(httpLoggingInterceptor)

            // apply headers with logging
            builder.apply {
                addInterceptor(
                    Interceptor { chain ->
                        val lang = SharedPreferenceHelper.language?.takeIf { it.isNotBlank() }
                            ?: "ar"
                        val originalRequest = chain.request()
                        val requestBuilder = originalRequest.newBuilder()
                            .addHeader(
                                "Accept", "application/json"
                            )
                            .addHeader(
                                "token", "${SharedPreferenceHelper.userToken}"
                            )
                            .addHeader(
                                "version", "${SharedPreferenceHelper.version}"
                            )
                            .addHeader("devicetype", "Android")
                            .addHeader("language",lang)

                        // Log request headers
                        val request = requestBuilder.build()
                        Log.d("HTTP_REQUEST", "Request Headers:")
                        request.headers.forEach { (name, value) ->
                            Log.d("HTTP_REQUEST", "$name: $value")
                        }

                        val response = chain.proceed(request)

                        // Log response headers
                        Log.d("HTTP_RESPONSE", "Response Headers:")
                        response.headers.forEach { (name, value) ->
                            Log.d("HTTP_RESPONSE", "$name: $value")
                        }

                        return@Interceptor response
                    }
                )
            }.build()
            return builder.build()

        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}