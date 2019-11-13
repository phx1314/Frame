package com.mdx.framework.service

import com.mdx.framework.service.subscriber.HttpResult
import com.mdx.framework.service.subscriber.S
import com.mdx.framework.util.AbAppUtil.isNetworkAvailable
import com.mdx.framework.util.Frame
import com.mdx.framework.util.Helper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit


fun <T> gB(clazz: Class<T>, baseUrl: String): T = ServiceFactory.createRxRetrofitService(clazz, baseUrl)

class ServiceFactory {
    companion object {
        private const val DEFAULT_TIMEOUT: Long = 30


        private fun getOkHttpClient(): OkHttpClient {
            val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                Timber.d(it)
            })
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
            return OkHttpClient.Builder()
//                    .addInterceptor {
//                val request = it.request().newBuilder()
//                        .addHeader("accept", "*/*")
////                        .addHeader("Authorization", Api.mToken)
//                        .addHeader("X-Accept-Locale", "zh_CN")
//                        .build()
//                it.proceed(request)
//            }
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).addInterceptor(loggingInterceptor).build()
        }

        fun <T> createRxRetrofitService(clazz: Class<T>, endPoint: String): T {
            val retrofit = Retrofit.Builder()
                    .baseUrl(endPoint)
                    .client(getOkHttpClient())
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

