package com.mdx.framework.service.subscriber

import android.app.ProgressDialog
import android.util.Log
import com.google.gson.Gson
import com.mdx.framework.service.exception.HttpResultException
import com.mdx.framework.util.Helper
import io.reactivex.observers.DisposableObserver
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException


class S<T>(
    var l: HttpResultSubscriberListener,
    var mProgressDialog: ProgressDialog,
    var method: String,
    var isShow: Boolean
) : DisposableObserver<HttpResult<T>>() {


    override fun onComplete() {
        Log.d("tag", " --> onCompleted")
    }

    override fun onError(e: Throwable) {
        var code = -1000
        var msg: String? = e.message
        if (e is ConnectException
            || e is SocketTimeoutException
            || e is TimeoutException
        ) {
            code = -9999
            msg = "network anomaly"

        } else if (e is HttpResultException) {
            code = e.code
            msg = e.msg
        }
        e.printStackTrace()
        Log.e(
            "tag",
            "Request to enter the onError of HttpResultSubscriber , status is $code , msg is $msg"
        )
        l.onError(code.toString(), msg)
    }

    override fun onNext(httpResult: HttpResult<T>) {
        Timber.d(httpResult.toString())
        if (httpResult.code.equals("1111")) {
            try {
                l.onSuccess(Gson().toJson(httpResult.data), method)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            Helper.toast(httpResult.msg)
        }
    }
}
