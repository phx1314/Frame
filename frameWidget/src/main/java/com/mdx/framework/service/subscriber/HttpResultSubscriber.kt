package com.mdx.framework.service.subscriber

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import com.mdx.framework.service.entity.HttpResultEntity
import com.mdx.framework.service.exception.HttpResultException
import com.mdx.framework.util.Frame
import rx.Subscriber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException

abstract class HttpResultSubscriber(var l: HttpResultSubscriberListener, var method: String) : Subscriber<HttpResultEntity>() {


    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        var code = -1000
        var msg: String? = e.message
        if (e is ConnectException
                || e is SocketTimeoutException
                || e is TimeoutException) {
            code = -9999
            msg = "network anomaly"

        } else if (e is HttpResultException) {
            code = e.code
            msg = e.msg
        }
        e.printStackTrace()
        Log.e("tag", "Request to enter the onError of HttpResultSubscriber , status is $code , msg is $msg")
        l.onError(code, msg)
        onCompleted()

    }

    override fun onNext(httpResult: HttpResultEntity) {
        httpResult.takeIf { it.isSuccess }?.also {
            l.onSuccess(httpResult.data, method)
        } ?: l.onError(httpResult.errorCode, "")
    }
}
