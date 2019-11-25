package com.mdx.framework.view.pullview;/*
 * Copyright (C) 2013 www.418log.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.OverScroller;
import android.widget.ProgressBar;

import com.mdx.framework.adapter.MAdapter;
import com.mdx.framework.util.AbViewUtil;
import com.mdx.framework.util.HttpResponseListener;
import com.mdx.framework.util.HttpResponseListenerSon;
import com.mdx.framework.util.HttpUtil;
import com.mdx.framework.view.listener.AbOnListListener;
import com.mdx.framework.view.listener.AbOnListViewListener;

// TODO: Auto-generated Javadoc

/**
 * The Class AbPullListView.
 */
public class AbPullListView extends ListView implements HttpResponseListenerSon, AbsListView.OnScrollListener {
    Object tag;
    /**
     * The m last y.
     */
    private float mLastY = -1;
    boolean isRefreash;
    /**
     * The m scroller.
     */
    private OverScroller mScroller;

    /**
     * The m list view listener.
     */
    private AbOnListViewListener mListViewListener;
    private AbOnListListener mAbOnListListener;

    /**
     * The m header view.
     */
    private AbListViewHeader mHeaderView;

    /**
     * The m footer view.
     */
    private AbListViewFooter mFooterView;

    /**
     * The m header view height.
     */
    private int mHeaderViewHeight;

    /**
     * The m footer view height.
     */
    private int mFooterViewHeight;

    /**
     * The m enable pull refresh.
     */
    private boolean mEnablePullRefresh = true;

    /**
     * The m enable pull load.
     */
    private boolean mEnablePullLoad = true;

    /**
     * The m pull refreshing.
     */
    private boolean mPullRefreshing = false;

    /**
     * The m pull loading.
     */
    private boolean mPullLoading;

    /**
     * The m is footer ready.
     */
    private boolean mIsFooterReady = false;

    /**
     * 总条数.
     */
    private int mTotalItemCount;

    /**
     * The m scroll back.
     */
    private int mScrollBack;

    /**
     * The Constant SCROLLBACK_HEADER.
     */
    private final static int SCROLLBACK_HEADER = 0;

    /**
     * The Constant SCROLLBACK_FOOTER.
     */
    private final static int SCROLLBACK_FOOTER = 1;

    /**
     * The Constant SCROLL_DURATION.
     */
    private final static int SCROLL_DURATION = 500;

    /**
     * The Constant OFFSET_RADIO.
     */
    private final static float OFFSET_RADIO = 1.8f;

    /**
     * 数据相关.
     */
    private MAdapter mAdapter = null;

    /**
     * 上一次的数量
     */
    private int count = 0;
    private String method;
    private String type = "POST";
    private String token = "";
    private Object[] mparams;
    public int PageSize = 20;
    public int PageIndex = 1;
    public int pageIndex = 1;
    public String PageIndex_key = "page";
    public String PageSize_key = "size";
    public Handler mHandler = new Handler();
    public Runnable runnable;

    /**
     * 构造.
     *
     * @param context the context
     */
    public AbPullListView(Context context) {
        super(context);
        initView(context);
    }

    public void setPageIndex(int pageIndex) {
        PageIndex = this.pageIndex = pageIndex;
    }

    public MAdapter getmAdapter() {
        return mAdapter;
    }

    public void setPageSize(int pageSize) {
        PageSize = pageSize;
    }

    public void setPageIndex_key(String pageIndex_key) {
        PageIndex_key = pageIndex_key;
    }

    public void setPageSize_key(String pageSize_key) {
        PageSize_key = pageSize_key;
    }

    /**
     * 构造.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public AbPullListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    /**
     * 初始化View.
     *
     * @param context the context
     */
    private void initView(Context context) {

        mScroller = new OverScroller(context, new DecelerateInterpolator());
        super.setOnScrollListener(this);

        // init header view
        mHeaderView = new AbListViewHeader(context);

        // init header height
        mHeaderViewHeight = mHeaderView.getHeaderHeight();
        mHeaderView.setGravity(Gravity.BOTTOM);
        addHeaderView(mHeaderView);

        // init footer view
        mFooterView = new AbListViewFooter(context);

        mFooterViewHeight = mFooterView.getFooterHeight();

        //默认是打开刷新与更多
        setPullRefreshEnable(true);
        setPullLoadEnable(true);
        setStyle();
        //先隐藏
        mFooterView.hide();
        runnable = new Runnable() {
            @Override
            public void run() {
                toXiala();
                reLoad();
            }
        };
    }

