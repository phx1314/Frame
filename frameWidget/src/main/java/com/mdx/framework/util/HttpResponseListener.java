package com.mdx.framework.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by DELL on 2017/3/29.
 */

public class HttpResponseListener extends StringCallback {
    public String methodName;
    public boolean isShow = true;
    public Context context;
    public HttpResponseListenerSon mHttpResponseListenerSon;
    public ProgressDialog dialog;


    public HttpResponseListener(Context context, HttpResponseListenerSon mHttpResponseListenerSon, String methodName, boolean isShow) {
        this.context = context;
        this.mHttpResponseListenerSon = mHttpResponseListenerSon;
        this.methodName = methodName;
        this.isShow = isShow;
        try {
            dialog = new ProgressDialog(context);
            dialog.setMessage("数据加载中，请稍后...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dismissProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    @Override
    public void onError(Request request, Exception e) {
        e.printStackTrace();
        mHttpResponseListenerSon.onError(methodName, e.toString());
        dismissProgressDialog();
    }

    @Override
    public void onResponse(String content) {
        Log.i("ok", content);
        try {
            dismissProgressDialog();
            if (!TextUtils.isEmpty(content)) {
                Object json = new JSONTokener(content).nextValue();
                if (json instanceof JSONObject) {
                    JSONObject mJSONObject = new JSONObject(content);
                    if (mJSONObject.has("HttpResult") && !mJSONObject.getBoolean("HttpResult")) {
                        Helper.toast(mJSONObject.getString("Message"));
                        return;
                    }
                    if (mJSONObject.has("stateMsg") && mJSONObject.getInt("stateType") != 0) {
                        Helper.toast(mJSONObject.getString("stateMsg"));
                        return;
                    }
                }
            }
            if (mHttpResponseListenerSon != null) {
                mHttpResponseListenerSon.onSuccess(methodName, content);

            }
        } catch (Exception e) {
            e.printStackTrace();
            mHttpResponseListenerSon.onError(methodName, content);
//            if (mHttpResponseListenerSon != null) {
//                mHttpResponseListenerSon.onSuccess(methodName, content);
//            }
        }
    }
}
