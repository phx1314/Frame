package com.mdx.framework.utility;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory.Options;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BitmapRead {
    public BitmapRead() {
    }

    public static int calculateInSampleSize(Options options, float reqWidth, float reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (reqHeight == 0.0F) {
            reqHeight = 3.4028235E38F;
        }

        if ((float) height > reqHeight || (float) width > reqWidth) {
            if ((float) width / reqWidth < (float) height / reqHeight) {
                if (height >= 1500) {
                    inSampleSize = Math.round((float) height / reqHeight);
                } else {
                    inSampleSize = (int) ((float) height / reqHeight);
                }
            } else if (width >= 1080) {
                inSampleSize = Math.round((float) width / reqWidth);
            } else {
                inSampleSize = (int) ((float) width / reqWidth);
            }
        }

        return inSampleSize;
    }

    public static float calculateInSampleSize(int height, int width, float reqWidth, float reqHeight) {
        float inSampleSize = 1.0F;
        if (reqHeight == 0.0F) {
            reqHeight = 3.4028235E38F;
        }

        if ((float) height > reqHeight || (float) width > reqWidth) {
            if ((float) width / reqWidth < (float) height / reqHeight) {
                if (height >= 1500) {
                    inSampleSize = (float) height / reqHeight;
                } else {
                    inSampleSize = (float) height / reqHeight;
                }
            } else if (width >= 1080) {
                inSampleSize = (float) width / reqWidth;
            } else {
                inSampleSize = (float) width / reqWidth;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, (float) reqWidth, (float) reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeSampledBitmapFromFile(String filePath, float reqWidth, float reqHeight) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static Bitmap decodeSampledBitmapFromByte(byte[] bytes, float reqWidth, float reqHeight) {
        return decodeSampledBitmapFromByte(bytes, 0, bytes.length, reqWidth, reqHeight);
    }

    public static Rect getRectFromByte(byte[] bytes) {
        return getRectFromByte(bytes, 0, bytes.length);
    }

    public static Rect getRectFromByte(byte[] bytes, int offset, int length) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, offset, length, options);
        Rect retn = new Rect(0, 0, options.outWidth, options.outHeight);
        return retn;
    }

    public static Bitmap decodeSampledBitmapFromByte(byte[] bytes, int offset, int length, float reqWidth, float reqHeight) {
        Options options = new Options();
        if (reqWidth != 0.0F || reqHeight != 0.0F) {
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(bytes, offset, length, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
        }

        return BitmapFactory.decodeByteArray(bytes, offset, length, options);
    }

    public static Bitmap decodeSampledBitmapFromStream(InputStream is, float reqWidth, float reqHeight) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        boolean c = false;

        int c1;
        try {
            while ((c1 = is.read(bytes)) != -1) {
                baos.write(bytes, 0, c1);
            }
        } catch (Exception var7) {
        }

        return decodeSampledBitmapFromByte(baos.toByteArray(), reqWidth, reqHeight);
    }

    public static byte[] getByte(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static void saveImage(Bitmap bit, OutputStream out) {
        bit.compress(CompressFormat.JPEG, 90, out);

        try {
            out.flush();
            out.close();
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }

    public static Bitmap rotationImg(Bitmap bm, int orientationDegree) {
        if (orientationDegree != 0) {
            int w = bm.getWidth();
            int h = bm.getHeight();
            Matrix m = new Matrix();
            m.setRotate((float) orientationDegree, (float) w / 2.0F, (float) h / 2.0F);
            Bitmap bitmap = Bitmap.createBitmap(bm, 0, 0, w, h, m, true);
            if (bm != bitmap) {
                bm.recycle();
            }

            bm = bitmap;
        }

        return bm;
    }

    public static Bitmap decodeBitmapSize(Bitmap bitmap, float reqWidth, float reqHeight) {
        byte[] byts = getByte(bitmap);
        bitmap.recycle();
        return decodeSampledBitmapFromByte(byts, reqWidth, reqHeight);
    }
}
