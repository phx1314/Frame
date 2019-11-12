package com.mdx.framework.service.subscriber

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.util.Log


abstract class S(l: HttpResultSubscriberListener, var context: Context?, method: String) : HttpResultSubscriber(l, method) {
    var mProgressDialog: ProgressDialog? = null

    init {
        if (context != null && !(context as Activity).isDestroyed) mProgressDialog = ProgressDialog(context).apply { this.setMessage("加载中,请稍后...") }
    }

    override fun onCompleted() {
        try {
            if (context != null && !(context as Activity).isDestroyed) mProgressDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.d("tag", " --> onCompleted")
    }

    override fun onStart() {
        super.onStart()
        try {
            if (context != null && !(context as Activity).isDestroyed)
                (context as Activity)!!.runOnUiThread {
                    mProgressDialog?.show()
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
