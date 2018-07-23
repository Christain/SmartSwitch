package com.chris.base.glide;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.chris.base.Constants;
import com.chris.base.util.PhoneUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * ===============================
 * 描    述：glide配置
 * 作    者：Christain
 * 创建日期：2018/7/18 下午8:39
 * ===============================
 */
@GlideModule
public class BaseAppGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        super.applyOptions(context, builder);
        boolean permission = (PackageManager.PERMISSION_GRANTED == context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE"));
        if (!permission) {
            InternalCacheDiskCache(context, builder);
        } else {
            //如果不存在SD卡，就缓存在app目录下
            if (!PhoneUtil.isSdCardExit()) {
                InternalCacheDiskCache(context, builder);
                return;
            }
            File cacheLocation = new File(Constants.PATH_IMAGE_CACHE_DIR);
            if (!cacheLocation.exists()) {
                cacheLocation.mkdirs();
            }
            //100M
            builder.setDiskCache(new DiskLruCacheFactory(cacheLocation.getPath(), 100 * 1024 * 1024));
            //手机内存大于2G,使用8888高清格式缓存
            builder.setDefaultRequestOptions(new RequestOptions().format(
                    getTotalMemory() >= 2 * 1024 * 1024 * 1024
                            ? DecodeFormat.PREFER_ARGB_8888
                            : DecodeFormat.PREFER_RGB_565));
            MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                    .setBitmapPoolScreens(3)
                    .build();
            long customMemoryCacheSize = (long) (1.2 * calculator.getMemoryCacheSize());
            long customBitmapPoolSize = (long) (1.2 * calculator.getBitmapPoolSize());
            builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
            builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));
        }
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
    }

    /**
     * 缓存图片到app目录下(无法在SD卡查看)
     *
     * @param context
     * @param builder
     */
    private void InternalCacheDiskCache(@NonNull Context context, @NonNull GlideBuilder builder) {
        //100M
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, 100 * 1024 * 1024));
        //手机内存大于2G,使用8888高清格式缓存
        builder.setDefaultRequestOptions(new RequestOptions().format(
                getTotalMemory() >= 2 * 1024 * 1024 * 1024
                        ? DecodeFormat.PREFER_ARGB_8888
                        : DecodeFormat.PREFER_RGB_565));
    }

    /**
     * 读取手机系统内存大小
     */
    private long getTotalMemory() {
        //系统内存信息文件
        String str1 = "/proc/meminfo";
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();

            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }

            //获得系统总内存，单位是KB，乘以1024转换为Byte
            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;
            localBufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return initial_memory;
    }
}
