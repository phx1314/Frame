//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mdx.framework.utility;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera.Size;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.Settings.System;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import com.mdx.framework.Frame;
import com.mdx.framework.commons.verify.Md5;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Device {
    public static String LOGID = UUID.randomUUID().toString();
    private static String IMSI;
    private static String PROVIDERSNAME;
    private static String MACADDRESS;
    private static String NETWORKTYPE;
    private static String NETWORKSUBTYPE;
    private static String NETWORKSUBNAME;
    private static String METICS_S;
    private static int NETWORKSPEED = 0;
    private static int TOUCHSLOP = 10;
    private static long LASTGETNETWORK = 0L;
    private static int METICS_W = 800;
    private static int METICS_H = 600;
    private static float DENSITY = 2.0F;
    private static String ID;

    public Device() {
    }

    @TargetApi(17)
    public static synchronized int getMeticsW() {
        Context context = Frame.CONTEXT;
        if (METICS_S == null) {
            @SuppressLint("WrongConstant") WindowManager configuration = (WindowManager) context.getSystemService("window");
            Display dis = configuration.getDefaultDisplay();
            DisplayMetrics dm;
            if (getSdkVersion() > 17) {
                dm = new DisplayMetrics();
                dis.getRealMetrics(dm);
                METICS_W = dm.widthPixels;
                METICS_H = dm.heightPixels;
                DENSITY = dm.density;
                METICS_S = "";
            } else {
                dm = new DisplayMetrics();
                dis.getMetrics(dm);
                METICS_W = dm.widthPixels;
                METICS_H = dm.heightPixels;
                DENSITY = dm.density;
                METICS_S = "";
            }
        }

        ViewConfiguration configuration1 = ViewConfiguration.get(context);
        TOUCHSLOP = configuration1.getScaledTouchSlop();
        return METICS_W;
    }

    public static synchronized int getMeticsH() {
        getMeticsW();
        return METICS_H;
    }

    public static synchronized float getDensity() {
        getMeticsW();
        return DENSITY;
    }

    public static synchronized String getModel() {
        return Build.MODEL;
    }

    public static String getId() {
        if (ID == null) {
            StringBuffer sb = new StringBuffer();
            String ANDROID_ID = System.getString(Frame.CONTEXT.getContentResolver(), "android_id");
            SharedPreferences sup = Frame.CONTEXT.getSharedPreferences("default_device_id", 0);
            String deviceid = sup.getString("deviceid", (String) null);
            if (deviceid == null) {
                deviceid = UUID.randomUUID().toString();
                sup.edit().putString("deviceid", deviceid).commit();
            }

            sb.append("&&");
            sb.append(ANDROID_ID);
            sb.append("&&");
            sb.append(deviceid);
            sb.append("&&");
            sb.append(Build.SERIAL);
            sb.append("&&");
            sb.append(getImsi());

            try {
                ID = Md5.md5(sb.toString());
                return ID;
            } catch (Exception var5) {
                return deviceid;
            }
        } else {
            return ID;
        }
    }

    public static synchronized String getBrand() {
        return Build.BRAND;
    }

    public static synchronized String getDevice() {
        return Build.DEVICE;
    }

    public static synchronized String getFacturer() {
        return Build.MANUFACTURER;
    }

    public static synchronized String getReleaseVersion() {
        String version = VERSION.RELEASE;
        if (version.toUpperCase(Locale.ENGLISH).indexOf("ANDROID") < 0) {
            version = "android " + version;
        }

        return version;
    }

    public static synchronized int getSdkVersion() {
        return VERSION.SDK_INT;
    }

    public static synchronized String getNetWorkSubName(Context context) {
        getNetworkType();
        return NETWORKSUBNAME;
    }

    public static synchronized String getNetWorkSubType() {
        getNetworkType();
        return NETWORKSUBTYPE;
    }

    public static synchronized String getImsi() {
        Context context = Frame.CONTEXT;

        try {
            if (IMSI == null) {
                @SuppressLint("WrongConstant") TelephonyManager e = (TelephonyManager) context.getSystemService("phone");
                if (ActivityCompat.checkSelfPermission(Frame.CONTEXT, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return "";
                }
                IMSI = e.getDeviceId();
                PROVIDERSNAME = e.getNetworkOperatorName();
            }
        } catch (Exception var2) {
            PROVIDERSNAME = "";
            return "";
        }

        return IMSI;
    }

    public static synchronized String getProvidersName() {
        getImsi();
        return PROVIDERSNAME;
    }

    public static synchronized String getMacAddress() {
        Context context = Frame.CONTEXT;

        try {
            if (MACADDRESS == null) {
                @SuppressLint("WrongConstant") WifiManager e = (WifiManager) context.getSystemService("wifi");
                WifiInfo info = e.getConnectionInfo();
                MACADDRESS = info.getMacAddress();
            }
        } catch (Exception var3) {
            return "";
        }

        return MACADDRESS;
    }

    public static synchronized String getNetworkType() {
        if (java.lang.System.currentTimeMillis() - LASTGETNETWORK > 50L) {
            LASTGETNETWORK = java.lang.System.currentTimeMillis();
            Context context = Frame.CONTEXT;

            try {
                @SuppressLint("WrongConstant") ConnectivityManager e = (ConnectivityManager) context.getSystemService("connectivity");
                NetworkInfo info = e.getActiveNetworkInfo();
                if (info == null) {
                    NETWORKTYPE = "NONE";
                    NETWORKSUBNAME = "NONE";
                    NETWORKSUBTYPE = "NONE";
                    NETWORKSPEED = 0;
                    return NETWORKTYPE;
                }

                switch (info.getType()) {
                    case 0:
                        NETWORKTYPE = "MOBILE";
                        NETWORKSUBTYPE = info.getSubtypeName();
                        switch (info.getSubtype()) {
                            case 0:
                                NETWORKSUBNAME = "unknown";
                                NETWORKSPEED = 1;
                                return NETWORKTYPE;
                            case 1:
                                NETWORKSUBNAME = "2G GPRS";
                                NETWORKSPEED = 2;
                                return NETWORKTYPE;
                            case 2:
                                NETWORKSUBNAME = "2G EDGE";
                                NETWORKSPEED = 2;
                                return NETWORKTYPE;
                            case 3:
                                NETWORKSUBNAME = "UMTS";
                                NETWORKSPEED = 3;
                                return NETWORKTYPE;
                            case 4:
                                NETWORKSUBNAME = "2G CDMA";
                                NETWORKSPEED = 2;
                                return NETWORKTYPE;
                            case 5:
                                NETWORKSUBNAME = "3G EVDO_0";
                                NETWORKSPEED = 3;
                                return NETWORKTYPE;
                            case 6:
                                NETWORKSUBNAME = "3G EVDO_A";
                                NETWORKSPEED = 3;
                                return NETWORKTYPE;
                            case 7:
                                NETWORKSUBNAME = "2G 1xRTT";
                                NETWORKSPEED = 2;
                                return NETWORKTYPE;
                            case 8:
                                NETWORKSUBNAME = "HSDPA";
                                NETWORKSPEED = 3;
                                return NETWORKTYPE;
                            case 9:
                                NETWORKSUBNAME = "HSUPA";
                                NETWORKSPEED = 2;
                                return NETWORKTYPE;
                            case 10:
                                NETWORKSUBNAME = "HSPA";
                                NETWORKSPEED = 2;
                                return NETWORKTYPE;
                            default:
                                NETWORKSUBNAME = "UMTS";
                                NETWORKSPEED = 3;
                                return NETWORKTYPE;
                        }
                    case 1:
                        NETWORKTYPE = "WIFI";
                        NETWORKSUBNAME = "WIFI";
                        NETWORKSUBTYPE = "WIFI";
                        NETWORKSPEED = 10;
                        break;
                    default:
                        NETWORKTYPE = "NONE";
                        NETWORKSUBNAME = "NONE";
                        NETWORKSUBTYPE = "NONE";
                        NETWORKSPEED = 0;
                }
            } catch (Exception var3) {
                NETWORKTYPE = "";
                NETWORKSUBNAME = "";
                NETWORKSUBTYPE = "";
                NETWORKSPEED = 1;
            }

            return NETWORKTYPE;
        } else {
            return NETWORKTYPE;
        }
    }

    public static Size getNearSize(List<Size> sizes, int nw, int nh) {
        Size retn = null;
        double cs = 0.0D;
        Iterator var6 = sizes.iterator();

        while (var6.hasNext()) {
            Size s = (Size) var6.next();
            if (retn == null) {
                cs = (double) (s.width / nw);
                retn = s;
            } else if (s.width > nw && cs > (double) (s.width / nw)) {
                cs = (double) (s.width / nw);
                retn = s;
            }
        }

        return retn;
    }

    public static synchronized int getNetWorkSpeed() {
        getNetworkType();
        if (NETWORKSPEED == 0) {
            AbLogUtil.d("system.run", "end:" + NETWORKSPEED);
        }

        return NETWORKSPEED;
    }

    public static synchronized long getRxBytes() {
        Context context = Frame.CONTEXT;
        int uid = context.getApplicationInfo().uid;
        long rxbytes = 0L;

        try {
            Class e = Class.forName("android.net.TrafficStats");
            Method method = e.getMethod("getUidRxBytes", new Class[]{Integer.TYPE});
            rxbytes = ((Long) method.invoke(e, new Object[]{Integer.valueOf(uid)})).longValue();
        } catch (Exception var6) {
            rxbytes = -2L;
        }

        return rxbytes;
    }

    public static synchronized long getTxBytes() {
        Context context = Frame.CONTEXT;
        int uid = context.getApplicationInfo().uid;
        long txbytes = 0L;

        try {
            Class e = Class.forName("android.net.TrafficStats");
            Method method = e.getMethod("getUidTxBytes", new Class[]{Integer.TYPE});
            txbytes = ((Long) method.invoke(e, new Object[]{Integer.valueOf(uid)})).longValue();
        } catch (Exception var6) {
            txbytes = -2L;
        }

        return txbytes;
    }

    public static int getTouchSlop() {
        return TOUCHSLOP;
    }

    public static void sendSms(String phone_number, String sms_content) {
        SmsManager smsManager = SmsManager.getDefault();
        if (sms_content.length() > 70) {
            ArrayList contents = smsManager.divideMessage(sms_content);
            smsManager.sendMultipartTextMessage(phone_number, (String) null, contents, (ArrayList) null, (ArrayList) null);
        } else {
            smsManager.sendTextMessage(phone_number, (String) null, sms_content, (PendingIntent) null, (PendingIntent) null);
        }

    }

    public static void setTouchSlop(int tOUCHSLOP) {
        TOUCHSLOP = tOUCHSLOP;
    }
}
