package com.mdx.framework.service.subscriber

import android.app.ProgressDialog
import android.util.Log
import com.mdx.framework.service.exception.HttpResultException
import io.reactivex.observers.DisposableObserver
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException


abstract class S<Any>(var l: HttpResultSubscriberListener, var mProgressDialog: ProgressDialog , var method: String, var isShow: Boolean) : DisposableObserver<HttpResult<Any>>() {


    override fun onComplete() {
        Log.d("tag", " --> onCompleted")
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
        l.onError(code.toString(), msg)
        onComplete()

    }

    override fun onNext(httpResult: HttpResult<Any>) {
        httpResult.takeIf { it.isSuccess }.also {
            l.onSuccess(httpResult.data, method)
        } ?: l.onError(httpResult.code, "")
    }
}