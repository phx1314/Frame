package com.mdx.framework.util;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.IBinder;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.mdx.framework.Frame;
import com.mdx.framework.permissions.PermissionRequest;
import com.mdx.framework.permissions.PermissionsHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {
    private static final int kSystemRootStateUnknow = -1;
    private static final int kSystemRootStateDisable = 0;
    private static final int kSystemRootStateEnable = 1;
    private static int systemRootState = -1;

    public Helper() {
    }


    public static synchronized void toast(final CharSequence text) {
//        Toast.makeText(Frame.CONTEXT, text, 1).show();
        if (!TextUtils.isEmpty(text)) {
            Toast toast = Toast.makeText(Frame.CONTEXT, "", Toast.LENGTH_SHORT);
            toast.setText(text);
            toast.show();
        }
    }

    public int getActionbarHeight(Context context) {

        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(16843499, tv, true)) {
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
            return actionBarHeight;
        } else {
            return 0;
        }
    }

    public static void requestPermissions(String[] permissions, PermissionRequest runnable) {
        PermissionsHelper.requestPermissions(permissions, runnable);
    }

    public int getStatuHeight(Context context) {
        Class c = null;
        Object obj = null;
        Field field = null;
        boolean x = false;
        boolean sbar = false;

        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            int x1 = Integer.parseInt(field.get(obj).toString());
            int sbar1 = context.getResources().getDimensionPixelSize(x1);
            return sbar1;
        } catch (Exception var8) {
            var8.printStackTrace();
            return 0;
        }
    }


    public static String UnicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{2,4}))");

        char ch;
        for (Matcher matcher = pattern.matcher(str); matcher.find(); str = str.replace(matcher.group(1), ch + "")) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
        }

        return str;
    }

    public static DelayClickListener delayClickLitener(View.OnClickListener click) {
        return new DelayClickListener(click);
    }

    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) angle);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    public static byte[] getByte(File file) throws Exception {
        byte[] bytes = null;
        if (file != null) {
            FileInputStream is = new FileInputStream(file);
            int length = (int) file.length();
            if (length > 2147483647) {
                return null;
            }

            bytes = new byte[length];
            int offset = 0;

            int numRead1;
            for (boolean numRead = false; offset < bytes.length && (numRead1 = is.read(bytes, offset, bytes.length - offset)) >= 0; offset += numRead1) {
                ;
            }

            if (offset < bytes.length) {
                return null;
            }

            is.close();
        }

        return bytes;
    }

    public static Bitmap createCircleImage(Bitmap source, int min) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(min, min, Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawCircle((float) (min / 2), (float) (min / 2), (float) (min / 2), paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(source, 0.0F, 0.0F, paint);
        return target;
    }

    public static Bitmap createRoundConerImage(Bitmap source, int w, int h, int r) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        RectF rect = new RectF(0.0F, 0.0F, (float) source.getWidth(), (float) source.getHeight());
        canvas.drawRoundRect(rect, (float) r, (float) r, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(source, 0.0F, 0.0F, paint);
        return target;
    }

    public static void closeSoftKey(Context act, View v) {
        InputMethodManager localInputMethodManager = (InputMethodManager) act.getSystemService("input_method");
        if (localInputMethodManager.isActive()) {
            IBinder localIBinder = v.getWindowToken();
            localInputMethodManager.hideSoftInputFromWindow(localIBinder, 0);
        }

    }

    public static void startActivity(Context context, Class<?> fragment, Class<?> activity, Map<String, Object> params) {
        startActivity(context, 0, (String) fragment.getName(), activity, (Map) params);
    }

    public static void startActivity(Context context, int flag, Class<?> fragment, Class<?> activity, Map<String, Object> params) {
        startActivity(context, flag, fragment.getName(), activity, params);
    }

    public static void startActivity(Context context, int flag, String fragment, Class<?> activity, Map<String, Object> params) {
        Intent i = new Intent(context, activity);
        i.setFlags(flag);
        i.putExtra("className", fragment);
        if (params != null) {
            Iterator var7 = params.keySet().iterator();
            while (var7.hasNext()) {
                String key = (String) var7.next();
                Object obj = params.get(key);
                if (obj instanceof Boolean) {
                    i.putExtra(key, (Boolean) obj);
                } else if (obj instanceof Integer) {
                    i.putExtra(key, (Integer) obj);
                } else if (obj instanceof Float) {
                    i.putExtra(key, (Float) obj);
                } else if (obj instanceof Double) {
                    i.putExtra(key, (Double) obj);
                } else if (obj instanceof Long) {
                    i.putExtra(key, (Long) obj);
                } else if (obj instanceof String) {
                    i.putExtra(key, (String) obj);
                } else if (obj instanceof Serializable) {
                    i.putExtra(key, (Serializable) obj);
                } else if (obj instanceof Byte[]) {
                    i.putExtra(key, (Byte[]) ((Byte[]) obj));
                } else if (obj instanceof String[]) {
                    i.putExtra(key, (String[]) ((String[]) obj));
                } else if (obj instanceof Parcelable) {
                    i.putExtra(key, (Parcelable) obj);
                }
            }
        }

        context.startActivity(i);
    }

    public static void startActivity(Context context, Class<?> fragment, Class<?> activity, Object... params) {
        startActivity(context, 0, (String) fragment.getName(), activity, (Object[]) params);
    }

    public static void startActivity(Context context, int flag, Class<?> fragment, Class<?> activity, Object... params) {
        startActivity(context, flag, fragment.getName(), activity, params);
    }

    public static void startActivity(Context context, int flag, String fragment, Class<?> activity, Object... params) {
        Intent i = new Intent(context, activity);
        i.setFlags(flag);
        i.putExtra("className", fragment);
        if (params != null) {
            for (int ind = 0; ind < params.length; ++ind) {
                String key = params[ind].toString();
                if (params.length > ind + 1) {
                    Object obj = params[ind + 1];
                    if (obj instanceof Boolean) {
                        i.putExtra(key, (Boolean) obj);
                    } else if (obj instanceof Integer) {
                        i.putExtra(key, (Integer) obj);
                    } else if (obj instanceof Float) {
                        i.putExtra(key, (Float) obj);
                    } else if (obj instanceof Double) {
                        i.putExtra(key, (Double) obj);
                    } else if (obj instanceof Long) {
                        i.putExtra(key, (Long) obj);
                    } else if (obj instanceof String) {
                        i.putExtra(key, (String) obj);
                    } else if (obj instanceof Serializable) {
                        i.putExtra(key, (Serializable) obj);
                    } else if (obj instanceof Byte[]) {
                        i.putExtra(key, (Byte[]) ((Byte[]) obj));
                    } else if (obj instanceof String[]) {
                        i.putExtra(key, (String[]) ((String[]) obj));
                    } else if (obj instanceof Parcelable) {
                        i.putExtra(key, (Parcelable) obj);
                    }
                }

                ++ind;
            }
        }

        context.startActivity(i);
    }

    public static void startActivityForResult(Activity context, int requestCode, Class<?> fragment, Class<?> activity, Map<String, Object> params) {
        startActivityForResult(context, requestCode, fragment.getName(), activity, params);
    }

    public static void startActivityForResult(Activity context, int requestCode, String fragment, Class<?> activity, Map<String, Object> params) {
        Intent i = new Intent(context, activity);
        i.putExtra("className", fragment);
        if (params != null) {
            Iterator var7 = params.keySet().iterator();

            while (var7.hasNext()) {
                String key = (String) var7.next();
                Object obj = params.get(key);
                if (obj instanceof Boolean) {
                    i.putExtra(key, (Boolean) obj);
                } else if (obj instanceof Integer) {
                    i.putExtra(key, (Integer) obj);
                } else if (obj instanceof Float) {
                    i.putExtra(key, (Float) obj);
                } else if (obj instanceof Double) {
                    i.putExtra(key, (Double) obj);
                } else if (obj instanceof Long) {
                    i.putExtra(key, (Long) obj);
                } else if (obj instanceof String) {
                    i.putExtra(key, (String) obj);
                } else if (obj instanceof Serializable) {
                    i.putExtra(key, (Serializable) obj);
                } else if (obj instanceof Byte[]) {
                    i.putExtra(key, (Byte[]) ((Byte[]) obj));
                } else if (obj instanceof String[]) {
                    i.putExtra(key, (String[]) ((String[]) obj));
                } else if (obj instanceof Parcelable) {
                    i.putExtra(key, (Parcelable) obj);
                }
            }
        }

        context.startActivityForResult(i, requestCode);
    }

    public static void startActivityForResult(Activity context, int requestCode, Class<?> fragment, Class<?> activity, Object... params) {
        startActivityForResult(context, requestCode, fragment.getName(), activity, params);
    }

    public static void startActivityForResult(Activity context, int requestCode, String fragment, Class<?> activity, Object... params) {
        Intent i = new Intent(context, activity);
        i.putExtra("className", fragment);
        if (params != null) {
            for (int ind = 0; ind < params.length; ++ind) {
                String key = params[ind].toString();
                if (params.length > ind + 1) {
                    Object obj = params[ind + 1];
                    if (obj instanceof Boolean) {
                        i.putExtra(key, (Boolean) obj);
                    } else if (obj instanceof Integer) {
                        i.putExtra(key, (Integer) obj);
                    } else if (obj instanceof Float) {
                        i.putExtra(key, (Float) obj);
                    } else if (obj instanceof Double) {
                        i.putExtra(key, (Double) obj);
                    } else if (obj instanceof Long) {
                        i.putExtra(key, (Long) obj);
                    } else if (obj instanceof String) {
                        i.putExtra(key, (String) obj);
                    } else if (obj instanceof Serializable) {
                        i.putExtra(key, (Serializable) obj);
                    } else if (obj instanceof Byte[]) {
                        i.putExtra(key, (Byte[]) ((Byte[]) obj));
                    } else if (obj instanceof String[]) {
                        i.putExtra(key, (String[]) ((String[]) obj));
                    } else if (obj instanceof Parcelable) {
                        i.putExtra(key, (Parcelable) obj);
                    } else {
                    }
                }

                ++ind;
            }
        }

        context.startActivityForResult(i, requestCode);
    }

    @SuppressLint({"SimpleDateFormat"})
    public static String formatDateTime(Calendar cal, String format) {
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }

        SimpleDateFormat dateformat1 = new SimpleDateFormat(format);
        String ts = dateformat1.format(new Date(cal.getTimeInMillis()));
        return ts;
    }


    public static String getShare(Context context, String id) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(id, "");
    }

    public static void setShare(Context context, String id, String value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString(id, value);
        editor.commit();
    }

    public static String getShare(Context context, String id, String defaultvalue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(id, defaultvalue);
    }


    public static boolean isRootSystem() {
        if (systemRootState == 1) {
            return true;
        } else if (systemRootState == 0) {
            return false;
        } else {
            File f = null;
            String[] kSuSearchPaths = new String[]{"/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/", "/vendor/bin/"};

            try {
                for (int i = 0; i < kSuSearchPaths.length; ++i) {
                    f = new File(kSuSearchPaths[i] + "su");
                    if (f != null && f.exists()) {
                        systemRootState = 1;
                        return true;
                    }
                }
            } catch (Exception var3) {
                ;
            }

            systemRootState = 0;
            return false;
        }
    }

}
