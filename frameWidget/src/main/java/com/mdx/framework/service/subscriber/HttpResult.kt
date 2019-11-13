package com.mdx.framework.service.subscriber

data class HttpResult<T>(val code: String,
                         val data: T,
                         val message: String,
                         val isSuccess: Boolean,
                         val time: Long)