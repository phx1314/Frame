package com.mdx.framework.activity;// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;


import com.mdx.framework.Frame;
import com.mdx.framework.handle.MHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class MFragment extends Fragment {
    protected MHandler handler;
    private View contextView = null;
    private LayoutInflater inflater;
    private ViewGroup viewgroup;
    private Boolean createed = Boolean.valueOf(false);
    private ArrayList<OnFragmentCreateListener> onFragmentCreateListenerss = new ArrayList();

    public MFragment() {
    }


    public void setContentView(int contextview) {
        this.contextView = this.inflater.inflate(contextview, this.viewgroup, false);
    }

    public void setContextView(View contextview) {
        this.contextView = contextview;
    }

    public View getContextView() {
        return this.contextView;
    }

    public Context getContext() {
        return this.getActivity();
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        this.handler = new MHandler();
        String className = this.getClass().getSimpleName();
        this.handler.setId(className);
        this.handler.setMsglisnener(new MHandler.HandleMsgLisnener() {
            public void onMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        MFragment.this.finish();
                        break;
                    case 99:
                        MFragment.this.removeFragment(MFragment.this);
                        break;
                    case 201:
                        MFragment.this.disposeMsg(msg.arg1, msg.obj);
                }

            }
        });
        Frame.HANDLES.add(this.handler);
        this.initcreate(savedInstanceState);
    }

    public void setActionBar(LinearLayout mActionBar) {
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        this.viewgroup = container;
        if (!this.createed.booleanValue()) {
            this.createed = Boolean.valueOf(true);
            this.create(savedInstanceState);
            Iterator var4 = this.onFragmentCreateListenerss.iterator();

            while (var4.hasNext()) {
                OnFragmentCreateListener onf = (OnFragmentCreateListener) var4.next();
                onf.onFragmentCreateListener(this);
            }
        }

        if (this.contextView.getParent() != null && this.contextView.getParent() instanceof ViewGroup) {
            ((ViewGroup) this.contextView.getParent()).removeView(this.contextView);
        }

        return this.contextView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initV(view);
    }

    public void initV(View view) {
    }

    public View findViewById(int id) {
        return this.contextView == null ? null : this.contextView.findViewById(id);
    }

    protected void removeFragment(MFragment fragment) {
        FragmentActivity parent = this.getActivity();
        FragmentTransaction fragmentTransaction = parent.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }

    public void finish() {
        if (this.getActivity() != null) {
            this.getActivity().finish();
        }

    }

    public void onDestroy() {

        Frame.HANDLES.remove(this.handler);
        this.destroy();
        super.onDestroy();
    }

    public void onPause() {
        super.onPause();
        this.pause();
    }

    public void onResume() {
        super.onResume();
        this.resume();
    }

    public void sendMessage(Message msg) {
        this.handler.sendMessage(msg);
    }

    public void close(String id) {
        Frame.HANDLES.close(id);
    }

    public List<MHandler> get(String id) {
        return Frame.HANDLES.get(id);
    }

    public MHandler getOne(String id) {
        return Frame.HANDLES.get(id).size() > 0 ? (MHandler) Frame.HANDLES.get(id).get(0) : null;
    }

    public void setId(String id) {
        this.handler.setId(id);
    }

    public String getHId() {
        return this.handler.getId();
    }

    public void dataLoadByDelay(int[] types) {
        this.dataLoadByDelay(types, 500L);
    }

    public void dataLoadByDelay(final int[] type, long time) {
        this.handler.postDelayed(new Runnable() {
            public void run() {
                MFragment.this.dataLoad(type);
            }
        }, time);
    }

    public void clearView() {
        if (this.contextView != null) {
            ViewParent vg = this.contextView.getParent();
            if (vg instanceof ViewGroup) {
                ((ViewGroup) vg).removeAllViewsInLayout();
            }
        }

    }

    public Object runLoad(int type, Object obj) {
        return null;
    }

    public void disposeMsg(int type, Object obj) {
    }

    public void dataLoad(int[] types) {
    }

    protected void destroy() {
    }

    protected void resume() {
    }

    protected void pause() {
    }

    protected abstract void create(Bundle var1);

    protected void initcreate(Bundle savedInstanceState) {
    }


    public void addOnFragmentCreateListener(OnFragmentCreateListener onCreate) {
        this.onFragmentCreateListenerss.add(onCreate);
    }

    public void removeOnFragmentCreateListener(OnFragmentCreateListener onCreate) {
        this.onFragmentCreateListenerss.remove(onCreate);
    }

    public View getContent() {
        return this.viewgroup;
    }

    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        return false;
    }

    @SuppressLint({"NewApi"})
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    public interface OnFragmentCreateListener {
        void onFragmentCreateListener(MFragment var1);
    }
}
