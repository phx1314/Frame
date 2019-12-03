package com.mdx.framework.service

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit


fun <T> gB(clazz: Class<T>, baseUrl: String, token: String?, TIME: Long  ): T =
    ServiceFactory.createRxRetrofitService(clazz, baseUrl, token, TIME)

class ServiceFactory {
    companion object {

        private fun getOkHttpClient(token: String?, TIME: Long): OkHttpClient {
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
                .connectTimeout(TIME, TimeUnit.SECONDS)
                .readTimeout(TIME, TimeUnit.SECONDS)
                .writeTimeout(TIME, TimeUnit.SECONDS)
                .addNetworkInterceptor(loggingInterceptor)
                .build()
        }

        fun <T> createRxRetrofitService(
            clazz: Class<T>,
            endPoint: String,
            token: String?,
            TIME: Long
        ): T {
            val retrofit = Retrofit.Builder()
                .baseUrl(endPoint)
                .client(getOkHttpClient(token, TIME))
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

