package com.mdx.framework.service.subscriber

data class HttpResult<T>(
    val code: String,
    val data: T,
    val msg: String
)