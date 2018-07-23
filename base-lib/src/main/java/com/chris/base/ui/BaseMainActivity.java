package com.chris.base.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chris.base.R;
import com.chris.base.superclass.SuperActivity;
import com.chris.base.util.DoubleClickExitUtil;
import com.chris.base.widget.viewpagerindicator.indicator.FixedIndicatorView;
import com.chris.base.widget.viewpagerindicator.indicator.IndicatorViewPager;
import com.chris.base.widget.viewpagerindicator.viewpager.ScrollViewPager;

/**
 * ===============================
 * 描    述：首页
 * 作    者：Christain
 * 创建日期：2018/7/19 下午8:40
 * ===============================
 */
public class BaseMainActivity extends SuperActivity {

    private DoubleClickExitUtil doubleClickExitHelper;
    private IndicatorViewPager indicatorViewPager;
    private ScrollViewPager mViewPager;
    private FixedIndicatorView mIndicatorView;
    private int indexTab = 0;
    private BaseMainFragment mFragmentOne, mFragmentTwo, mFragmentThree;

    private String[] tabNames = {"首页", "设备", "个人中心"};
    private int[] tabIcons = {R.drawable.base_main_tab_one_selector
            , R.drawable.base_main_tab_one_selector
            , R.drawable.base_main_tab_one_selector};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_main);

        mViewPager = (ScrollViewPager) findViewById(R.id.view_pager);
        mIndicatorView = (FixedIndicatorView) findViewById(R.id.indicator);

        if (savedInstanceState != null) {
            indexTab = savedInstanceState.getInt("INDEX_TAB");
        }

        doubleClickExitHelper = new DoubleClickExitUtil(this);

        initViewPager();
    }

    /**
     * 初始化切换卡
     */
    private void initViewPager() {
        indicatorViewPager = new IndicatorViewPager(mIndicatorView, mViewPager);
        mViewPager.setCanScroll(false);
        mViewPager.setOffscreenPageLimit(tabNames.length);
        mIndicatorView.setOnFrontItemClicklistener(new OnTabClickListener());
        indicatorViewPager.setAdapter(new HomeViewPagerAdapter(getSupportFragmentManager()));
        mViewPager.addOnPageChangeListener(new OnPageChangeListener());
        mViewPager.setCurrentItem(indexTab);
    }

    private class HomeViewPagerAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {

        public HomeViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return tabNames.length;
        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.base_item_main_tab, container, false);
            }
            ImageView ivTab = (ImageView) convertView.findViewById(R.id.iv_tab);
            TextView tvTab = (TextView) convertView.findViewById(R.id.tv_tab);
            ivTab.setImageResource(tabIcons[position]);
            tvTab.setText(tabNames[position]);

            return convertView;
        }

        @Override
        public Fragment getFragmentForPage(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    mFragmentOne = new BaseMainFragment();
                    fragment = mFragmentOne;
                    break;
                case 1:
                    mFragmentTwo = new BaseMainFragment();
                    fragment = mFragmentTwo;
                    break;
                case 2:
                    mFragmentThree = new BaseMainFragment();
                    fragment = mFragmentThree;
                    break;
            }
            Bundle bundle = new Bundle();
            bundle.putString("TITLE", tabNames[position]);
            fragment.setArguments(bundle);
            return fragment;
        }
    }

    /**
     * 点击MainTab监听
     */
    private class OnTabClickListener implements FixedIndicatorView.OnFrontItemClicklistener {

        @Override
        public boolean itemClicklistener(View selectItemView, int select, int preSelect) {
            //true不拦截，false拦截
            return true;
        }
    }

    /**
     * 切换卡改变监听
     */
    private class OnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

        @Override
        public void onPageSelected(int position) {
            indexTab = position;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            return doubleClickExitHelper.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (doubleClickExitHelper != null) {
            doubleClickExitHelper = null;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("INDEX_TAB", mViewPager.getCurrentItem());
        super.onSaveInstanceState(outState);
    }
}
