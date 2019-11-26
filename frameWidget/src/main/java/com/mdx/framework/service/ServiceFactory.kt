package com.mdx.framework.service

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit


fun <T> gB(clazz: Class<T>, baseUrl: String, token: String?): T =
    ServiceFactory.createRxRetrofitService(clazz, baseUrl, token)

class ServiceFactory {
    companion object {
        private const val DEFAULT_TIMEOUT: Long = 30


        private fun getOkHttpClient(token: String?): OkHttpClient {
            val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                Timber.d(it)
            })
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            return OkHttpClient.Builder()
                .addInterceptor {
                    Timber.d(it.request().body.toString())
                    val request = it.request().newBuilder()
//                        .addHeader("accept", "*/*")
//                        .addHeader("Authorization", Api.mToken)
                        .addHeader("token", token ?: "")
                        .build()
                    it.proceed(request)
                }
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(loggingInterceptor)
                .build()
        }

        fun <T> createRxRetrofitService(clazz: Class<T>, endPoint: String, token: String?): T {
            val retrofit = Retrofit.Builder()
                .baseUrl(endPoint)
                .client(getOkHttpClient(token))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(clazz)
        }

        fun <T> createRetrofitService(clazz: Class<T>, endPoint: String): T {
            return Retrofit
                .Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(endPoint)
                .build()
                .create(clazz)

        }

    }


}

