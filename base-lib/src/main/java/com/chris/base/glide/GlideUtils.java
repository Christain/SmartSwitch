package com.chris.base.glide;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestOptions;

/**
 * ===============================
 * 描    述：glide图片加载工具类
 * 作    者：Christain
 * 创建日期：2018/7/18 下午8:29
 * ===============================
 */
public class GlideUtils {

    public static void load(Context context, String url, ImageView imageView, RequestOptions options) {
        GlideApp.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }
}
