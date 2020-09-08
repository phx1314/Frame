package com.mdx.framework.adapter;// (powered by Fernflower decompiler)
//


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint({"UseSparseArrays"})
public abstract class MAdapter<T> extends MBaseAdapter {
    private Context context;
    protected HashMap<Integer, Boolean> map = new HashMap();
    protected int groupCount = 1;
    private LayoutInflater layoutInflater;
    private ArrayList<T> list = new ArrayList();
    private OnNotifyChangedListener<T> onNotifyChangedListener;
    private int resoure = 0;

    public MAdapter(Context context, List<T> list, int Resoure) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(this.context);
        if (list != null)
            this.list.addAll(list);
        this.resoure = Resoure;
    }

    public void AddAll(List<T> list) {
        this.list.addAll(list);
        this.resetGroup();
        this.notifyDataSetChanged();
    }

    public void AddAll(MAdapter<T> list) {
        for (int i = 0; i < list.getCount(); ++i) {
            T item = list.get(i);
            this.list.add(item);
            if (list.params != null) {
                this.params.putAll(list.params);
            }
        }

        this.resetGroup();
        this.notifyDataSetChanged();
    }

    public void AddAllOnBegin(MAdapter<T> list) {
        for (int i = list.getCount() - 1; i >= 0; --i) {
            T item = list.get(i);
            this.list.add(0, item);
            if (list.params != null) {
                this.params.putAll(list.params);
            }
        }

        this.resetGroup();
        this.notifyDataSetChanged();
    }

    public void add(T item) {
        this.getList().add(item);
        this.resetGroup();
        this.notifyDataSetChanged();
    }

    public void add(int ind, T item) {
        this.getList().add(ind, item);
        this.resetGroup();
        this.notifyDataSetChanged();
    }

    public void remove(Object item) {
        this.getList().remove(item);
        this.notifyDataSetChanged();
    }

    public void remove(int posion) {
        this.getList().remove(posion);
        this.notifyDataSetChanged();
    }

    public void clear() {
        this.getList().clear();
        this.nowAnimiCurry = 0L;
        this.notifyDataSetChanged();
    }

    public MAdapter(Context context, List<T> list) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(this.context);
        if (list != null)
            this.list.addAll(list);
    }

    public int getCount() {
        return this.list.size();
    }

    public T getItem(int position) {
        return this.list.get(position);
    }

    public long getItemId(int position) {
        return (long) (99900000 + position);
    }

    public View createView() {
        return this.createView(this.resoure);
    }

    public View createView(int resoure) {
        if (resoure == 0) {
            return null;
        } else {
            View view = this.layoutInflater.inflate(resoure, (ViewGroup) null);
            if (view instanceof MAdapterView) {
                MAdapterView met = (MAdapterView) view;
                met.init();
            } else {
                try {
                    Method met1 = view.getClass().getMethod("init", new Class[0]);
                    met1.invoke(view, new Object[0]);
                } catch (Exception var4) {
                    ;
                }
            }

            return view;
        }
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (this.onNotifyChangedListener != null) {
            this.onNotifyChangedListener.onNotifyChanged(this);
        }

    }

    public void resetGroup() {
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return this.context;
    }

    public LayoutInflater getLayoutInflater() {
        return this.layoutInflater;
    }

    public List<T> getList() {
        return this.list;
    }

    public int getResoure() {
        return this.resoure;
    }

    public T get(int ind) {
        return this.list.get(ind);
    }

    public void setOnNotifyChangedListener(OnNotifyChangedListener<T> onNotifyChangedListener) {
        this.onNotifyChangedListener = onNotifyChangedListener;
    }

    public interface OnNotifyChangedListener<T> {
        void onNotifyChanged(MAdapter<T> var1);
    }
}
