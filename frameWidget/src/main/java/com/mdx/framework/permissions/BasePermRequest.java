package com.mdx.framework.permissions;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import java.util.ArrayList;

public class BasePermRequest extends PermissionRequest implements OnClickListener, OnCheckedChangeListener, OnItemSelectedListener {
    public View view;
    public CompoundButton compoundButton;
    public AdapterView adapterView;
    public boolean checked = false;
    public int i;
    public long l;
    public OnClickListener onClickListener;
    public OnCheckedChangeListener onCheckedChangeListener;
    public OnItemSelectedListener onItemSelectedListener;
    public String[] permissions;
    public ArrayList<Integer> run = new ArrayList();

    public BasePermRequest(String[] permissions, OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.permissions = permissions;
    }

    public BasePermRequest(String[] permissions, OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
        this.permissions = permissions;
    }

    public BasePermRequest(String[] permissions, OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
        this.permissions = permissions;
    }

    public void onGrant(String[] permissions, int[] grantResults) {
        while(this.run.size() > 0) {
            int type = ((Integer)this.run.remove(0)).intValue();
            switch(type) {
                case 1:
                    if(this.onCheckedChangeListener != null) {
                        this.onCheckedChangeListener.onCheckedChanged(this.compoundButton, this.checked);
                    }
                    break;
                case 2:
                    if(this.onClickListener != null) {
                        this.onClickListener.onClick(this.view);
                    }
                    break;
                case 3:
                    if(this.onItemSelectedListener != null) {
                        this.onItemSelectedListener.onItemSelected(this.adapterView, this.view, this.i, this.l);
                    }
                    break;
                case 4:
                    if(this.onItemSelectedListener != null) {
                        this.onItemSelectedListener.onNothingSelected(this.adapterView);
                    }
            }
        }

    }

    public void onUngrant(String[] permissions, int[] grantResults) {
    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        this.run.add(Integer.valueOf(1));
        this.compoundButton = compoundButton;
        this.checked = b;
        PermissionsHelper.requestPermissions(this.permissions, this);
    }

    public void onClick(View view) {
        this.run.add(Integer.valueOf(2));
        this.view = view;
        PermissionsHelper.requestPermissions(this.permissions, this);
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        this.run.add(Integer.valueOf(3));
        this.adapterView = adapterView;
        this.view = view;
        this.i = i;
        this.l = l;
        PermissionsHelper.requestPermissions(this.permissions, this);
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
        this.run.add(Integer.valueOf(4));
        this.adapterView = adapterView;
        PermissionsHelper.requestPermissions(this.permissions, this);
    }
}
