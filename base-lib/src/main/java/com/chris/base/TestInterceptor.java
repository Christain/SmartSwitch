package com.chris.base;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.orhanobut.logger.Logger;

/**
 * ===============================
 * 描    述：
 * 作    者：Christain
 * 创建日期：2018/7/24 16:38
 * ===============================
 */
@Interceptor(priority = 8)
public class TestInterceptor implements IInterceptor {
    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        Logger.e("测试用拦截器");
        callback.onContinue(postcard);
    }

    @Override
    public void init(Context context) {

    }
}
