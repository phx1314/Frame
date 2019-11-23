//
//  FrgCxFaxian
//
//  Created by Administrator on 2015-04-21 10:29:49
//  Copyright (c) Administrator All rights reserved.

/**
 *
 */

package com.mdx.framework.frg;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.framework.R;
import com.mdx.framework.Frame;
import com.mdx.framework.util.Helper;
import com.mdx.framework.view.Headlayout;

import java.util.ArrayList;
import java.util.List;

public class FrgSexChoose extends BaseFrg {

    public ListView mMPageListView;
    public List<String> data = new ArrayList<String>();
    public String from;
    public static final int SEX = 100;

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.frg_cx_sex_list);
        from = getActivity().getIntent().getStringExtra("from");
        initView();
        loaddata();
    }

    private void initView() {
        mMPageListView = (ListView) findViewById(R.id.mMPageListView);

    }

    public void loaddata() {
        data.add("男");
        data.add("女");
        mMPageListView.setAdapter(new ArrayAdapter<String>(getActivity(),
                R.layout.item_cx_text_ca, data));
        mMPageListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == 1) {
                    Frame.HANDLES.sentAll(from, SEX, "男");
                } else {
                    Frame.HANDLES.sentAll(from, SEX, "女");
                }
                FrgSexChoose.this.finish();
            }
        });
    }

    @Override
    public void onClick(View arg0) {
    }

    @Override
    public void setActionBar(LinearLayout actionBar) {
        Headlayout mHeadlayout = new Headlayout(getContext());
        mHeadlayout.setLeftBackground(R.drawable.arrows_left);
        mHeadlayout.setGoBack();
        mHeadlayout.setTitle("性别选择");
        actionBar.addView(mHeadlayout);
    }


}