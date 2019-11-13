package com.mdx.framework.service.subscriber

interface HttpResultSubscriberListener {
    fun onSuccess(data: Any?, method: String)
    fun onError(status: String?, msg: String?)
}