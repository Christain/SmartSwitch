package com.chris.base.widget.viewpagerindicator.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ScrollViewPager extends ViewPager {

	private boolean canScroll;

	public ScrollViewPager(Context context) {
		super(context);
		canScroll = false;
	}

	public ScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		canScroll = false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (canScroll) {
			try {
				return super.onInterceptTouchEvent(ev);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (canScroll) {
			return super.onTouchEvent(event);
		}
		return false;
	}

	public void toggleLock() {
		canScroll = !canScroll;
	}

	public void setCanScroll(boolean canScroll) {
		this.canScroll = canScroll;
	}

	public boolean isCanScroll() {
		return canScroll;
	}

}
