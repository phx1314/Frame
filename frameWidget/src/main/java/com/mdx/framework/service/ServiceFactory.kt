package com.mdx.framework.service

import com.mdx.framework.utility.AbLogUtil
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


fun <T> gB(clazz: Class<T>, baseUrl: String, token: String?, TIME: Long, c: Converter.Factory = GsonConverterFactory.create()): T =
        ServiceFactory.createRxRetrofitService(clazz, baseUrl, token, TIME, c)

class ServiceFactory {
    companion object {
        private fun getOkHttpClient(token: String?, TIME: Long): OkHttpClient {
            val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                AbLogUtil.d(it)
            })

            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            return if (TIME >= 1000) OkHttpClient.Builder()
                    .connectTimeout(TIME, TimeUnit.SECONDS)
                    .readTimeout(TIME, TimeUnit.SECONDS)
                    .writeTimeout(TIME, TimeUnit.SECONDS)
                    .build() else OkHttpClient.Builder()
                    .addInterceptor {
                        AbLogUtil.d(it.request().body.toString())
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
                TIME: Long, c: Converter.Factory
        ): T {
            val retrofit = Retrofit.Builder()
                    .baseUrl(endPoint)
                    .client(getOkHttpClient(token, TIME))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(c)
                    .build()

            return retrofit.create(clazz)
        }

    }


}

