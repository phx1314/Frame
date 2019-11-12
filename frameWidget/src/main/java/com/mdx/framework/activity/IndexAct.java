package com.mdx.framework.activity;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import android.os.Bundle;
import android.view.KeyEvent;

import com.framework.R;
import com.mdx.framework.util.Frame;
import com.mdx.framework.util.Helper;


public class IndexAct extends  BaseActivity {
    private long exitTime = 0L;

    public IndexAct() {
    }


    @Override
    public void create(Bundle var1) {
        this.setContentView(R.layout.default_act_title);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 && event.getAction() == 0) {
            if (System.currentTimeMillis() - this.exitTime > 2000L) {
                Helper.toast("再按一次退出程序!" );
                this.exitTime = System.currentTimeMillis();
            } else {
                Frame.finish();
            }

            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


}
