package com.chris.base.ui;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

import com.chris.base.Constants;
import com.chris.base.superclass.SuperActivity;
import com.chris.base.util.AppDataPrefrenceUtil;
import com.chris.base.util.HandlerUtil;
import com.chris.base.util.PhoneUtil;
import com.chris.base.util.StatusBarUtil;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * ===============================
 * 描    述：通用启动页
 * 作    者：Christain
 * 创建日期：2018/7/19 上午11:19
 * ===============================
 */
public class BaseStartActivity extends SuperActivity implements EasyPermissions.PermissionCallbacks {

    private static final int REQUESTCODE = 100; //权限申请Code标识
    private int splashTime = 2000;              //停留页面时间

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.NormalMode(this);
        //启动时，需要申请的权限
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            start();
            return;
        }
        EasyPermissions.requestPermissions(this, "有未获得的权限，应用可能无法正常使用", REQUESTCODE, perms);
    }

    /**
     * 权限处理完回调此方法，作为初始化入口
     * 注意：方法不能有参数
     */
    @AfterPermissionGranted(REQUESTCODE)
    private void start() {
        //TODO 业务和数据初始化
        HandlerUtil.runOnUiThreadDelay(mRunnable, splashTime);
    }

    /**
     * 跳转路由
     */
    private void routerTo() {
        //引导页
        if (isNeedShowGuide()) {
            startActivity(BaseGuideActivity.class);
            finish();
            return;
        }
        //登录页
        if (isNeedShowLogin()) {
            return;
        }
        //首页
        startActivity(BaseMainActivity.class);
        finish();
    }

    /**
     * 是否需要显示引导页
     */
    private boolean isNeedShowGuide() {
        int currentVersionCode = PhoneUtil.getVersionInfo(this).versionCode;
        int lastVersionCode = AppDataPrefrenceUtil.getInstance().getInt(Constants.SP_VERSION_CODE, 0);
        if (currentVersionCode > lastVersionCode) {
            AppDataPrefrenceUtil.getInstance().put(Constants.SP_VERSION_CODE, currentVersionCode);
            return true;
        }
        return true;
    }

    /**
     * 是否需要登录
     */
    private boolean isNeedShowLogin() {
        return false;
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            routerTo();
        }
    };

    /**
     * 权限申请成功回调
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    /**
     * 权限申请失败回调
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRunnable != null) {
            HandlerUtil.HANDLER.removeCallbacks(mRunnable);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
