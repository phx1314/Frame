package com.mdx.framework.service.subscriber

interface HttpResultSubscriberListener {
    fun onSuccess(data: String?, method: String)
    fun onError(code: String?, msg: String?, data: String?, method: String)
    fun onNext(httpResult: Any?, method: String)
}