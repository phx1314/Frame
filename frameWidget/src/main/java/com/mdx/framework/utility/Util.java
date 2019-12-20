//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mdx.framework.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Build.VERSION;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.renderscript.Allocation.MipmapControl;
import android.text.TextUtils;
import android.view.View;


import java.io.File;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    public static Handler HANDLER = new Handler(Looper.getMainLooper());
    public static final String FILE_START = "FILE:";
    public static final String JAR_START = "JAR:";
    public static final String ASSETS_START = "ASSETS:";
    public static final String MEDIA_START = "MEDIA:";
    public static final String MINMEDIA_START = "MINMEDIA:";

    public Util() {
    }

    public static void post(Runnable run) {
        HANDLER.post(run);
    }

    public static boolean isFullUrl(String url) {
        return url.toUpperCase(Locale.ENGLISH).startsWith("HTTP://") || url.toUpperCase(Locale.ENGLISH).startsWith("HTTPS://") || url.toUpperCase(Locale.ENGLISH).startsWith("FTP://");
    }

    public static DefaultClickListener newClickLitener(Object parent, String click) {
        return new DefaultClickListener(parent, click);
    }

    public static boolean isMedia(String url) {
        return url.toUpperCase(Locale.ENGLISH).startsWith("MEDIA:");
    }

    public static boolean isMinMedia(String url) {
        return url.toUpperCase(Locale.ENGLISH).startsWith("MINMEDIA:");
    }

    public static boolean isFileUrl(String url) {
        return url.toUpperCase(Locale.ENGLISH).startsWith("FILE:");
    }

    public static boolean isAssets(String url) {
        return url.toUpperCase(Locale.ENGLISH).startsWith("ASSETS:");
    }

    public static boolean isJarUrl(String url) {
        return url.toUpperCase(Locale.ENGLISH).startsWith("JAR:");
    }

    @SuppressLint({"NewApi"})
    public static long sizeOfBitmap(Bitmap bitmap) {
        return VERSION.SDK_INT >= 12 ? (long) bitmap.getByteCount() : (long) (bitmap.getRowBytes() * bitmap.getHeight());
    }

    public static boolean isPatten(String str, String patten) {
        if (TextUtils.isEmpty(str)) {
            return false;
        } else {
            Pattern pattern = Pattern.compile(patten);
            Matcher matcher = pattern.matcher(str);
            return matcher.find();
        }
    }


    public static File getMPath(Context context, String path) {
        if (path != null && path.startsWith("/")) {
            path = path.substring(1);
        }

        File file = null;
        File fparent = context.getDir("frame", 0);
        if (!fparent.exists()) {
            fparent.mkdir();
        }

        file = new File(fparent.getPath() + "/" + (path != null && path.length() != 0 ? "/" + path : ""));
        if (!file.exists()) {
            file.mkdirs();
        }

        return file;
    }

    public static File getMPath(Context context, String path, String filename) {
        if (path != null && path.startsWith("/")) {
            path = path.substring(1);
        }

        File file = getMPath(context, path);
        return file != null && file.isDirectory() ? new File(file, filename) : null;
    }


    public static String number2String(Double d) {
        return d == null ? null : (d.doubleValue() % 1.0D != 0.0D ? String.valueOf(d) : String.valueOf(Math.round(d.doubleValue())));
    }

    public static void sleep(long sleep) {
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException var3) {
            ;
        }

    }

    public static synchronized Boolean setScrollAbleParent(View view, boolean bol) {
        while (view.getParent() instanceof View) {
            view = (View) view.getParent();
            if (view instanceof MScrollAble) {
                MScrollAble scable = (MScrollAble) view;
                scable.setScrollAble(bol);
            }
        }

        return Boolean.valueOf(bol);
    }


    @SuppressLint({"NewApi"})
    public static Bitmap fastblur(Context context, Bitmap sentBitmap, int radius) {
        Bitmap bitmap;
        if (VERSION.SDK_INT > 16) {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
            RenderScript var39 = RenderScript.create(context);
            Allocation var40 = Allocation.createFromBitmap(var39, sentBitmap, MipmapControl.MIPMAP_NONE, 1);
            Allocation var41 = Allocation.createTyped(var39, var40.getType());
            ScriptIntrinsicBlur var42 = ScriptIntrinsicBlur.create(var39, Element.U8_4(var39));
            var42.setRadius((float) radius);
            var42.setInput(var40);
            var42.forEach(var41);
            var41.copyTo(bitmap);
            return bitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
            if (radius < 1) {
                return null;
            } else {
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();
                int[] pix = new int[w * h];
                bitmap.getPixels(pix, 0, w, 0, 0, w, h);
                int wm = w - 1;
                int hm = h - 1;
                int wh = w * h;
                int div = radius + radius + 1;
                int[] r = new int[wh];
                int[] g = new int[wh];
                int[] b = new int[wh];
                int[] vmin = new int[Math.max(w, h)];
                int divsum = div + 1 >> 1;
                divsum *= divsum;
                int[] dv = new int[256 * divsum];

                int i;
                for (i = 0; i < 256 * divsum; ++i) {
                    dv[i] = i / divsum;
                }

                int yi = 0;
                int yw = 0;
                int[][] stack = new int[div][3];
                int r1 = radius + 1;

                int rsum;
                int gsum;
                int bsum;
                int x;
                int y;
                int p;
                int stackpointer;
                int stackstart;
                int[] sir;
                int rbs;
                int routsum;
                int goutsum;
                int boutsum;
                int rinsum;
                int ginsum;
                int binsum;
                for (y = 0; y < h; ++y) {
                    bsum = 0;
                    gsum = 0;
                    rsum = 0;
                    boutsum = 0;
                    goutsum = 0;
                    routsum = 0;
                    binsum = 0;
                    ginsum = 0;
                    rinsum = 0;

                    for (i = -radius; i <= radius; ++i) {
                        p = pix[yi + Math.min(wm, Math.max(i, 0))];
                        sir = stack[i + radius];
                        sir[0] = (p & 16711680) >> 16;
                        sir[1] = (p & '\uff00') >> 8;
                        sir[2] = p & 255;
                        rbs = r1 - Math.abs(i);
                        rsum += sir[0] * rbs;
                        gsum += sir[1] * rbs;
                        bsum += sir[2] * rbs;
                        if (i > 0) {
                            rinsum += sir[0];
                            ginsum += sir[1];
                            binsum += sir[2];
                        } else {
                            routsum += sir[0];
                            goutsum += sir[1];
                            boutsum += sir[2];
                        }
                    }

                    stackpointer = radius;

                    for (x = 0; x < w; ++x) {
                        r[yi] = dv[rsum];
                        g[yi] = dv[gsum];
                        b[yi] = dv[bsum];
                        rsum -= routsum;
                        gsum -= goutsum;
                        bsum -= boutsum;
                        stackstart = stackpointer - radius + div;
                        sir = stack[stackstart % div];
                        routsum -= sir[0];
                        goutsum -= sir[1];
                        boutsum -= sir[2];
                        if (y == 0) {
                            vmin[x] = Math.min(x + radius + 1, wm);
                        }

                        p = pix[yw + vmin[x]];
                        sir[0] = (p & 16711680) >> 16;
                        sir[1] = (p & '\uff00') >> 8;
                        sir[2] = p & 255;
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                        rsum += rinsum;
                        gsum += ginsum;
                        bsum += binsum;
                        stackpointer = (stackpointer + 1) % div;
                        sir = stack[stackpointer % div];
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                        rinsum -= sir[0];
                        ginsum -= sir[1];
                        binsum -= sir[2];
                        ++yi;
                    }

                    yw += w;
                }

                for (x = 0; x < w; ++x) {
                    bsum = 0;
                    gsum = 0;
                    rsum = 0;
                    boutsum = 0;
                    goutsum = 0;
                    routsum = 0;
                    binsum = 0;
                    ginsum = 0;
                    rinsum = 0;
                    int yp = -radius * w;

                    for (i = -radius; i <= radius; ++i) {
                        yi = Math.max(0, yp) + x;
                        sir = stack[i + radius];
                        sir[0] = r[yi];
                        sir[1] = g[yi];
                        sir[2] = b[yi];
                        rbs = r1 - Math.abs(i);
                        rsum += r[yi] * rbs;
                        gsum += g[yi] * rbs;
                        bsum += b[yi] * rbs;
                        if (i > 0) {
                            rinsum += sir[0];
                            ginsum += sir[1];
                            binsum += sir[2];
                        } else {
                            routsum += sir[0];
                            goutsum += sir[1];
                            boutsum += sir[2];
                        }

                        if (i < hm) {
                            yp += w;
                        }
                    }

                    yi = x;
                    stackpointer = radius;

                    for (y = 0; y < h; ++y) {
                        pix[yi] = -16777216 & pix[yi] | dv[rsum] << 16 | dv[gsum] << 8 | dv[bsum];
                        rsum -= routsum;
                        gsum -= goutsum;
                        bsum -= boutsum;
                        stackstart = stackpointer - radius + div;
                        sir = stack[stackstart % div];
                        routsum -= sir[0];
                        goutsum -= sir[1];
                        boutsum -= sir[2];
                        if (x == 0) {
                            vmin[y] = Math.min(y + r1, hm) * w;
                        }

                        p = x + vmin[y];
                        sir[0] = r[p];
                        sir[1] = g[p];
                        sir[2] = b[p];
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                        rsum += rinsum;
                        gsum += ginsum;
                        bsum += binsum;
                        stackpointer = (stackpointer + 1) % div;
                        sir = stack[stackpointer];
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                        rinsum -= sir[0];
                        ginsum -= sir[1];
                        binsum -= sir[2];
                        yi += w;
                    }
                }

                bitmap.setPixels(pix, 0, w, 0, 0, w, h);
                return bitmap;
            }
        }
    }
}
