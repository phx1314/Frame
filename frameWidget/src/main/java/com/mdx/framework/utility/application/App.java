//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mdx.framework.utility.application;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.mdx.framework.utility.application.AppInfo;
import com.mdx.framework.utility.application.MContact;
import com.mdx.framework.utility.application.MContacts;
import com.mdx.framework.utility.application.MContacts.OnContactAddListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class App {
    public App() {
    }

    public static List<AppInfo> getAppList(Context context) {
        List packages = context.getPackageManager().getInstalledPackages(0);
        ArrayList list = new ArrayList();
        Iterator var3 = packages.iterator();

        while(var3.hasNext()) {
            PackageInfo packinfo = (PackageInfo)var3.next();
            AppInfo apk = new AppInfo();
            ApplicationInfo app = packinfo.applicationInfo;
            if((app.flags & 1) == 0) {
                apk.setName(app.loadLabel(context.getPackageManager()).toString());
                apk.setPackage(packinfo.packageName);
                apk.setVersion(packinfo.versionCode);
                apk.setVersionName(packinfo.versionName);
                if(!apk.getPackage().startsWith("com.wjwl.apkfactory")) {
                    list.add(apk);
                }
            }
        }

        return list;
    }

    public static Drawable getIcon(Context context, String packageName) throws NameNotFoundException {
        PackageInfo pack = context.getPackageManager().getPackageInfo(packageName, 0);
        ApplicationInfo app = pack.applicationInfo;
        return app.loadIcon(context.getPackageManager());
    }

    public static AppInfo getApp(Context context, String packageName) throws NameNotFoundException {
        PackageInfo pack = context.getPackageManager().getPackageInfo(packageName, 0);
        AppInfo apk = new AppInfo();
        ApplicationInfo app = pack.applicationInfo;
        apk.setName(app.loadLabel(context.getPackageManager()).toString());
        apk.setPackage(pack.packageName);
        apk.setVersion(pack.versionCode);
        apk.setVersionName(pack.versionName);
        return apk;
    }

    public static void install(Context context, String path) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");
        intent.addFlags(268435456);
        context.startActivity(intent);
    }

    public static void open(Context context, String packag) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packag);
        intent.addFlags(268435456);
        context.startActivity(intent);
    }

    @SuppressLint("WrongConstant")
    public static void deleteApp(Context context, String packag) {
        Uri packageURI = Uri.parse("package:" + packag);
        Intent uninstallIntent = new Intent("android.intent.action.DELETE", packageURI);
        uninstallIntent.addFlags(268435456);
        context.startActivity(uninstallIntent);
    }

    public static List<MContact> getContacts(Context context) {
        return getContacts(context, (OnContactAddListener)null);
    }

    public static List<MContact> getContacts(Context context, OnContactAddListener onadd) {
        MContacts conts = new MContacts();
        return conts.getContact(context, onadd);
    }

    public static Drawable getContantPhoto(Context context, MContact contact) {
        MContacts conts = new MContacts();
        return conts.getPhoto(context, contact);
    }
}
