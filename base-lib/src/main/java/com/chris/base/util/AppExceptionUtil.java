package com.chris.base.util;

import android.content.pm.PackageInfo;
import android.os.Environment;

import com.chris.base.BuildConfig;
import com.chris.base.SuperApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;

/**
 * 系统错误处理
 */
public class AppExceptionUtil implements UncaughtExceptionHandler {

    private static AppExceptionUtil INSTANCE;

    public static void start() {
        if (INSTANCE == null) {
            INSTANCE = new AppExceptionUtil();
        }
        Thread.setDefaultUncaughtExceptionHandler(INSTANCE);
    }

    private UncaughtExceptionHandler mDefaultHandler;

    private AppExceptionUtil(){
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ActivityStackUtil.getInstance().exit(SuperApp.getContext());
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @return true代表处理该异常，不再向上抛异常，
     * false代表不处理该异常(可以将该log信息存储起来)然后交给上层(这里就到了系统的异常处理)去处理，
     * 简单来说就是true不会弹出那个错误提示框，false就会弹出
     */

    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }
        new Thread() {
            @Override
            public void run() {
                ex.printStackTrace();
                //非调试模式时，错误日志写入SD卡
                if (!BuildConfig.DEBUG) {
                    saveExceptionLog(ex);
                }
            }
        }.start();
        return true;
    }

    /**
     * 错误日志写入SD卡
     * @param ex
     */
    private void saveExceptionLog(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String logInfo = writer.toString();
        if (PhoneUtil.isSdCardExit()) {
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/zima/log");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            StringBuilder sb = new StringBuilder();
            long date = System.currentTimeMillis();
            sb.append("crash_");
            PackageInfo info = PhoneUtil.getVersionInfo(SuperApp.getContext());
            if (info != null) {
                sb.append("V");
                sb.append(info.versionName);
                sb.append("_");
            }
            sb.append(getFormatedTime("MM-dd HH:mm", date));
            sb.append(".txt");
            File logFile = new File(dir, sb.toString());
            try {
                FileOutputStream fos = new FileOutputStream(logFile);
                fos.write(logInfo.getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取格式化的时间
     *
     * @param format    yyyy-MM-dd HH:mm; MM-dd HH:mm;HH:mm;yyyy-MM-dd;yyyy年M月d日
     *                  HH:mm;M月d日 HH:mm;H:mm;...
     * @param timeMills
     */
    public String getFormatedTime(String format, long timeMills) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(timeMills);
    }
}
