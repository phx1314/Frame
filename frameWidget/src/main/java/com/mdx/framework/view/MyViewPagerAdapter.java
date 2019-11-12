package com.mdx.framework.view;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

public class MyViewPagerAdapter extends PagerAdapter {
	private final List<View> mListViews;

	public MyViewPagerAdapter(List<View> mListViews) {
		this.mListViews = mListViews;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(mListViews.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(mListViews.get(position), 0);
		return mListViews.get(position);
	}

	@Override
	public int getCount() {
		return mListViews.size();
	}

	public View getItem(int position) {
		return mListViews.get(position);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
}