package com.chris.base.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chris.base.R;
import com.chris.base.superclass.SuperActivity;
import com.chris.base.util.BtnClickUtil;
import com.chris.base.util.PhoneUtil;
import com.chris.base.util.StatusBarUtil;
import com.chris.base.widget.roundview.RoundTextView;
import com.chris.base.widget.ultraviewpager.UltraViewPager;

import java.util.ArrayList;

/**
 * ===============================
 * 描    述：引导页
 * 作    者：Christain
 * 创建日期：2018/7/19 下午3:48
 * ===============================
 */
public class BaseGuideActivity extends SuperActivity implements View.OnClickListener {

    private UltraViewPager mViewPager;
    private RoundTextView mNext;
    private ArrayList<Integer> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_guide);

        StatusBarUtil.NormalMode(this);

        mViewPager = (UltraViewPager) findViewById(R.id.view_pager);
        mNext = (RoundTextView) findViewById(R.id.next);
        mNext.setOnClickListener(this);

        initData();
        initViewPager();
    }

    private void initData() {
        if (list == null) {
            list = new ArrayList<Integer>();
        }
        list.add(R.drawable.bg_guide_1);
        list.add(R.drawable.bg_guide_2);
        list.add(R.drawable.bg_guide_3);
    }

    private void initViewPager() {
        mViewPager.setOffscreenPageLimit(list.size());
        mViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        BaseGuidePagerAdapter adapter = new BaseGuidePagerAdapter(list);
        mViewPager.setAdapter(adapter);

        mViewPager.initIndicator();
        mViewPager.getIndicator()
                .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                .setFocusColor(Color.GREEN)
                .setNormalColor(Color.BLUE)
                .setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM)
                .setIndicatorPadding(PhoneUtil.dip2px(this, 5))
                .setMargin(0, 0, 0, PhoneUtil.dip2px(this, 30))
                .setRadius(PhoneUtil.dip2px(this, 4))
                .build();

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == list.size() - 1) {
                    if (mNext != null && !mNext.isShown()) {
                        mNext.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (mNext != null && mNext.isShown()) {
                        mNext.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (BtnClickUtil.isFastDoubleClick(v.getId())) {
            return;
        }
        if (isNeedShowLogin()) {
           //TODO 登录
           return;
        }
        //TODO 首页
        ARouter.getInstance().build("/home/main").navigation();
    }

    /**
     * 是否需要登录
     */
    private boolean isNeedShowLogin() {
        return false;
    }

}
