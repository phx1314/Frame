//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mdx.framework.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mdx.framework.adapter.MAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MPagerAdapter<T> extends PagerAdapter {
    private Stack<View> mViews = new Stack();
    private List<T> mDataList = new ArrayList();
    private MAdapter<T> mAdapter;
    private Context context;
    private ViewPager mViewPager;

    public MPagerAdapter(Context context, List<T> list) {
        this.context = context;
        this.mDataList = list;
    }

    public MPagerAdapter(Context context, MAdapter<T> adapter) {
        this.context = context;
        this.mAdapter = adapter;
    }

    public Context getContext() {
        return this.context;
    }

    public int getCount() {
        return this.mAdapter != null ? this.mAdapter.getCount() : this.mDataList.size();
    }

    public int getItemPosition(Object object) {
        return -2;
    }

    public View getView(int position, View convertView) {
        if (this.mAdapter != null) {
            return this.mAdapter.getView(position, convertView, (ViewGroup) null);
        } else {
            ImageView mv = new ImageView(this.getContext());
            return mv;
        }
    }

    public Object instantiateItem(View rootview, int position) {
        this.mViewPager = (ViewPager) rootview;
        View contentView = null;
        if (this.mViews.size() > 0) {
            contentView = (View) this.mViews.pop();
        }

        contentView = this.getView(position, contentView);
        this.mViewPager.addView(contentView);
        return contentView;
    }

    public void destroyItem(View arg0, int position, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
        this.mViewPager = (ViewPager) arg0;
        this.mViews.push((View) arg2);
    }

    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    public T get(int position) {
        return this.mAdapter != null ? this.mAdapter.get(position) : this.mDataList.get(position);
    }
}
