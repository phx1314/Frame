package com.mdx.framework.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;

import static com.mdx.framework.util.AbAppUtil.isNetworkAvailable;


public class HttpUtil {
    public static void load(Context context, Object tag,
                            String methodName, Callback mCallback, Object... mparams) {
        try {
            if (context == null) {
                return;
            }
            if (!isNetworkAvailable(context)) {
                Helper.toast("无可用网络，请检查网络连接" );
                return;
            }



            PostFormBuilder mPostFormBuilder = OkHttpUtils
                    .post();
            String url;
            if (methodName.startsWith("http:")) {
                url = methodName;
            } else {
                url = "/" + methodName;
            }
            mPostFormBuilder.url(url).tag(tag);
            for (int i = 0; i < mparams.length; i++) {
                if (mparams[i] instanceof Object[]) {
                    for (int j = 0; j < ((Object[]) mparams[i]).length; j++) {
                        if (((Object[]) mparams[i]).length > j + 1 && ((Object[]) mparams[i])[j + 1] != null) {
                            if (((Object[]) mparams[i])[j + 1] instanceof File) {
                                mPostFormBuilder.addFile(((Object[]) mparams[i])[j].toString(), ((File) ((Object[]) mparams[i])[j + 1]).getName(), (File) ((Object[]) mparams[i])[j + 1]);
                            } else {
                                mPostFormBuilder.addParams(((Object[]) mparams[i])[j].toString(), ((Object[]) mparams[i])[j + 1].toString());
                            }
                        }
                        j++;
                    }
                } else {
                    if (mparams.length > i + 1 && mparams[i + 1] != null) {
                        if (mparams[i + 1] instanceof File) {
                            mPostFormBuilder.addFile(mparams[i].toString(), ((File) mparams[i + 1]).getName(), (File) mparams[i + 1]);
                        } else {
                            mPostFormBuilder.addParams(mparams[i].toString(), mparams[i + 1].toString());
                        }
                    }
                    i++;
                }
            }
            Log.i("i", url);
            mPostFormBuilder.build().execute(mCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void loadGet(Context context, Object tag,
                            String methodName, Callback mCallback, Object... mparams) {
        try {
            if (context == null) {
                return;
            }
            if (!isNetworkAvailable(context)) {
                Helper.toast("无可用网络，请检查网络连接" );
                return;
            }



            GetBuilder mPostFormBuilder = OkHttpUtils
                    .get();
            String url;
            if (methodName.startsWith("https:")) {
                url = methodName;
            } else {
                url = "/" + methodName;
            }
            mPostFormBuilder.url(url).tag(tag);
            for (int i = 0; i < mparams.length; i++) {
                if (mparams[i] instanceof Object[]) {
                    for (int j = 0; j < ((Object[]) mparams[i]).length; j++) {
                        if (((Object[]) mparams[i]).length > j + 1 && ((Object[]) mparams[i])[j + 1] != null) {
                            if (((Object[]) mparams[i])[j + 1] instanceof File) {
//                                mPostFormBuilder.addFile(((Object[]) mparams[i])[j].toString(), ((File) ((Object[]) mparams[i])[j + 1]).getName(), (File) ((Object[]) mparams[i])[j + 1]);
                            } else {
                                mPostFormBuilder.addParams(((Object[]) mparams[i])[j].toString(), ((Object[]) mparams[i])[j + 1].toString());
                            }
                        }
                        j++;
                    }
                } else {
                    if (mparams.length > i + 1 && mparams[i + 1] != null) {
                        if (mparams[i + 1] instanceof File) {
//                            mPostFormBuilder.addFile(mparams[i].toString(), ((File) mparams[i + 1]).getName(), (File) mparams[i + 1]);
                        } else {
                            mPostFormBuilder.addParams(mparams[i].toString(), mparams[i + 1].toString());
                        }
                    }
                    i++;
                }
            }
            Log.i("i", url);
            mPostFormBuilder.build().execute(mCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void load(Context context, Object tag,
                            String methodName, Callback mCallback, Object obj) {
        try {
            if (context == null) {
                return;
            }
            if (!isNetworkAvailable(context)) {
                Helper.toast("无可用网络，请检查网络连接" );
                return;
            }
            String url;
            if (methodName.startsWith("http:")) {
                url = methodName;
            } else {
                url = "/" + methodName;
            }
            Log.i("i", url + " 参数:" + new Gson().toJson(obj));
            OkHttpUtils
                    .postString()
                    .url(url).tag(tag)
                    .content(new Gson().toJson(obj))
                    .mediaType(MediaType.parse("application/json"))
                    .build()
                    .execute(mCallback);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
