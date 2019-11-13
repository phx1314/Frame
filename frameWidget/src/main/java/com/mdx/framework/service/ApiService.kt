package com.mdx.framework.service

import android.database.Observable
import retrofit2.http.GET


interface ApiService {
    @GET("user/logout/json")
    fun logout(): Observable<Result<Any>>
}