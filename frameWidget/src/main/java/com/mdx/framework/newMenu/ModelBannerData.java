package com.mdx.framework.newMenu;

import android.support.v4.app.Fragment;
import android.view.View.OnClickListener;


import com.mdx.framework.activity.MFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModelBannerData implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public ArrayList<MFragment> fragments = new ArrayList<MFragment>();
    public Map<Integer, String> mtexts = new HashMap<Integer, String>();
    public Map<Integer, Boolean> mStatus = new HashMap<Integer, Boolean>(); // 是否已经加载
    public Map<Integer, Boolean> mIsShow = new HashMap<Integer, Boolean>();
    public Map<Integer, String> mDatas = new HashMap<Integer, String>(); // 红点中的数字
    public Map<Integer, Integer> mres = new HashMap<Integer, Integer>();
    public Map<Integer, OnClickListener> mOnClickListeners = new HashMap<Integer, OnClickListener>();
    public int whitch = 3;
    public int width_left = 120;
    public int width_right = 120;
    public int position_new;
    public int position_old;
    public int position_check = 0;
    public int bcRes;
    public boolean isShow = true;
    public DfRadioGroup.DfCallback mICallback;
    public Fragment mFragment_left;
    public Fragment mFragment_right;

    public ArrayList<MFragment> getFragments() {
        return fragments;
    }

    public void setFragments(ArrayList<MFragment> fragments) {
        this.fragments = fragments;
    }

    public Map<Integer, String> getMtexts() {
        return mtexts;
    }

    public void setMtexts(Map<Integer, String> mtexts) {
        this.mtexts = mtexts;
    }

    public Map<Integer, Boolean> getmStatus() {
        return mStatus;
    }

    public void setmStatus(Map<Integer, Boolean> mStatus) {
        this.mStatus = mStatus;
    }

    public Map<Integer, Boolean> getmIsShow() {
        return mIsShow;
    }

    public void setmIsShow(Map<Integer, Boolean> mIsShow) {
        this.mIsShow = mIsShow;
    }

    public Map<Integer, String> getmDatas() {
        return mDatas;
    }

    public void setmDatas(Map<Integer, String> mDatas) {
        this.mDatas = mDatas;
    }

    public Map<Integer, Integer> getMres() {
        return mres;
    }

    public void setMres(Map<Integer, Integer> mres) {
        this.mres = mres;
    }

    public Map<Integer, OnClickListener> getmOnClickListeners() {
        return mOnClickListeners;
    }

    public void setmOnClickListeners(
            Map<Integer, OnClickListener> mOnClickListeners) {
        this.mOnClickListeners = mOnClickListeners;
    }

}
