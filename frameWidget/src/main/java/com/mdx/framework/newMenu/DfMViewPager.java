package com.mdx.framework.newMenu;

import android.content.Context;
import android.util.AttributeSet;


/**
 *
 */
public class DfMViewPager extends UnsildeViewPager {
    public ICallback callback;

    public DfMViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DfMViewPager(Context context) {
        super(context);
    }

    public void setCallback(ICallback callback) {
        this.callback = callback;
    }


}
