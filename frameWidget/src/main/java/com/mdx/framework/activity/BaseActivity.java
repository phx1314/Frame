package com.mdx.framework.activity;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MenuItem;


import com.framework.R;
import com.mdx.framework.permissions.PermissionsHelper;
import com.mdx.framework.util.Frame;
import com.mdx.framework.view.Headlayout;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

public abstract class BaseActivity extends FragmentActivity {
    public Fragment mFragment;
    public Headlayout mHeadlayout;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FragmentManager fm = this.getSupportFragmentManager();
        Iterator var5 = fm.getFragments().iterator();

        while (var5.hasNext()) {
            Fragment frag = (Fragment) var5.next();
            if (frag != null) {
                this.handleResult(frag, requestCode, resultCode, data);
            }
        }

    }

    public Context getContext() {
        return this;
    }

    private void handleResult(Fragment frag, int requestCode, int resultCode, Intent data) {
        frag.onActivityResult(requestCode & '\uffff', resultCode, data);
        List frags = frag.getChildFragmentManager().getFragments();
        if (frags != null) {
            Iterator var6 = frags.iterator();

            while (var6.hasNext()) {
                Fragment f = (Fragment) var6.next();
                if (f != null) {
                    this.handleResult(f, requestCode, resultCode, data);
                }
            }
        }

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.create(savedInstanceState);
        this.mHeadlayout = this.findViewById(R.id.mHeadlayout);
        Frame.setNowShowActivity(this);
        init();
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsHelper.onRequestPermissions(requestCode, permissions, grantResults);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Frame.removNowShowActivity(this);
    }

    public void init() {
        String classname = this.getIntent().getStringExtra("className");
        this.initFrament(classname);

    }


    private void initFrament(String classname) {
        try {
            Class cls = Class.forName(classname);
            Object e = cls.newInstance();
            if (e instanceof MFragment) {
                ((MFragment) e).setActionBar(mHeadlayout);
                this.showFragment((MFragment) e);
            }
        } catch (Exception var6) {
        }

    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (this.mFragment != null) {
            Fragment obj = this.mFragment;
            try {
                Method m1 = obj.getClass().getDeclaredMethod("onOptionsItemSelected", new Class[]{MenuItem.class});
                return ((Boolean) m1.invoke(obj, new Object[]{item})).booleanValue();
            } catch (Exception var5) {
                if (item.getItemId() == 16908332) {
                    this.finish();
                    return true;
                }
            }
        } else if (item.getItemId() == 16908332) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public abstract void create(Bundle var1);

    public void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (this.mFragment != null) {
            fragmentTransaction.remove(this.mFragment);
        }

        fragmentTransaction.replace(R.id.defautlt_container, fragment);
        this.mFragment = fragment;
        fragmentTransaction.commit();
    }

    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (this.mFragment != null && this.mFragment instanceof MFragment) {
            MFragment obj = (MFragment) this.mFragment;
            if (obj.onKeyLongPress(keyCode, event)) {
                return true;
            }
        }

        return super.onKeyLongPress(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (this.mFragment != null && this.mFragment instanceof MFragment) {
            MFragment obj = (MFragment) this.mFragment;
            if (obj.onKeyUp(keyCode, event)) {
                return true;
            }
        }

        return super.onKeyUp(keyCode, event);
    }

    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        if (this.mFragment != null && this.mFragment instanceof MFragment) {
            MFragment obj = (MFragment) this.mFragment;
            if (obj.onKeyMultiple(keyCode, repeatCount, event)) {
                return true;
            }
        }

        return super.onKeyMultiple(keyCode, repeatCount, event);
    }

    @SuppressLint({"NewApi"})
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        if (this.mFragment != null && this.mFragment instanceof MFragment) {
            MFragment obj = (MFragment) this.mFragment;
            if (obj.onKeyShortcut(keyCode, event)) {
                return true;
            }
        }

        return super.onKeyShortcut(keyCode, event);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (this.mFragment != null && this.mFragment instanceof MFragment) {
            MFragment obj = (MFragment) this.mFragment;
            if (obj.onKeyDown(keyCode, event)) {
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }
}
