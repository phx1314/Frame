package com.mdx.framework.service.subscriber

interface HttpResultSubscriberListener {
    fun onSuccess(data: String?, method: String): Boolean
    fun onError(code: String?, msg: String?, data: String?, method: String)
}