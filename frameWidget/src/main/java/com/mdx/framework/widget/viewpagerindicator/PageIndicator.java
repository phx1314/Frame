//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mdx.framework.widget.viewpagerindicator;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public interface PageIndicator extends OnPageChangeListener {
    void setViewPager(ViewPager var1);

    void setViewPager(ViewPager var1, int var2);

    void setCurrentItem(int var1);

    void setOnPageChangeListener(OnPageChangeListener var1);

    void notifyDataSetChanged();
}
