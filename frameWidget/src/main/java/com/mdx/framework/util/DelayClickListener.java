package com.mdx.framework.util;

import android.view.View;

public class DelayClickListener implements View.OnClickListener {
    private View.OnClickListener click;
    private long lastClickTime = 0L;

    public DelayClickListener(View.OnClickListener click) {
        this.click = click;
    }

    public void onClick(View v) {
        if(System.currentTimeMillis() - this.lastClickTime > 1000L) {
            this.lastClickTime = System.currentTimeMillis();
            this.click.onClick(v);
        }

    }
}
