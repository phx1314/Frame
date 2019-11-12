package com.mdx.framework.service

import com.mdx.framework.service.entity.HttpResultEntity
import retrofit2.http.GET
import rx.Observable


interface ApiService {
    @GET("user/logout/json")
    fun logout(): Observable<HttpResultEntity>
}