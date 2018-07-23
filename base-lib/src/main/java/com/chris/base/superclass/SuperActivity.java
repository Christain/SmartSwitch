package com.chris.base.superclass;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.chris.base.util.AppDavikActivityUtil;
import com.lzy.okgo.OkGo;

import org.greenrobot.eventbus.EventBus;

/**
 * ===============================
 * 描    述：Activity父类
 * 作    者：Christain
 * 创建日期：2017/5/8 16:40
 * ===============================
 */
public class SuperActivity extends AppCompatActivity {

    protected Activity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        AppDavikActivityUtil.getScreenManager().addActivity(this);
    }

    @Override
    public void finish() {
        AppDavikActivityUtil.getScreenManager().removeActivity(this);
        super.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppDavikActivityUtil.getScreenManager().removeActivity(this);
        OkGo.getInstance().cancelTag(this);
    }

    /**
     * 注册事件
     */
    protected void registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    /**
     * 解除事件
     */
    protected void unregisterEventBus() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    protected void startActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    protected void startActivity(Class clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) {
            intent.putExtra("BUNDLE", bundle);
        }
        startActivity(intent);
    }

    @Override
    public Resources getResources() {
        try {
            Resources res = super.getResources();
            Configuration config = new Configuration();
            config.setToDefaults();
            res.updateConfiguration(config, res.getDisplayMetrics());
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
