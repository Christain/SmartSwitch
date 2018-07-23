package com.chris.base.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chris.base.R;
import com.chris.base.superclass.SuperLazyFragment;

/**
 * ===============================
 * 描    述：
 * 作    者：Christain
 * 创建日期：2018/7/19 下午9:25
 * ===============================
 */
public class BaseMainFragment extends SuperLazyFragment {

    private TextView mTvTitle;
    private LinearLayout root;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.base_fragment_main);

        mTvTitle = (TextView) findViewById(R.id.tv_title);
        root = (LinearLayout) findViewById(R.id.root);

        Bundle bundle = getArguments();
        mTvTitle.setText(bundle.getString("TITLE", "Default"));

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
