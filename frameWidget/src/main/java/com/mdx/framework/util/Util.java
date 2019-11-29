package com.mdx.framework.util;

import android.view.View;

public class Util {
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
}
