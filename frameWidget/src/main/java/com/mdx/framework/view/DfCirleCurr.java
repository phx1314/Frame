package com.mdx.framework.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.framework.R;
import com.mdx.framework.adapter.MAdapter;
import com.mdx.framework.adapter.MPagerAdapter;
import com.mdx.framework.widget.viewpagerindicator.CirclePageIndicator;

/**
 * [涓�彞璇濆姛鑳界畝杩癩<BR>
 * [鍔熻兘璇︾粏鎻忚堪]
 *
 * @author ryan
 * @version [2014骞�鏈�6鏃�涓嬪崍12:51:05]
 */
public class DfCirleCurr extends FrameLayout {
    protected CirclePageIndicator mIndicator;

    protected int transforms = 0;

    protected ViewPager mViewpager;

    protected boolean isAutoScroll = true;

    protected int interval = 5000, SCROLL_WHAT = 999, step = 1;

    protected MyHandler handler = new MyHandler();

    public DfCirleCurr(Context context) {
        super(context);
        init();
    }

    public DfCirleCurr(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DfCirleCurr(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    protected void init() {
        LayoutInflater f = LayoutInflater.from(getContext());
        f.inflate(R.layout.baner_viewpager_cirle, this);
        mViewpager = (ViewPager) findViewById(R.id.framework_banner_viewpager);
        mIndicator = (CirclePageIndicator) findViewById(R.id.framework_banner_indicator);
        sendScrollMessage(interval);
    }

    protected void sendScrollMessage(long delayTimeInMills) {
        if (isAutoScroll) {
            handler.removeMessages(SCROLL_WHAT);
            handler.sendEmptyMessageDelayed(SCROLL_WHAT, delayTimeInMills);
        }
    }

    @SuppressLint("HandlerLeak")
    protected class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if (isAutoScroll) {
                scrollOnce();
                sendScrollMessage(interval);
            }
        }
    }

    public void scrollOnce() {
        PagerAdapter mp = mViewpager.getAdapter();
        if (mp != null) {
            int c = mp.getCount();
            int i = mViewpager.getCurrentItem();
            if (c > 1 && i >= 0) {
                if (i == c - 1) {
                    step = -1;
                } else if (i == 0) {
                    step = 1;
                }
                mViewpager.setCurrentItem(step + i);
            }
        }
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener l) {
        mIndicator.setOnPageChangeListener(l);
    }

    public void setAdapter(PagerAdapter pageadapter) {
        if (pageadapter != null) {
            mViewpager.setAdapter(pageadapter);
            if (mIndicator != null) {
                mIndicator.setViewPager(mViewpager);
            }
        } else {
            if (mIndicator != null) {
                mIndicator.setViewPager(null);
            }
        }
    }

    public void setFillColor(int fillColor) {
        mIndicator.setFillColor(fillColor);
    }

    public void setPadding(int l, int t, int r, int b) {
        mIndicator.setPadding(l, t, r, b);
    }

    public void setRadious(float radious) {
        mIndicator.setRadius(radious);
    }

    public void setPageColor(int fillColor) {
        mIndicator.setPageColor(fillColor);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void setAdapter(MAdapter pageadapter) {
        if (pageadapter != null) {
            mViewpager.setAdapter(new MPagerAdapter(getContext(), pageadapter));
            if (mIndicator != null) {
                mIndicator.setViewPager(mViewpager);
            }
        } else {
            if (mIndicator != null) {
                mIndicator.setViewPager(null);
            }
        }
    }

    public boolean isAutoScroll() {
        return isAutoScroll;
    }

    public void setAutoScroll(boolean isAutoScroll) {
        this.isAutoScroll = isAutoScroll;
        if (isAutoScroll) {
            sendScrollMessage(interval);
        } else {
            handler.removeMessages(SCROLL_WHAT);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        sendScrollMessage(interval);
        return super.dispatchTouchEvent(ev);
    }

    public int getTransforms() {
        return transforms;
    }


}
