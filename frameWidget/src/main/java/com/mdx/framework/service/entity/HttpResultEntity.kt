package com.mdx.framework.service.entity

class HttpResultEntity : BaseEntity () {
    val isSuccess: Boolean
        get() = errorCode == 0
}
