package com.akhnaton.atrapp.shared

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

            // apply headers
            builder.apply {
                addInterceptor(
                    Interceptor { chain ->
                        val builder = chain.request().newBuilder()
                            .addHeader(
                                "Accept", "application/json"
                            )
                            .addHeader(
                                "token", "${SharedPreferenceHelper.userToken}"
                            )
                            .addHeader(
                                "device_type", "Android"
                            )
                            .addHeader(
                                "version", "${SharedPreferenceHelper.version}"
                            )

                        val response = chain.proceed(builder.build())

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