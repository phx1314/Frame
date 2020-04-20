//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mdx.framework.widget.viewpagerindicator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewConfigurationCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.framework.R.attr;
import com.framework.R.bool;
import com.framework.R.color;
import com.framework.R.dimen;
import com.framework.R.integer;
import com.framework.R.styleable;

public class CirclePageIndicator extends View implements PageIndicator {
    private static final int INVALID_POINTER = -1;
    private float mRadius;
    private final Paint mPaintPageFill;
    private final Paint mPaintStroke;
    private final Paint mPaintFill;
    private ViewPager mViewPager;
    private OnPageChangeListener mListener;
    private int mCurrentPage;
    private int mSnapPage;
    private float mPageOffset;
    private int mScrollState;
    private int mOrientation;
    private boolean mCentered;
    private boolean mSnap;
    private int mTouchSlop;
    private float mLastMotionX;
    private int mActivePointerId;
    private boolean mIsDragging;

    public CirclePageIndicator(Context context) {
        this(context, (AttributeSet)null);
    }

    public CirclePageIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, attr.vpiCirclePageIndicatorStyle);
    }

    public CirclePageIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mPaintPageFill = new Paint(1);
        this.mPaintStroke = new Paint(1);
        this.mPaintFill = new Paint(1);
        this.mLastMotionX = -1.0F;
        this.mActivePointerId = -1;
        if(!this.isInEditMode()) {
            Resources res = this.getResources();
            int defaultPageColor = res.getColor(color.default_circle_indicator_page_color);
            int defaultFillColor = res.getColor(color.default_circle_indicator_fill_color);
            int defaultOrientation = res.getInteger(integer.default_circle_indicator_orientation);
            int defaultStrokeColor = res.getColor(color.default_circle_indicator_stroke_color);
            float defaultStrokeWidth = res.getDimension(dimen.default_circle_indicator_stroke_width);
            float defaultRadius = res.getDimension(dimen.default_circle_indicator_radius);
            boolean defaultCentered = res.getBoolean(bool.default_circle_indicator_centered);
            boolean defaultSnap = res.getBoolean(bool.default_circle_indicator_snap);
            TypedArray a = context.obtainStyledAttributes(attrs, styleable.CirclePageIndicator, defStyle, 0);
            this.mCentered = a.getBoolean(styleable.CirclePageIndicator_centered, defaultCentered);
            this.mOrientation = a.getInt(styleable.CirclePageIndicator_android_orientation, defaultOrientation);
            this.mPaintPageFill.setStyle(Style.FILL);
            this.mPaintPageFill.setColor(a.getColor(styleable.CirclePageIndicator_pageColor, defaultPageColor));
            this.mPaintStroke.setStyle(Style.STROKE);
            this.mPaintStroke.setColor(a.getColor(styleable.CirclePageIndicator_strokeColor, defaultStrokeColor));
            this.mPaintStroke.setStrokeWidth(a.getDimension(styleable.CirclePageIndicator_strokeWidth, defaultStrokeWidth));
            this.mPaintFill.setStyle(Style.FILL);
            this.mPaintFill.setColor(a.getColor(styleable.CirclePageIndicator_fillColor, defaultFillColor));
            this.mRadius = a.getDimension(styleable.CirclePageIndicator_radius, defaultRadius);
            this.mSnap = a.getBoolean(styleable.CirclePageIndicator_snap, defaultSnap);
            Drawable background = a.getDrawable(styleable.CirclePageIndicator_android_background);
            if(background != null) {
                this.setBackgroundDrawable(background);
            }

            a.recycle();
            ViewConfiguration configuration = ViewConfiguration.get(context);
            this.mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        }
    }

    public void setCentered(boolean centered) {
        this.mCentered = centered;
        this.invalidate();
    }

    public boolean isCentered() {
        return this.mCentered;
    }

    public void setPageColor(int pageColor) {
        this.mPaintPageFill.setColor(pageColor);
        this.invalidate();
    }

    public int getPageColor() {
        return this.mPaintPageFill.getColor();
    }

    public void setFillColor(int fillColor) {
        this.mPaintFill.setColor(fillColor);
        this.invalidate();
    }

    public int getFillColor() {
        return this.mPaintFill.getColor();
    }

    public void setOrientation(int orientation) {
        switch(orientation) {
            case 0:
            case 1:
                this.mOrientation = orientation;
                this.requestLayout();
                return;
            default:
                throw new IllegalArgumentException("Orientation must be either HORIZONTAL or VERTICAL.");
        }
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public void setStrokeColor(int strokeColor) {
        this.mPaintStroke.setColor(strokeColor);
        this.invalidate();
    }

    public int getStrokeColor() {
        return this.mPaintStroke.getColor();
    }

    public void setStrokeWidth(float strokeWidth) {
        this.mPaintStroke.setStrokeWidth(strokeWidth);
        this.invalidate();
    }

    public float getStrokeWidth() {
        return this.mPaintStroke.getStrokeWidth();
    }

    public void setRadius(float radius) {
        this.mRadius = radius;
        this.invalidate();
    }

    public float getRadius() {
        return this.mRadius;
    }

    public void setSnap(boolean snap) {
        this.mSnap = snap;
        this.invalidate();
    }

    public boolean isSnap() {
        return this.mSnap;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(this.mViewPager != null) {
            int count = this.mViewPager.getAdapter().getCount();
            if(count != 0) {
                if(this.mCurrentPage >= count) {
                    this.setCurrentItem(count - 1);
                } else {
                    int longSize;
                    int longPaddingBefore;
                    int longPaddingAfter;
                    int shortPaddingBefore;
                    if(this.mOrientation == 0) {
                        longSize = this.getWidth();
                        longPaddingBefore = this.getPaddingLeft();
                        longPaddingAfter = this.getPaddingRight();
                        shortPaddingBefore = this.getPaddingTop();
                    } else {
                        longSize = this.getHeight();
                        longPaddingBefore = this.getPaddingTop();
                        longPaddingAfter = this.getPaddingBottom();
                        shortPaddingBefore = this.getPaddingLeft();
                    }

                    float threeRadius = this.mRadius * 3.0F;
                    float shortOffset = (float)shortPaddingBefore + this.mRadius;
                    float longOffset = (float)longPaddingBefore + this.mRadius;
                    if(this.mCentered) {
                        longOffset += (float)(longSize - longPaddingBefore - longPaddingAfter) / 2.0F - (float)count * threeRadius / 2.0F;
                    }

                    float pageFillRadius = this.mRadius;
                    if(this.mPaintStroke.getStrokeWidth() > 0.0F) {
                        pageFillRadius -= this.mPaintStroke.getStrokeWidth() / 2.0F;
                    }

                    float dX;
                    float dY;
                    for(int cx = 0; cx < count; ++cx) {
                        float drawLong = longOffset + (float)cx * threeRadius;
                        if(this.mOrientation == 0) {
                            dX = drawLong;
                            dY = shortOffset;
                        } else {
                            dX = shortOffset;
                            dY = drawLong;
                        }

                        if(this.mPaintPageFill.getAlpha() > 0) {
                            canvas.drawCircle(dX, dY, pageFillRadius, this.mPaintPageFill);
                        }

                        if(pageFillRadius != this.mRadius) {
                            canvas.drawCircle(dX, dY, this.mRadius, this.mPaintStroke);
                        }
                    }

                    float var15 = (float)(this.mSnap?this.mSnapPage:this.mCurrentPage) * threeRadius;
                    if(!this.mSnap) {
                        var15 += this.mPageOffset * threeRadius;
                    }

                    if(this.mOrientation == 0) {
                        dX = longOffset + var15;
                        dY = shortOffset;
                    } else {
                        dX = shortOffset;
                        dY = longOffset + var15;
                    }

                    canvas.drawCircle(dX, dY, this.mRadius, this.mPaintFill);
                }
            }
        }
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent ev) {
        if(super.onTouchEvent(ev)) {
            return true;
        } else if(this.mViewPager != null && this.mViewPager.getAdapter().getCount() != 0) {
            int action = ev.getAction() & 255;
            int pointerIndex;
            int pointerId;
            float newPointerIndex1;
            switch(action) {
                case 0:
                    this.mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                    this.mLastMotionX = ev.getX();
                    break;
                case 1:
                case 3:
                    if(!this.mIsDragging) {
                        pointerIndex = this.mViewPager.getAdapter().getCount();
                        pointerId = this.getWidth();
                        newPointerIndex1 = (float)pointerId / 2.0F;
                        float sixthWidth = (float)pointerId / 6.0F;
                        if(this.mCurrentPage > 0 && ev.getX() < newPointerIndex1 - sixthWidth) {
                            if(action != 3) {
                                this.mViewPager.setCurrentItem(this.mCurrentPage - 1);
                            }

                            return true;
                        }

                        if(this.mCurrentPage < pointerIndex - 1 && ev.getX() > newPointerIndex1 + sixthWidth) {
                            if(action != 3) {
                                this.mViewPager.setCurrentItem(this.mCurrentPage + 1);
                            }

                            return true;
                        }
                    }

                    this.mIsDragging = false;
                    this.mActivePointerId = -1;
                    if(this.mViewPager.isFakeDragging()) {
                        this.mViewPager.endFakeDrag();
                    }
                    break;
                case 2:
                    pointerIndex = MotionEventCompat.findPointerIndex(ev, this.mActivePointerId);
                    float pointerId1 = MotionEventCompat.getX(ev, pointerIndex);
                    newPointerIndex1 = pointerId1 - this.mLastMotionX;
                    if(!this.mIsDragging && Math.abs(newPointerIndex1) > (float)this.mTouchSlop) {
                        this.mIsDragging = true;
                    }

                    if(this.mIsDragging) {
                        this.mLastMotionX = pointerId1;
                        if(this.mViewPager.isFakeDragging() || this.mViewPager.beginFakeDrag()) {
                            this.mViewPager.fakeDragBy(newPointerIndex1);
                        }
                    }
                case 4:
                default:
                    break;
                case 5:
                    pointerIndex = MotionEventCompat.getActionIndex(ev);
                    this.mLastMotionX = MotionEventCompat.getX(ev, pointerIndex);
                    this.mActivePointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
                    break;
                case 6:
                    pointerIndex = MotionEventCompat.getActionIndex(ev);
                    pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
                    if(pointerId == this.mActivePointerId) {
                        int newPointerIndex = pointerIndex == 0?1:0;
                        this.mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
                    }

                    this.mLastMotionX = MotionEventCompat.getX(ev, MotionEventCompat.findPointerIndex(ev, this.mActivePointerId));
            }

            return true;
        } else {
            return false;
        }
    }

    public void setViewPager(ViewPager view) {
        if(this.mViewPager != view) {
            if(this.mViewPager != null) {
                this.mViewPager.setOnPageChangeListener((OnPageChangeListener)null);
            }

            if(view.getAdapter() == null) {
                throw new IllegalStateException("ViewPager does not have adapter instance.");
            } else {
                this.mViewPager = view;
                this.mViewPager.setOnPageChangeListener(this);
                this.invalidate();
            }
        }
    }

    public void setViewPager(ViewPager view, int initialPosition) {
        this.setViewPager(view);
        this.setCurrentItem(initialPosition);
    }

    public void setCurrentItem(int item) {
        if(this.mViewPager != null) {
            this.mViewPager.setCurrentItem(item);
            this.mCurrentPage = item;
            this.invalidate();
        }
    }

    public void notifyDataSetChanged() {
        this.invalidate();
    }

    public void onPageScrollStateChanged(int state) {
        this.mScrollState = state;
        if(this.mListener != null) {
            this.mListener.onPageScrollStateChanged(state);
        }

    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        this.mCurrentPage = position;
        this.mPageOffset = positionOffset;
        this.invalidate();
        if(this.mListener != null) {
            this.mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

    }

    public void onPageSelected(int position) {
        if(this.mSnap || this.mScrollState == 0) {
            this.mCurrentPage = position;
            this.mSnapPage = position;
            this.invalidate();
        }

        if(this.mListener != null) {
            this.mListener.onPageSelected(position);
        }

    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mListener = listener;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(this.mOrientation == 0) {
            this.setMeasuredDimension(this.measureLong(widthMeasureSpec), this.measureShort(heightMeasureSpec));
        } else {
            this.setMeasuredDimension(this.measureShort(widthMeasureSpec), this.measureLong(heightMeasureSpec));
        }

    }

    private int measureLong(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result;
        if(specMode != 1073741824 && this.mViewPager != null) {
            int count = this.mViewPager.getAdapter().getCount();
            result = (int)((float)(this.getPaddingLeft() + this.getPaddingRight()) + (float)(count * 2) * this.mRadius + (float)(count - 1) * this.mRadius + 1.0F);
            if(specMode == -2147483648) {
                result = Math.min(result, specSize);
            }
        } else {
            result = specSize;
        }

        return result;
    }

    private int measureShort(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result;
        if(specMode == 1073741824) {
            result = specSize;
        } else {
            result = (int)(2.0F * this.mRadius + (float)this.getPaddingTop() + (float)this.getPaddingBottom() + 1.0F);
            if(specMode == -2147483648) {
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    public void onRestoreInstanceState(Parcelable state) {
        CirclePageIndicator.SavedState savedState = (CirclePageIndicator.SavedState)state;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.mCurrentPage = savedState.currentPage;
        this.mSnapPage = savedState.currentPage;
        this.requestLayout();
    }

    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        CirclePageIndicator.SavedState savedState = new CirclePageIndicator.SavedState(superState);
        savedState.currentPage = this.mCurrentPage;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        int currentPage;
        public static final Creator<CirclePageIndicator.SavedState> CREATOR = new Creator() {
            public CirclePageIndicator.SavedState createFromParcel(Parcel in) {
                return new CirclePageIndicator.SavedState(in);
            }

            public CirclePageIndicator.SavedState[] newArray(int size) {
                return new CirclePageIndicator.SavedState[size];
            }
        };

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.currentPage = in.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.currentPage);
        }
    }
}
