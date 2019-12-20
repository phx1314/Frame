//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mdx.framework.utility;

import android.view.View;
import android.view.View.OnClickListener;
import java.lang.reflect.Method;

public class DefaultClickListener implements OnClickListener {
    private Object parent;
    private String click;

    public DefaultClickListener(Object parent, String click) {
        this.parent = parent;
        this.click = click;
    }

    public void onClick(View v) {
        Method method = null;

        try {
            method = this.parent.getClass().getMethod(this.click, new Class[]{View.class});
            method.invoke(this.parent, new Object[]{v});
        } catch (Exception var4) {
            ;
        }

    }
}
