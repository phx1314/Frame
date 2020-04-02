package com.mdx.framework.service.subscriber

import android.app.Dialog
import android.app.ProgressDialog
import android.util.Log
import com.google.gson.Gson
import com.mdx.framework.service.exception.HttpResultException
import com.mdx.framework.utility.AbLogUtil
import com.mdx.framework.utility.Helper
import io.reactivex.observers.DisposableObserver
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException


class S(
        var l: HttpResultSubscriberListener,
        var mProgressDialog: Dialog,
        var method: String,
        var isShow: Boolean
) : DisposableObserver<Any>() {


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
//            msg = "network anomaly"

        } else if (e is HttpResultException) {
            code = e.code
//            msg = e.msg
        }
        msg = "请求服务器失败"
        Helper.toast(msg)
        e.printStackTrace()
        l.onError(code.toString(), e.message, "", "")
    }

    override fun onNext(httpResult: Any) {
        Log.i(method, Gson().toJson(httpResult))
        l.onSuccess(Gson().toJson(httpResult), method)
//        if (httpResult.code.equals("1111")) {
//            try {
//                l.onSuccess(Gson().toJson(httpResult.data), method)
//                Log.i(method, Gson().toJson(httpResult.data))
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        } else {
//            l.onError(httpResult.code, httpResult.msg, Gson().toJson(httpResult.data), method)
//            Helper.toast(httpResult.msg)
//        }
    }
}
