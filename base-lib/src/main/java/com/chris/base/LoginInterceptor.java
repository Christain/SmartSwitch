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
 * 创建日期：2018/7/24 16:27
 * ===============================
 */
@Interceptor(priority = 7)
public class LoginInterceptor implements IInterceptor {
    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        if (postcard.getExtra() == 1) {
            Logger.e("拦截登录");
        }
        callback.onContinue(postcard);
    }

    @Override
    public void init(Context context) {

    }

    /**
     * int转byte数组
     */
    public byte[]IntToByte(int num){
        byte[]bytes=new byte[4];
        bytes[0]=(byte) ((num>>24)&0xff);
        bytes[1]=(byte) ((num>>16)&0xff);
        bytes[2]=(byte) ((num>>8)&0xff);
        bytes[3]=(byte) (num&0xff);
        return bytes;
    }

}
