package com.mdx.framework.permissions;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build.VERSION;
import android.support.v4.content.ContextCompat;

import com.mdx.framework.Frame;

import java.util.ArrayList;
import java.util.HashMap;

public class PermissionsHelper {
    public static HashMap<Integer, PermissionRequest> permissionRequestHashMap = new HashMap();
    public static ArrayList<PermissionsHelper.PermissionRun> permissionRuns = new ArrayList();
    public static int nowMaxCode = 10;

    public PermissionsHelper() {
    }

    @TargetApi(23)
    public static synchronized void requestPermissions(String[] permissions, PermissionRequest runnable) {
        Activity activity = Frame.getNowShowActivity();
        if(activity == null) {
            permissionRuns.add(new PermissionsHelper.PermissionRun(permissions, runnable));
        } else {
            ArrayList noPromissions = new ArrayList();
            String[] requestCode = permissions;
            int var5 = permissions.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String permission = requestCode[var6];
                int checkCallPhonePermission = ContextCompat.checkSelfPermission(activity, permission);
                if(checkCallPhonePermission != 0) {
                    noPromissions.add(permission);
                }
            }

            if(noPromissions.size() == 0) {
                if(runnable != null) {
                    runnable.onRequest(0, permissions, (int[])null);
                }
            } else if(VERSION.SDK_INT >= 23) {
                int var9 = nowMaxCode++;
                permissionRequestHashMap.put(Integer.valueOf(var9), runnable);
                activity.requestPermissions(permissions, var9);
            } else if(runnable != null) {
                runnable.onRequest(0, permissions, (int[])null);
            }

        }
    }

    public static synchronized void onRequestPermissions(int requestCode, String[] permissions, int[] grantResults) {
        PermissionRequest permissionRequest = (PermissionRequest)permissionRequestHashMap.remove(Integer.valueOf(requestCode));
        if(permissionRequest != null) {
            permissionRequest.onRequest(1, permissions, grantResults);
        }

    }

    public static synchronized void onActivityLoaded(Activity activity) {
        while(permissionRuns.size() > 0) {
            PermissionsHelper.PermissionRun pr = (PermissionsHelper.PermissionRun)permissionRuns.remove(0);
            requestPermissions(pr.permissions, pr.runnable);
        }

    }

    public static class PermissionRun {
        public String[] permissions;
        public PermissionRequest runnable;

        public PermissionRun(String[] permissions, PermissionRequest runnable) {
            this.permissions = permissions;
            this.runnable = runnable;
        }
    }
}
