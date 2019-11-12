//
//  FrgPtDetail
//
//  Created by Administrator on 2015-01-28 13:49:35
//  Copyright (c) Administrator All rights reserved.

/**

 */

package com.mdx.framework.frg;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.framework.R;

import static com.mdx.framework.F.COLOR;
import static com.mdx.framework.F.mCallBackPt;


public class FrgPtDetail extends  BaseFrg {

    public WebView mWebView;
    public TextView tv_close;
    public TextView tv_title;
    public ImageButton btn_left;
    public LinearLayout mLinearLayout_back;
    public String url = "";
    public String title = "";
    public ImageView mImageView;
    public ImageButton btn_right_2;
    public ImageButton btn_right;
    public TextView mTextView_right;
    public RelativeLayout mRelativeLayout;
    public ProgressBar mProgressBar;

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.frg_pt_detail);
        url = getActivity().getIntent().getStringExtra("url");
        title = getActivity().getIntent().getStringExtra("title");
        initView();
        loaddata();
        setDarkStatusIcon(true);
    }

    /**
     * 说明：Android 6.0+ 状态栏图标原生反色操作
     */
    protected void setDarkStatusIcon(boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getActivity().getWindow().getDecorView();
            if (decorView == null) return;

            int vis = decorView.getSystemUiVisibility();
            if (dark) {
                vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            decorView.setSystemUiVisibility(vis);
        }
    }

    private void initView() {
        mLinearLayout_back = (LinearLayout) findViewById(R.id.mLinearLayout_back);
        tv_close = (TextView) findViewById(R.id.tv_close);
        tv_title = (TextView) findViewById(R.id.tv_title);
        btn_left = (ImageButton) findViewById(R.id.btn_left);
        mWebView = (WebView) findViewById(R.id.mWebView);
        mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.mRelativeLayout);
        tv_close.setTextColor(Color.parseColor(COLOR));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btn_left.getDrawable().setTint(Color.parseColor(COLOR));
            mProgressBar.getProgressDrawable().setTint(Color.parseColor(COLOR));
        }
        btn_left.setOnClickListener(this);
        mLinearLayout_back.setOnClickListener(this);
        if (title.equals("帮助中心") || title.equals("联系客服")) {
            tv_close.setVisibility(View.VISIBLE);
        }
    }

    public void loaddata() {
        tv_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FrgPtDetail.this.finish();
            }
        });
        tv_title.setText(title);
        mWebView.loadUrl(url);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    mProgressBar.setProgress(newProgress);//设置进度值
                }
            }
        });
        mWebView.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK
                            && mWebView.canGoBack()) { // 表示按返回键
                        mWebView.goBack(); // 后退
                        return true; // 已处理
                    }
                }
                return false;
            }
        });
//        mWebView.addJavascriptInterface(new JsInterface(getActivity()), "JinQuMobile");
    }

    private class JsInterface {
        private Context mContext;

        public JsInterface(Context context) {
            this.mContext = context;
        }

        @JavascriptInterface
        public void OpenImgBase64(String url) {
            mCallBackPt.Click(getContext(), url);
        }


    }

    @Override
    public void onClick(View arg0) {
        if (arg0.getId() == R.id.btn_left || arg0.getId() == R.id.mLinearLayout_back) {
            if (mWebView.canGoBack()) { // 表示
                // 按返回键
                mWebView.goBack(); // 后退
            } else {
                FrgPtDetail.this.finish();
            }
        }
    }


}