    /**
     * 描述：设置适配器
     */
    public void setAdapter(MAdapter adapter) {
        mAdapter = adapter;
        if (mIsFooterReady == false) {
            mIsFooterReady = true;
            mFooterView.setGravity(Gravity.TOP);
            addFooterView(mFooterView);
        }
        super.setAdapter(adapter);
    }

    /**
     * 打开或者关闭下拉刷新功能.
     *
     * @param enable 开关标记
     */
    public void setPullRefreshEnable(boolean enable) {
        mEnablePullRefresh = enable;
        if (!mEnablePullRefresh) {
            mHeaderView.setVisibility(View.INVISIBLE);
        } else {
            mHeaderView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 打开或者关闭加载更多功能.
     *
     * @param enable 开关标记
     */
    public void setPullLoadEnable(boolean enable) {
        mEnablePullLoad = enable;
        if (!mEnablePullLoad) {
            mFooterView.hide();
            mFooterView.setOnClickListener(null);
            if (getFooterView() != null) {
                removeFooterView(mFooterView);
            }
        } else {
            mPullLoading = false;
            if (getFooterView() == null) {
                addFooterView(mFooterView);
            }
            mFooterView.setState(AbListViewFooter.STATE_READY);
            //load more点击事件.
            mFooterView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startLoadMore();
                }
            });
        }
    }

    /**
     * 停止刷新并重置header的状态.
     */
    public void stopRefresh() {
        if (mPullRefreshing == true) {
            mPullRefreshing = false;
            resetHeaderHeight();
        }
        if (mAdapter != null)
            count = mAdapter.getCount();
        //判断有没有数据
        if (count > 0) {
            mFooterView.setState(AbListViewFooter.STATE_READY);
        } else {
            mFooterView.setState(AbListViewFooter.STATE_EMPTY);
        }
    }

    public void setProgressColor() {
        mHeaderView.setColor();
        mFooterView.setColor();
    }

    /**
     * 更新header的高度.
     *
     * @param delta 差的距离
     */
    private void updateHeaderHeight(float delta) {
        int newHeight = (int) delta + mHeaderView.getVisiableHeight();
        mHeaderView.setVisiableHeight(newHeight);
        if (mEnablePullRefresh && !mPullRefreshing) {
            if (mHeaderView.getVisiableHeight() >= mHeaderViewHeight) {
                mHeaderView.setState(AbListViewHeader.STATE_READY);
            } else {
                mHeaderView.setState(AbListViewHeader.STATE_NORMAL);
            }
        }
        setSelection(0);
    }

    /**
     * 根据状态设置Header的位置.
     */
    private void resetHeaderHeight() {
        if (mScroller.isFinished()) {
            //当前下拉到的高度
            int height = mHeaderView.getVisiableHeight();
            if (height < mHeaderViewHeight || !mPullRefreshing) {
                //距离短  隐藏
                mScrollBack = SCROLLBACK_HEADER;
                mScroller.startScroll(0, height, 0, -1 * height, SCROLL_DURATION);
            } else if (height >= mHeaderViewHeight || !mPullRefreshing) {
                //距离多的  弹回到mHeaderViewHeight
                mScrollBack = SCROLLBACK_HEADER;
                mScroller.startScroll(0, height, 0, -(height - mHeaderViewHeight), SCROLL_DURATION);
//            mScroller.startScroll(0, height, 0, -1 * height, SCROLL_DURATION);
            }
            invalidate();
        }
    }


    /**
     * 开始加载更多.
     */
    private void startLoadMore() {
        Log.d("TAG", "startLoadMore");
        mFooterView.show();
        mPullLoading = true;
        mFooterView.setState(AbListViewFooter.STATE_LOADING);
        if (mListViewListener != null) {
            loadData(false);
            //开始下载数据
//                mListViewListener.onLoadMore();
        }
    }

    /**
     * 停止加载更多并重置footer的状态.
     */
    public void stopLoadMore() {
        mFooterView.hide();
        mPullLoading = false;
        if (mAdapter != null) {
            int countNew = mAdapter.getCount();
            //判断有没有更多数据了
            if (countNew > count) {
                mFooterView.setState(AbListViewFooter.STATE_READY);
            } else {
                mFooterView.setState(AbListViewFooter.STATE_NO);
            }
        }
    }

    /**
     * 描述：onTouchEvent
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (mEnablePullRefresh && getFirstVisiblePosition() == 0 && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
                    updateHeaderHeight(deltaY / OFFSET_RADIO);
                } else if (mEnablePullLoad && !mPullLoading && getLastVisiblePosition() == mTotalItemCount - 1 && deltaY < 0) {
                    startLoadMore();
                }
                break;
            case MotionEvent.ACTION_UP:
                mLastY = -1;
                if (getFirstVisiblePosition() == 0) {
                    //需要刷新的条件
                    if (mEnablePullRefresh && mHeaderView.getVisiableHeight() >= mHeaderViewHeight) {
                        mPullRefreshing = true;
                        mHeaderView.setState(AbListViewHeader.STATE_REFRESHING);
                        if (mListViewListener != null) {
                            reLoad();
                            //刷新
//                            mListViewListener.onRefresh();
                        }
                        if (mAbOnListListener != null) {
                            mAbOnListListener.onRefresh();
                        }
                    }

                    if (mEnablePullRefresh) {
                        //弹回
                        resetHeaderHeight();
                    }
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void loadData(boolean isRefreash) {
        this.isRefreash = isRefreash;
        HttpUtil.load(getContext(), type, tag, token, method, new HttpResponseListener(getContext(), this, method, false)  , PageSize_key, PageSize + "", PageIndex_key, PageIndex + "",mparams);
    }

    public void reLoad() {
        setPullLoadEnable(true);
        PageIndex = this.pageIndex;
        loadData(true);
    }

    public void pullLoad() {
        setPullLoadEnable(true);
        mHandler.removeCallbacks(runnable);
        mHandler.postDelayed(runnable, 200);
    }


    public void stopAll() {
        stopRefresh();
        stopLoadMore();
    }

    public void stopLoad() {
        mHandler.removeCallbacks(runnable);
    }

    @Override
    public void onSuccess(String methodName, String content) {
        stopAll();
        PageIndex++;
        MAdapter mMAdapter = null;
        try {
            mMAdapter = mListViewListener.onSuccess(methodName, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mMAdapter != null) {
            if (mMAdapter.getCount() < PageSize) {
                setPullLoadEnable(false);
            }
//        stopLoadMore();
            if (mAdapter == null || isRefreash) {
                mAdapter = mMAdapter;
                setAdapter(mAdapter);
            } else {
                mAdapter.AddAll(mMAdapter);
            }
        }

    }


    public void setApiLoadParams(String method, String type, Object tag, String token, Object... mparams) {
        this.method = method;
        this.type = type;
        this.tag = tag;
        this.token = token;
        this.mparams = mparams;
        if (mEnablePullRefresh) {
            pullLoad();
        } else {
            reLoad();
        }
    }


    public void toXiala() {
        mPullRefreshing = true;
        mHeaderView.setState(AbListViewHeader.STATE_REFRESHING);
        mScroller.startScroll(0, 0, 0, (int) AbViewUtil.dip2px(getContext(), 50), SCROLL_DURATION);
        invalidate();//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界
    }

    /**
     * 描述：TODO
     *
     * @see View#computeScroll()
     */
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == SCROLLBACK_HEADER) {
                mHeaderView.setVisiableHeight(mScroller.getCurrY());
                if (!mPullRefreshing && mScroller.getCurrY() > 0) {
                    resetHeaderHeight();
                }
            }
            postInvalidate();
        }
        super.computeScroll();
    }

    /**
     * 描述：设置ListView的监听器.
     *
     * @param listViewListener
     */
    public void setAbOnListViewListener(AbOnListViewListener listViewListener) {
        mListViewListener = listViewListener;
    }

    public void setAbOnListListener(AbOnListListener listViewListener) {
        mAbOnListListener = listViewListener;
    }

    /**
     * 描述：TODO
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    /**
     * 描述：TODO
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mTotalItemCount = totalItemCount;
    }

    /**
     * 描述：获取Header View
     *
     * @return
     * @throws
     */
    public AbListViewHeader getHeaderView() {
        return mHeaderView;
    }

    /**
     * 描述：获取Footer View
     *
     * @return
     * @throws
     */
    public AbListViewFooter getFooterView() {
        return mFooterView;
    }

    /**
     * 描述：获取Header ProgressBar，用于设置自定义样式
     *
     * @return
     * @throws
     */
    public ProgressBar getHeaderProgressBar() {
        return mHeaderView.getHeaderProgressBar();
    }


    /**
     * 描述：获取Footer ProgressBar，用于设置自定义样式
     *
     * @return
     * @throws
     */
    public ProgressBar getFooterProgressBar() {
        return mFooterView.getFooterProgressBar();
    }

    void setStyle() {
//        mFooterView.getFooterProgressBar().setIndeterminateDrawable(
//                this.getResources().getDrawable(R.drawable.progress_circular));
//        mHeaderView.getHeaderProgressBar().setIndeterminateDrawable(
//                this.getResources().getDrawable(R.drawable.progress_circular));
    }

}
