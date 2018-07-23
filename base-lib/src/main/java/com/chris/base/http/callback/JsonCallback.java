package com.chris.base.http.callback;

import com.chris.base.BuildConfig;
import com.chris.base.http.log.LogUtils;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.exception.HttpException;
import com.lzy.okgo.exception.StorageException;
import com.lzy.okgo.request.base.Request;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.Response;

/**
 * ===============================
 * 描    述：默认将返回的数据解析成需要的Bean,可以是 BaseBean，String，List，Map
 * 作    者：Christain
 * 创建日期：2018/7/18 下午3:10
 * ===============================
 */
public abstract class JsonCallback<T> extends AbsCallback<T> {

    private Type type;
    private Class<T> clazz;
    private LogUtils<T> mLogUtils;

    public JsonCallback() {
        initLogUtil();
    }

    public JsonCallback(Type type) {
        this.type = type;
        initLogUtil();
    }

    public JsonCallback(Class<T> clazz) {
        this.clazz = clazz;
        initLogUtil();
    }

    private void initLogUtil() {
        if (BuildConfig.DEBUG) {
            mLogUtils = new LogUtils<T>();
        }
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        super.onStart(request);
        // 主要用于在所有请求之前添加公共的请求头或请求参数
        // 可以随意添加,也可以什么都不传
        // 还可以在这里对所有的参数进行加密，均在这里实现
    }

    /**
     * 该方法是子线程处理，不能做ui相关的工作
     */
    @Override
    public T convertResponse(Response response) throws Throwable {
        if (type == null) {
            if (clazz == null) {
                Type genType = getClass().getGenericSuperclass();
                type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            } else {
                JsonConvert<T> convert = new JsonConvert<>(clazz);
                return convert.convertResponse(response);
            }
        }

        JsonConvert<T> convert = new JsonConvert<>(type);
        return convert.convertResponse(response);
    }

    @Override
    public void onError(com.lzy.okgo.model.Response<T> response) {
        Throwable exception = response.getException();
        if (exception == null) {
            onFailed(response, "");
            return;
        }
        String message = null;
        if (exception instanceof UnknownHostException || exception instanceof ConnectException) {
            message = "网络连接失败，请连接网络";
        } else if (exception instanceof SocketTimeoutException) {
            message = "网络请求超时";
        } else if (exception instanceof HttpException) {
            message = "服务器响应失败";
        } else if (exception instanceof StorageException) {
            message = "sd卡不存在或者没有权限";
        } else if (exception.getMessage().contains("JSON") || exception.getMessage().contains("Json")) {
            message = "数据解析错误";
        } else if (exception.getMessage().contains("IOException") || exception.getMessage().contains("Socket closed")) {
            message = "";
        } else if (exception.getMessage().contains("SSL")
                || exception.getMessage().contains("ProtocolException")
                || exception.getMessage().contains("ERR_INTERNET")) {
            message = "网络连接失败，请重试";
        } else {
            message = exception.getMessage();
        }
        onFailed(response, message != null ? message : "");

        //=============打印错误日志==============//
        if (mLogUtils != null) {
            mLogUtils.printErrorLog(response.getRawCall().request(), message);
            mLogUtils = null;
        }
    }

    @Override
    public void onSuccess(com.lzy.okgo.model.Response<T> response) {
        onSuccessed(response);

        //=============打印日志==============//
        if (mLogUtils != null) {
            mLogUtils.printSuccessLog(response.getRawCall().request(), response.body());
            mLogUtils = null;
        }
    }

    /**
     * 抽象成功回调
     */
    public abstract void onSuccessed(com.lzy.okgo.model.Response<T> response);

    /**
     * 抽象失败回调
     */
    public abstract void onFailed(com.lzy.okgo.model.Response<T> response, String message);

}
