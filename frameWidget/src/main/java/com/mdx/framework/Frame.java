package com.mdx.framework;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import android.app.Activity;
import android.content.Context;


import com.mdx.framework.utility.handle.Handles;
import com.mdx.framework.utility.permissions.PermissionsHelper;

import java.util.ArrayList;

public class Frame {
    private static ArrayList<Activity> NowShowActivity = new ArrayList();
    public static Handles HANDLES = new Handles();
    public static Context CONTEXT;

    public Frame() {
    }

    public static void finish() {
        HANDLES.closeAll();
    }


    public static void removNowShowActivity(Activity activity) {
        NowShowActivity.remove(activity);
    }

    public static void setNowShowActivity(Activity activity) {
        NowShowActivity.add(0, activity);
        PermissionsHelper.onActivityLoaded(getNowShowActivity());
    }

    public static Activity getNowShowActivity() {
        return NowShowActivity.size() == 0 ? null : (Activity) NowShowActivity.get(0);
    }

    public interface OnFinish {
        void onFinish(Context var1);
    }

    public static void init(Context context) {
        CONTEXT = context.getApplicationContext();
    }
}
