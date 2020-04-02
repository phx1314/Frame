package com.mdx.framework.service.subscriber

interface HttpResultSubscriberListener {
    fun onSuccess(data: Any?, method: String)
    fun onError(code: String?, msg: String?, data: String?, method: String)
}