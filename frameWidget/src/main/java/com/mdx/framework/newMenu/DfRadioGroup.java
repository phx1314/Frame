package com.mdx.framework.newMenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RadioGroup;

/**
 * 
 */
public class DfRadioGroup extends RadioGroup {
	DfCallback callback;

	public DfRadioGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DfRadioGroup(Context context) {
		super(context);
	}

	public void setCallback(DfCallback callback) {
		this.callback = callback;
	}

	public interface DfCallback {
		public boolean func();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (callback != null) {
			return callback.func();
		} else {
			return false;
		}

	}
}
