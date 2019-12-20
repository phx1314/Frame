//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mdx.framework.utility;

import java.util.Calendar;

public class UnitConver {
    public UnitConver() {
    }

    public static String getlevNow(long time) {
        Calendar cal = Calendar.getInstance();
        long timel = cal.getTimeInMillis() - time;
        return timel / 1000L < 60L?"1分钟以内":(timel / 1000L / 60L < 60L?timel / 1000L / 60L + "分前":(timel / 1000L / 60L / 60L < 24L?timel / 1000L / 60L / 60L + "小时前":timel / 1000L / 60L / 60L / 24L + "天前"));
    }

    public static String getBytesSize(Object obj) {
        if(obj == null) {
            return "";
        } else {
            long sized = Long.parseLong(obj.toString());
            double size = (double)sized;
            if(size > 1024.0D) {
                size /= 1024.0D;
                if(size > 1024.0D) {
                    size /= 1024.0D;
                    if(size > 1024.0D) {
                        size /= 1024.0D;
                        return Dou2Str(size, 2) + "GB";
                    } else {
                        return Dou2Str(size, 2) + "MB";
                    }
                } else {
                    return Dou2Str(size, 0) + "KB";
                }
            } else {
                return Dou2Str(size, 0) + "B";
            }
        }
    }

    public static String Dou2Str(double d, int count) {
        double dou = Math.abs(d);
        int rate = 1;

        for(int i = 0; i < count; ++i) {
            rate *= 10;
        }

        dou = (double)Math.round(dou * (double)rate);
        dou /= (double)rate;
        return dou - (double)Math.round(dou) == 0.0D?(d < 0.0D?"-":"") + String.valueOf((int)dou):(d < 0.0D?"-":"") + String.valueOf(dou);
    }
}
