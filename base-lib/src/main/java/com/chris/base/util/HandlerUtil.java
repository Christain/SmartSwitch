package com.chris.base.util;

import android.os.Handler;
import android.os.Looper;

/**
 * ==================================
 * 描    述：Handler封装类
 * 作    者：Christain
 * 创建日期：2017/5/8 15:35
 * ==================================
 */
public class HandlerUtil {
    public static final Handler HANDLER = new Handler(Looper.getMainLooper());

    public static void runOnUiThread(Runnable runnable){
        HANDLER.post(runnable);
    }

    public static void runOnUiThreadDelay(Runnable runnable, long delayMillis){
        HANDLER.postDelayed(runnable,delayMillis);
    }

    public static void removeRunable(Runnable runnable){
        HANDLER.removeCallbacks(runnable);
    }
}
