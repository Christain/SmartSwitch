package com.chris.base.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.chris.base.BuildConfig;
import com.chris.base.SuperApp;
import com.chris.base.util.AppExceptionUtil;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.squareup.leakcanary.LeakCanary;


/**
 * ===============================
 * 描    述：子线程初始化Application
 * 作    者：Christain
 * 创建日期：2017/5/22 19:59
 * ===============================
 */
public class AppInitializeService extends IntentService {

    private static final String ACTION_INIT = "com.chris.base.service.initApplication";

    public AppInitializeService() {
        super("AppInitializeService");
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, AppInitializeService.class);
        intent.setAction(ACTION_INIT);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INIT.equals(action)) {
                initApplication();
            }
        }
    }

    private void initApplication() {
        //错误拦截
        AppExceptionUtil.start();

        //内存泄漏检查
        if (!LeakCanary.isInAnalyzerProcess(SuperApp.getContext())) {
            LeakCanary.install(SuperApp.getInstance());
        }

        //初始化日志
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .logStrategy(new LogCatStrategy())
                .methodCount(1)         // (Optional) How many method line to show. Default 2
                .methodOffset(0)        // (Optional) Skips some method invokes in stack trace. Default 5
                .tag("BaseLogger")      // (Optional) Custom tag for each log. Default PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return BuildConfig.DEBUG;
            }
        });
    }

    /**
     * Logger自定义Tag
     */
    private class LogCatStrategy implements LogStrategy {

        @Override
        public void log(int priority, String tag, String message) {
            Log.println(priority, randomKey() + tag, message);
        }

        private int last;

        private String randomKey() {
            int random = (int) (10 * Math.random());
            if (random == last) {
                random = (random + 1) % 10;
            }
            last = random;
            return String.valueOf(random);
        }
    }

}
