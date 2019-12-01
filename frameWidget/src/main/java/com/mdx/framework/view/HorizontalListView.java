package com.mdx.framework.view;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.EdgeEffectCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.mdx.framework.util.Util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class HorizontalListView extends AdapterView<ListAdapter> {
    private static final int INSERT_AT_END_OF_LIST = -1;
    private static final int INSERT_AT_START_OF_LIST = 0;
    public float mLastMotionX;
    private static final float FLING_DEFAULT_ABSORB_VELOCITY = 30.0F;
    private static final float FLING_FRICTION = 0.009F;
    private static final String BUNDLE_ID_CURRENT_X = "BUNDLE_ID_CURRENT_X";
    private static final String BUNDLE_ID_PARENT_STATE = "BUNDLE_ID_PARENT_STATE";
    protected Scroller mFlingTracker = new Scroller(this.getContext());
    private final HorizontalListView.GestureListener mGestureListener = new HorizontalListView.GestureListener( );
    private GestureDetector mGestureDetector;
    private int mDisplayOffset;
    protected ListAdapter mAdapter;
    private List<Queue<View>> mRemovedViewsCache = new ArrayList();
    private boolean mDataChanged = false;
    private Rect mRect = new Rect();
    private View mViewBeingTouched = null;
    private int mDividerWidth = 0;
    private Drawable mDivider = null;
    protected int mCurrentX;
    protected int mNextX;
    private Integer mRestoreX = null;
    private int mMaxX = 2147483647;
    private int mLeftViewAdapterIndex;
    private int mRightViewAdapterIndex;
    private int mCurrentlySelectedAdapterIndex;
    private HorizontalListView.RunningOutOfDataListener mRunningOutOfDataListener = null;
    private int mTouchSlop;
    private int mRunningOutOfDataThreshold = 0;
    private boolean mHasNotifiedRunningLowOnData = false;
    private HorizontalListView.OnScrollStateChangedListener mOnScrollStateChangedListener = null;
    private OnScrollListener mOnScrollListener = null;
    private HorizontalListView.OnScrollStateChangedListener.ScrollState mCurrentScrollState;
    private EdgeEffectCompat mEdgeGlowLeft;
    private EdgeEffectCompat mEdgeGlowRight;
    private int mHeightMeasureSpec;
    private boolean mBlockTouchAction;
    private boolean mIsParentVerticiallyScrollableViewDisallowingInterceptTouchEvent;
    private OnClickListener mOnClickListener;
    private DataSetObserver mAdapterDataObserver;
    private Runnable mDelayedLayout;

    public HorizontalListView(Context context) {
        super(context);
        this.mCurrentScrollState = HorizontalListView.OnScrollStateChangedListener.ScrollState.SCROLL_STATE_IDLE;
        this.mBlockTouchAction = false;
        this.mIsParentVerticiallyScrollableViewDisallowingInterceptTouchEvent = false;
        this.mAdapterDataObserver = new DataSetObserver() {
            public void onChanged() {
                HorizontalListView.this.mDataChanged = true;
                HorizontalListView.this.mHasNotifiedRunningLowOnData = false;
                HorizontalListView.this.unpressTouchedChild();
                HorizontalListView.this.invalidate();
                HorizontalListView.this.requestLayout();
            }

            public void onInvalidated() {
                HorizontalListView.this.mHasNotifiedRunningLowOnData = false;
                HorizontalListView.this.unpressTouchedChild();
                HorizontalListView.this.reset();
                HorizontalListView.this.invalidate();
                HorizontalListView.this.requestLayout();
            }
        };
        this.mDelayedLayout = new Runnable() {
            public void run() {
                HorizontalListView.this.requestLayout();
            }
        };
        this.init(context);
    }

    private void init(Context context) {
        ViewConfiguration configuration = ViewConfiguration.get(this.getContext());
        this.mTouchSlop = configuration.getScaledTouchSlop();
    }

    public HorizontalListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mCurrentScrollState = HorizontalListView.OnScrollStateChangedListener.ScrollState.SCROLL_STATE_IDLE;
        this.mBlockTouchAction = false;
        this.mIsParentVerticiallyScrollableViewDisallowingInterceptTouchEvent = false;
        this.mAdapterDataObserver = new DataSetObserver() {
            public void onChanged() {
                HorizontalListView.this.mDataChanged = true;
                HorizontalListView.this.mHasNotifiedRunningLowOnData = false;
                HorizontalListView.this.unpressTouchedChild();
                HorizontalListView.this.invalidate();
                HorizontalListView.this.requestLayout();
            }

            public void onInvalidated() {
                HorizontalListView.this.mHasNotifiedRunningLowOnData = false;
                HorizontalListView.this.unpressTouchedChild();
                HorizontalListView.this.reset();
                HorizontalListView.this.invalidate();
                HorizontalListView.this.requestLayout();
            }
        };
        this.mDelayedLayout = new Runnable() {
            public void run() {
                HorizontalListView.this.requestLayout();
            }
        };
        this.init(context);
        this.mEdgeGlowLeft = new EdgeEffectCompat(context);
        this.mEdgeGlowRight = new EdgeEffectCompat(context);
        this.mGestureDetector = new GestureDetector(context, this.mGestureListener);
        this.bindGestureDetector();
        this.initView();
        this.retrieveXmlConfiguration(context, attrs);
        this.setWillNotDraw(false);
        if(VERSION.SDK_INT >= 11) {
            HorizontalListView.HoneycombPlus.setFriction(this.mFlingTracker, 0.009F);
        }

    }

    @SuppressLint({"ClickableViewAccessibility"})
    private void bindGestureDetector() {
        OnTouchListener gestureListenerHandler = new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return HorizontalListView.this.mGestureDetector.onTouchEvent(event);
            }
        };
        this.setOnTouchListener(gestureListenerHandler);
    }

    private void requestParentListViewToNotInterceptTouchEvents(Boolean disallowIntercept) {
        if(this.mIsParentVerticiallyScrollableViewDisallowingInterceptTouchEvent != disallowIntercept.booleanValue()) {
            for(Object view = this; ((View)view).getParent() instanceof View; view = (View)((View)view).getParent()) {
                if(((View)view).getParent() instanceof ListView || ((View)view).getParent() instanceof ScrollView) {
                    ((View)view).getParent().requestDisallowInterceptTouchEvent(disallowIntercept.booleanValue());
                    this.mIsParentVerticiallyScrollableViewDisallowingInterceptTouchEvent = disallowIntercept.booleanValue();
                    return;
                }
            }
        }

    }

    private void retrieveXmlConfiguration(Context context, AttributeSet attrs) {
//        if(attrs != null) {
//            TypedArray a = context.obtainStyledAttributes(attrs, styleable.HorizontalListView);
//            Drawable d = a.getDrawable(styleable.HorizontalListView_android_divider);
//            if(d != null) {
//                this.setDivider(d);
//            }
//
//            int dividerWidth = a.getDimensionPixelSize(styleable.HorizontalListView_dividerWidth, 0);
//            if(dividerWidth != 0) {
//                this.setDividerWidth(dividerWidth);
//            }
//
//            a.recycle();
//        }

    }

    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("BUNDLE_ID_PARENT_STATE", super.onSaveInstanceState());
        bundle.putInt("BUNDLE_ID_CURRENT_X", this.mCurrentX);
        return bundle;
    }

    public void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle) {
            Bundle bundle = (Bundle)state;
            this.mRestoreX = Integer.valueOf(bundle.getInt("BUNDLE_ID_CURRENT_X"));
            super.onRestoreInstanceState(bundle.getParcelable("BUNDLE_ID_PARENT_STATE"));
        }

    }

    public void setDivider(Drawable divider) {
        this.mDivider = divider;
        if(divider != null) {
            this.setDividerWidth(divider.getIntrinsicWidth());
        } else {
            this.setDividerWidth(0);
        }

    }

    public void setDividerWidth(int width) {
        this.mDividerWidth = width;
        this.requestLayout();
        this.invalidate();
    }

    private void initView() {
        this.mLeftViewAdapterIndex = -1;
        this.mRightViewAdapterIndex = -1;
        this.mDisplayOffset = 0;
        this.mCurrentX = 0;
        this.mNextX = 0;
        this.mMaxX = 2147483647;
        this.setCurrentScrollState(HorizontalListView.OnScrollStateChangedListener.ScrollState.SCROLL_STATE_IDLE);
    }

    private void reset() {
        this.initView();
        this.removeAllViewsInLayout();
        this.requestLayout();
    }

    public void setSelection(int position) {
        this.mCurrentlySelectedAdapterIndex = position;
    }

    public View getSelectedView() {
        return this.getChild(this.mCurrentlySelectedAdapterIndex);
    }

    public void setAdapter(ListAdapter adapter) {
        if(this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mAdapterDataObserver);
        }

        if(adapter != null) {
            this.mHasNotifiedRunningLowOnData = false;
            this.mAdapter = adapter;
            this.mAdapter.registerDataSetObserver(this.mAdapterDataObserver);
        }

        this.initializeRecycledViewCache(this.mAdapter.getViewTypeCount());
        this.reset();
    }

    public ListAdapter getAdapter() {
        return this.mAdapter;
    }

    private void initializeRecycledViewCache(int viewTypeCount) {
        this.mRemovedViewsCache.clear();

        for(int i = 0; i < viewTypeCount; ++i) {
            this.mRemovedViewsCache.add(new LinkedList());
        }

    }

    private View getRecycledView(int adapterIndex) {
        int itemViewType = this.mAdapter.getItemViewType(adapterIndex);
        return this.isItemViewTypeValid(itemViewType)?(View)((Queue)this.mRemovedViewsCache.get(itemViewType)).poll():null;
    }

    private void recycleView(int adapterIndex, View view) {
        int itemViewType = this.mAdapter.getItemViewType(adapterIndex);
        if(this.isItemViewTypeValid(itemViewType)) {
            ((Queue)this.mRemovedViewsCache.get(itemViewType)).offer(view);
        }

    }

    private boolean isItemViewTypeValid(int itemViewType) {
        return itemViewType < this.mRemovedViewsCache.size();
    }

    private void addAndMeasureChild(View child, int viewPos) {
        LayoutParams params = this.getLayoutParams(child);
        this.addViewInLayout(child, viewPos, params, true);
        this.measureChild(child);
    }

    private void measureChild(View child) {
        LayoutParams childLayoutParams = this.getLayoutParams(child);
        int childHeightSpec = ViewGroup.getChildMeasureSpec(this.mHeightMeasureSpec, this.getPaddingTop() + this.getPaddingBottom(), childLayoutParams.height);
        int childWidthSpec;
        if(childLayoutParams.width > 0) {
            childWidthSpec = MeasureSpec.makeMeasureSpec(childLayoutParams.width, 1073741824);
        } else {
            childWidthSpec = MeasureSpec.makeMeasureSpec(0, 0);
        }

        child.measure(childWidthSpec, childHeightSpec);
    }

    private LayoutParams getLayoutParams(View child) {
        LayoutParams layoutParams = child.getLayoutParams();
        if(layoutParams == null) {
            layoutParams = new LayoutParams(-2, -1);
        }

        return layoutParams;
    }

    @SuppressLint({"WrongCall"})
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(this.mAdapter != null) {
            this.invalidate();
            int dx;
            if(this.mDataChanged) {
                dx = this.mCurrentX;
                this.initView();
                this.removeAllViewsInLayout();
                this.mNextX = dx;
                this.mDataChanged = false;
            }

            if(this.mRestoreX != null) {
                this.mNextX = this.mRestoreX.intValue();
                this.mRestoreX = null;
            }

            if(this.mFlingTracker.computeScrollOffset()) {
                this.mNextX = this.mFlingTracker.getCurrX();
            }

            if(this.mNextX < 0) {
                this.mNextX = 0;
                if(this.mEdgeGlowLeft.isFinished()) {
                    this.mEdgeGlowLeft.onAbsorb((int)this.determineFlingAbsorbVelocity());
                }

                this.mFlingTracker.forceFinished(true);
                this.setCurrentScrollState(HorizontalListView.OnScrollStateChangedListener.ScrollState.SCROLL_STATE_IDLE);
            } else if(this.mNextX > this.mMaxX) {
                this.mNextX = this.mMaxX;
                if(this.mEdgeGlowRight.isFinished()) {
                    this.mEdgeGlowRight.onAbsorb((int)this.determineFlingAbsorbVelocity());
                }

                this.mFlingTracker.forceFinished(true);
                this.setCurrentScrollState(HorizontalListView.OnScrollStateChangedListener.ScrollState.SCROLL_STATE_IDLE);
            }

            dx = this.mCurrentX - this.mNextX;
            this.removeNonVisibleChildren(dx);
            this.fillList(dx);
            this.positionChildren(dx);
            this.mCurrentX = this.mNextX;
            if(this.determineMaxX()) {
                this.onLayout(changed, left, top, right, bottom);
            } else {
                if(this.mFlingTracker.isFinished()) {
                    if(this.mCurrentScrollState == HorizontalListView.OnScrollStateChangedListener.ScrollState.SCROLL_STATE_FLING) {
                        this.setCurrentScrollState(HorizontalListView.OnScrollStateChangedListener.ScrollState.SCROLL_STATE_IDLE);
                    }
                } else {
                    ViewCompat.postOnAnimation(this, this.mDelayedLayout);
                }

            }
        }
    }

    protected float getLeftFadingEdgeStrength() {
        int horizontalFadingEdgeLength = this.getHorizontalFadingEdgeLength();
        return this.mCurrentX == 0?0.0F:(this.mCurrentX < horizontalFadingEdgeLength?(float)this.mCurrentX / (float)horizontalFadingEdgeLength:1.0F);
    }

    protected float getRightFadingEdgeStrength() {
        int horizontalFadingEdgeLength = this.getHorizontalFadingEdgeLength();
        return this.mCurrentX == this.mMaxX?0.0F:(this.mMaxX - this.mCurrentX < horizontalFadingEdgeLength?(float)(this.mMaxX - this.mCurrentX) / (float)horizontalFadingEdgeLength:1.0F);
    }

    private float determineFlingAbsorbVelocity() {
        return VERSION.SDK_INT >= 14?this.mFlingTracker.getCurrVelocity():30.0F;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.mHeightMeasureSpec = heightMeasureSpec;
    }

    private boolean determineMaxX() {
        if(this.isLastItemInAdapter(this.mRightViewAdapterIndex)) {
            View rightView = this.getRightmostChild();
            if(rightView != null) {
                int oldMaxX = this.mMaxX;
                this.mMaxX = this.mCurrentX + (rightView.getRight() - this.getPaddingLeft()) - this.getRenderWidth();
                if(this.mMaxX < 0) {
                    this.mMaxX = 0;
                }

                if(this.mMaxX != oldMaxX) {
                    return true;
                }
            }
        }

        return false;
    }

    private void fillList(int dx) {
        int edge = 0;
        View child = this.getRightmostChild();
        if(child != null) {
            edge = child.getRight();
        }

        this.fillListRight(edge, dx);
        edge = 0;
        child = this.getLeftmostChild();
        if(child != null) {
            edge = child.getLeft();
        }

        this.fillListLeft(edge, dx);
        if(this.mOnScrollListener != null) {
            this.mOnScrollListener.onScroll((AbsListView)null, this.mLeftViewAdapterIndex, this.mRightViewAdapterIndex - this.mLeftViewAdapterIndex + 1, this.getAdapter().getCount());
        }

    }

    private void removeNonVisibleChildren(int dx) {
        View child;
        for(child = this.getLeftmostChild(); child != null && child.getRight() + dx <= 0; child = this.getLeftmostChild()) {
            this.mDisplayOffset += this.isLastItemInAdapter(this.mLeftViewAdapterIndex)?child.getMeasuredWidth():this.mDividerWidth + child.getMeasuredWidth();
            this.recycleView(this.mLeftViewAdapterIndex, child);
            this.removeViewInLayout(child);
            ++this.mLeftViewAdapterIndex;
        }

        for(child = this.getRightmostChild(); child != null && child.getLeft() + dx >= this.getWidth(); child = this.getRightmostChild()) {
            this.recycleView(this.mRightViewAdapterIndex, child);
            this.removeViewInLayout(child);
            --this.mRightViewAdapterIndex;
        }

    }

    private void fillListRight(int rightEdge, int dx) {
        while(rightEdge + dx + this.mDividerWidth < this.getWidth() && this.mRightViewAdapterIndex + 1 < this.mAdapter.getCount()) {
            ++this.mRightViewAdapterIndex;
            if(this.mLeftViewAdapterIndex < 0) {
                this.mLeftViewAdapterIndex = this.mRightViewAdapterIndex;
            }

            View child = this.mAdapter.getView(this.mRightViewAdapterIndex, this.getRecycledView(this.mRightViewAdapterIndex), this);
            this.addAndMeasureChild(child, -1);
            rightEdge += (this.mRightViewAdapterIndex == 0?0:this.mDividerWidth) + child.getMeasuredWidth();
            this.determineIfLowOnData();
        }

    }

    private void fillListLeft(int leftEdge, int dx) {
        while(leftEdge + dx - this.mDividerWidth > 0 && this.mLeftViewAdapterIndex >= 1) {
            --this.mLeftViewAdapterIndex;
            View child = this.mAdapter.getView(this.mLeftViewAdapterIndex, this.getRecycledView(this.mLeftViewAdapterIndex), this);
            this.addAndMeasureChild(child, 0);
            leftEdge -= this.mLeftViewAdapterIndex == 0?child.getMeasuredWidth():this.mDividerWidth + child.getMeasuredWidth();
            this.mDisplayOffset -= leftEdge + dx == 0?child.getMeasuredWidth():this.mDividerWidth + child.getMeasuredWidth();
        }

    }

    private void positionChildren(int dx) {
        int childCount = this.getChildCount();
        if(childCount > 0) {
            this.mDisplayOffset += dx;
            int leftOffset = this.mDisplayOffset;

            for(int i = 0; i < childCount; ++i) {
                View child = this.getChildAt(i);
                int left = leftOffset + this.getPaddingLeft();
                int top = this.getPaddingTop();
                int right = left + child.getMeasuredWidth();
                int bottom = top + child.getMeasuredHeight();
                child.layout(left, top, right, bottom);
                leftOffset += child.getMeasuredWidth() + this.mDividerWidth;
            }
        }

    }

    private View getLeftmostChild() {
        return this.getChildAt(0);
    }

    private View getRightmostChild() {
        return this.getChildAt(this.getChildCount() - 1);
    }

    private View getChild(int adapterIndex) {
        return adapterIndex >= this.mLeftViewAdapterIndex && adapterIndex <= this.mRightViewAdapterIndex?this.getChildAt(adapterIndex - this.mLeftViewAdapterIndex):null;
    }

    private int getChildIndex(int x, int y) {
        int childCount = this.getChildCount();

        for(int index = 0; index < childCount; ++index) {
            this.getChildAt(index).getHitRect(this.mRect);
            if(this.mRect.contains(x, y)) {
                return index;
            }
        }

        return -1;
    }

    private boolean isLastItemInAdapter(int index) {
        return index == this.mAdapter.getCount() - 1;
    }

    private int getRenderHeight() {
        return this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
    }

    private int getRenderWidth() {
        return this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
    }

    public void scrollTo(int x) {
        this.mFlingTracker.startScroll(this.mNextX, 0, x - this.mNextX, 0);
        this.setCurrentScrollState(HorizontalListView.OnScrollStateChangedListener.ScrollState.SCROLL_STATE_FLING);
        this.requestLayout();
    }

    public int getFirstVisiblePosition() {
        return this.mLeftViewAdapterIndex;
    }

    public int getLastVisiblePosition() {
        return this.mRightViewAdapterIndex;
    }

    private void drawEdgeGlow(Canvas canvas) {
        int restoreCount;
        int width;
        if(this.mEdgeGlowLeft != null && !this.mEdgeGlowLeft.isFinished() && this.isEdgeGlowEnabled()) {
            restoreCount = canvas.save();
            width = this.getHeight();
            canvas.rotate(-90.0F, 0.0F, 0.0F);
            canvas.translate((float)(-width + this.getPaddingBottom()), 0.0F);
            this.mEdgeGlowLeft.setSize(this.getRenderHeight(), this.getRenderWidth());
            if(this.mEdgeGlowLeft.draw(canvas)) {
                this.invalidate();
            }

            canvas.restoreToCount(restoreCount);
        } else if(this.mEdgeGlowRight != null && !this.mEdgeGlowRight.isFinished() && this.isEdgeGlowEnabled()) {
            restoreCount = canvas.save();
            width = this.getWidth();
            canvas.rotate(90.0F, 0.0F, 0.0F);
            canvas.translate((float)this.getPaddingTop(), (float)(-width));
            this.mEdgeGlowRight.setSize(this.getRenderHeight(), this.getRenderWidth());
            if(this.mEdgeGlowRight.draw(canvas)) {
                this.invalidate();
            }

            canvas.restoreToCount(restoreCount);
        }

    }

    private void drawDividers(Canvas canvas) {
        int count = this.getChildCount();
        Rect bounds = this.mRect;
        this.mRect.top = this.getPaddingTop();
        this.mRect.bottom = this.mRect.top + this.getRenderHeight();

        for(int i = 0; i < count; ++i) {
            if(i != count - 1 || !this.isLastItemInAdapter(this.mRightViewAdapterIndex)) {
                View child = this.getChildAt(i);
                bounds.left = child.getRight();
                bounds.right = child.getRight() + this.mDividerWidth;
                if(bounds.left < this.getPaddingLeft()) {
                    bounds.left = this.getPaddingLeft();
                }

                if(bounds.right > this.getWidth() - this.getPaddingRight()) {
                    bounds.right = this.getWidth() - this.getPaddingRight();
                }

                this.drawDivider(canvas, bounds);
                if(i == 0 && child.getLeft() > this.getPaddingLeft()) {
                    bounds.left = this.getPaddingLeft();
                    bounds.right = child.getLeft();
                    this.drawDivider(canvas, bounds);
                }
            }
        }

    }

    private void drawDivider(Canvas canvas, Rect bounds) {
        if(this.mDivider != null) {
            this.mDivider.setBounds(bounds);
            this.mDivider.draw(canvas);
        }

    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.drawDividers(canvas);
    }

    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        this.drawEdgeGlow(canvas);
    }

    protected void dispatchSetPressed(boolean pressed) {
    }

    protected boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        this.mFlingTracker.fling(this.mNextX, 0, (int)(-velocityX), 0, 0, this.mMaxX, 0, 0);
        this.setCurrentScrollState(HorizontalListView.OnScrollStateChangedListener.ScrollState.SCROLL_STATE_FLING);
        this.requestLayout();
        return true;
    }

    protected boolean onDown(MotionEvent e) {
        this.mBlockTouchAction = !this.mFlingTracker.isFinished();
        this.mFlingTracker.forceFinished(true);
        this.setCurrentScrollState(HorizontalListView.OnScrollStateChangedListener.ScrollState.SCROLL_STATE_IDLE);
        this.unpressTouchedChild();
        if(!this.mBlockTouchAction) {
            int index = this.getChildIndex((int)e.getX(), (int)e.getY());
            if(index >= 0) {
                this.mViewBeingTouched = this.getChildAt(index);
                if(this.mViewBeingTouched != null) {
                    this.mViewBeingTouched.setPressed(true);
                    this.refreshDrawableState();
                }
            }
        }

        return true;
    }

    private void unpressTouchedChild() {
        if(this.mViewBeingTouched != null) {
            this.mViewBeingTouched.setPressed(false);
            this.refreshDrawableState();
            this.mViewBeingTouched = null;
        }

    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == 1) {
            Log.d("touch", "touch to ACTION_UP");
            Util.setScrollAbleParent(this, true);
        } else if(ev.getAction() == 3) {
            Log.d("touch", "touch to ACTION_CANCEL");
            Util.setScrollAbleParent(this, true);
        } else if(ev.getAction() == 0) {
            Util.setScrollAbleParent(this, false);
            Log.d("touch", "touch to ACTION_DOWN");
        }

        return super.dispatchTouchEvent(ev);
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == 1) {
            if(this.mFlingTracker == null || this.mFlingTracker.isFinished()) {
                this.setCurrentScrollState(HorizontalListView.OnScrollStateChangedListener.ScrollState.SCROLL_STATE_IDLE);
            }

            this.requestParentListViewToNotInterceptTouchEvents(Boolean.valueOf(false));
            this.releaseEdgeGlow();
        } else if(event.getAction() == 3) {
            this.unpressTouchedChild();
            this.releaseEdgeGlow();
            this.requestParentListViewToNotInterceptTouchEvents(Boolean.valueOf(false));
        } else if(event.getAction() == 0) {
            ;
        }

        return super.onTouchEvent(event);
    }

    private void releaseEdgeGlow() {
        if(this.mEdgeGlowLeft != null) {
            this.mEdgeGlowLeft.onRelease();
        }

        if(this.mEdgeGlowRight != null) {
            this.mEdgeGlowRight.onRelease();
        }

    }

    public void setRunningOutOfDataListener(HorizontalListView.RunningOutOfDataListener listener, int numberOfItemsLeftConsideredLow) {
        this.mRunningOutOfDataListener = listener;
        this.mRunningOutOfDataThreshold = numberOfItemsLeftConsideredLow;
    }

    private void determineIfLowOnData() {
        if(this.mRunningOutOfDataListener != null && this.mAdapter != null && this.mAdapter.getCount() - (this.mRightViewAdapterIndex + 1) < this.mRunningOutOfDataThreshold && !this.mHasNotifiedRunningLowOnData) {
            this.mHasNotifiedRunningLowOnData = true;
            this.mRunningOutOfDataListener.onRunningOutOfData();
        }

    }

    public void setOnClickListener(OnClickListener listener) {
        this.mOnClickListener = listener;
    }

    public void setOnScrollStateChangedListener(HorizontalListView.OnScrollStateChangedListener listener) {
        this.mOnScrollStateChangedListener = listener;
    }

    public void setOnScrollListener(OnScrollListener listener) {
        this.mOnScrollListener = listener;
    }

    private void setCurrentScrollState(HorizontalListView.OnScrollStateChangedListener.ScrollState newScrollState) {
        if(this.mCurrentScrollState != newScrollState && this.mOnScrollStateChangedListener != null) {
            this.mOnScrollStateChangedListener.onScrollStateChanged(newScrollState);
        }

        this.mCurrentScrollState = newScrollState;
    }

    private void updateOverscrollAnimation(int scrolledOffset) {
        if(this.mEdgeGlowLeft != null && this.mEdgeGlowRight != null) {
            int nextScrollPosition = this.mCurrentX + scrolledOffset;
            if(this.mFlingTracker == null || this.mFlingTracker.isFinished()) {
                int overscroll;
                if(nextScrollPosition < 0) {
                    overscroll = Math.abs(scrolledOffset);
                    this.mEdgeGlowLeft.onPull((float)overscroll / (float)this.getRenderWidth());
                    if(!this.mEdgeGlowRight.isFinished()) {
                        this.mEdgeGlowRight.onRelease();
                    }
                } else if(nextScrollPosition > this.mMaxX) {
                    overscroll = Math.abs(scrolledOffset);
                    this.mEdgeGlowRight.onPull((float)overscroll / (float)this.getRenderWidth());
                    if(!this.mEdgeGlowLeft.isFinished()) {
                        this.mEdgeGlowLeft.onRelease();
                    }
                }
            }

        }
    }

    private boolean isEdgeGlowEnabled() {
        return this.mAdapter != null && !this.mAdapter.isEmpty()?this.mMaxX > 0:false;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        float x;
        switch(action & 255) {
            case 0:
                if(!this.mFlingTracker.isFinished()) {
                    this.mFlingTracker.abortAnimation();
                }

                x = ev.getX();
                this.mLastMotionX = x;
            case 1:
            case 3:
            case 4:
            case 5:
            case 6:
            default:
                break;
            case 2:
                x = ev.getX(0);
                int xDiff = (int)Math.abs(x - this.mLastMotionX);
                if(xDiff > this.mTouchSlop) {
                    this.mGestureDetector.onTouchEvent(this.getMe(ev, 0));
                    return true;
                }
        }

        return super.onInterceptTouchEvent(ev);
    }

    @SuppressLint({"Recycle"})
    private MotionEvent getMe(MotionEvent ev, int action) {
        return MotionEvent.obtain(ev.getDownTime(), ev.getEventTime(), action, ev.getX(), ev.getY(), ev.getPressure(), ev.getSize(), ev.getMetaState(), ev.getXPrecision(), ev.getYPrecision(), ev.getDeviceId(), ev.getEdgeFlags());
    }

    @TargetApi(14)
    private static final class IceCreamSandwichPlus {
        private IceCreamSandwichPlus() {
        }

        public static float getCurrVelocity(Scroller scroller) {
            return scroller.getCurrVelocity();
        }

        static {
            if(VERSION.SDK_INT < 14) {
                throw new RuntimeException("Should not get to IceCreamSandwichPlus class unless sdk is >= 14!");
            }
        }
    }

    @TargetApi(11)
    private static final class HoneycombPlus {
        private HoneycombPlus() {
        }

        public static void setFriction(Scroller scroller, float friction) {
            if(scroller != null) {
                scroller.setFriction(friction);
            }

        }

        static {
            if(VERSION.SDK_INT < 11) {
                throw new RuntimeException("Should not get to HoneycombPlus class unless sdk is >= 11!");
            }
        }
    }

    public interface OnScrollStateChangedListener {
        void onScrollStateChanged(HorizontalListView.OnScrollStateChangedListener.ScrollState var1);

        public static enum ScrollState {
            SCROLL_STATE_IDLE,
            SCROLL_STATE_TOUCH_SCROLL,
            SCROLL_STATE_FLING;

            private ScrollState() {
            }
        }
    }

    public interface RunningOutOfDataListener {
        void onRunningOutOfData();
    }

    private class GestureListener extends SimpleOnGestureListener {
        private GestureListener() {
        }

        public boolean onDown(MotionEvent e) {
            return HorizontalListView.this.onDown(e);
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return HorizontalListView.this.onFling(e1, e2, velocityX, velocityY);
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            HorizontalListView.this.requestParentListViewToNotInterceptTouchEvents(Boolean.valueOf(true));
            HorizontalListView.this.setCurrentScrollState(HorizontalListView.OnScrollStateChangedListener.ScrollState.SCROLL_STATE_TOUCH_SCROLL);
            HorizontalListView.this.unpressTouchedChild();
            HorizontalListView.this.mNextX += (int)distanceX;
            HorizontalListView.this.updateOverscrollAnimation(Math.round(distanceX));
            HorizontalListView.this.requestLayout();
            return true;
        }

        public boolean onSingleTapConfirmed(MotionEvent e) {
            HorizontalListView.this.unpressTouchedChild();
            OnItemClickListener onItemClickListener = HorizontalListView.this.getOnItemClickListener();
            int index = HorizontalListView.this.getChildIndex((int)e.getX(), (int)e.getY());
            if(index >= 0 && !HorizontalListView.this.mBlockTouchAction) {
                View child = HorizontalListView.this.getChildAt(index);
                int adapterIndex = HorizontalListView.this.mLeftViewAdapterIndex + index;
                if(onItemClickListener != null) {
                    onItemClickListener.onItemClick(HorizontalListView.this, child, adapterIndex, HorizontalListView.this.mAdapter.getItemId(adapterIndex));
                    return true;
                }
            }

            if(HorizontalListView.this.mOnClickListener != null && !HorizontalListView.this.mBlockTouchAction) {
                HorizontalListView.this.mOnClickListener.onClick(HorizontalListView.this);
            }

            return false;
        }

        public void onLongPress(MotionEvent e) {
            HorizontalListView.this.unpressTouchedChild();
            int index = HorizontalListView.this.getChildIndex((int)e.getX(), (int)e.getY());
            if(index >= 0 && !HorizontalListView.this.mBlockTouchAction) {
                View child = HorizontalListView.this.getChildAt(index);
                OnItemLongClickListener onItemLongClickListener = HorizontalListView.this.getOnItemLongClickListener();
                if(onItemLongClickListener != null) {
                    int adapterIndex = HorizontalListView.this.mLeftViewAdapterIndex + index;
                    boolean handled = onItemLongClickListener.onItemLongClick(HorizontalListView.this, child, adapterIndex, HorizontalListView.this.mAdapter.getItemId(adapterIndex));
                    if(handled) {
                        HorizontalListView.this.performHapticFeedback(0);
                    }
                }
            }

        }
    }
}
