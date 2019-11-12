package com.mdx.framework.service

import android.os.Environment
import android.util.Log
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.mdx.framework.service.converter.MyGsonConverterFactory
import com.mdx.framework.service.entity.HttpResultEntity
import com.mdx.framework.service.subscriber.S
import com.mdx.framework.util.AbAppUtil.isNetworkAvailable
import com.mdx.framework.util.Frame
import com.mdx.framework.util.Helper
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit


fun <T> gB(clazz: Class<T>, baseUrl: String): T = ServiceFactory.createRxRetrofitService(clazz, baseUrl)

fun Observable<HttpResultEntity>.l(s: S?): Subscription? {
    if (s?.context == null) return null
    if (!isNetworkAvailable(s?.context)) {
        Helper.toast("无可用网络，请检查网络连接")
        return null
    }
    return this.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(s)
}

class ServiceFactory {
    companion object {
        private const val DEFAULT_TIMEOUT: Long = 50

        private val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            Timber.d(it);
        })

        private fun getOkHttpClient(): OkHttpClient {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
            val builder = OkHttpClient.Builder()
            builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            builder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            builder.addInterceptor(loggingInterceptor)
            builder.cookieJar(PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(Frame.CONTEXT)))
//            builder.cache(Cache(File(Environment.getExternalStorageDirectory(), "deepbule"), 10 * 1024 * 1024))
            return builder.build()
        }

        fun <T> createRxRetrofitService(clazz: Class<T>, endPoint: String): T {
            val retrofit = Retrofit.Builder()
                    .baseUrl(endPoint)
                    .client(getOkHttpClient())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(MyGsonConverterFactory.create())
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

