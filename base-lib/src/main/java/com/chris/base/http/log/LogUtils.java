package com.chris.base.http.log;

import android.graphics.Bitmap;

import com.chris.base.http.convert.Convert;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.Request;
import okio.Buffer;

/**
 * ===============================
 * 描    述：日志打印处理类
 * 作    者：Christain
 * 创建日期：2018/7/18 下午4:24
 * ===============================
 */
public class LogUtils<T> {

    /**
     * 打印成功logger
     *
     * @param request
     */
    public void printSuccessLog(Request request, T body) {
        if (request == null) return;
        Logger.json("{\"Url\":\"" + request.url().toString() + "\","
                + "\"Method\":\"" + request.method() + "\","
                + "\"Params\":\"" + paramsString(request) + "\","
                + "\"Headers\":\"" + request.headers().toString() + "\","
                + "\"Response\":" + getResponseData(body) + "}");
    }

    /**
     * 打印错误日志
     *
     * @param request
     * @param message
     */
    public void printErrorLog(Request request, String message) {
        if (request == null) return;
        Logger.e("请求地址：" + request.url().toString()
                + "\n\n请求方式：" + request.method()
                + "\n\n参数：" + paramsString(request)
                + "\n\n错误信息：" + message);
    }

    /**
     * 转换正确响应的数据为String
     */
    private String getResponseData(T body) {
        StringBuilder sb;
        String responseData = null;
        if (body == null) {
            return "--";
        }
        if (body instanceof String) {
            responseData = (String) body;
        } else if (body instanceof List) {
            sb = new StringBuilder();
            List list = (List) body;
            for (Object obj : list) {
                sb.append(obj.toString()).append("\n");
            }
            responseData = sb.toString();
        } else if (body instanceof Set) {
            sb = new StringBuilder();
            Set set = (Set) body;
            for (Object obj : set) {
                sb.append(obj.toString()).append("\n");
            }
            responseData = sb.toString();
        } else if (body instanceof Map) {
            sb = new StringBuilder();
            Map map = (Map) body;
            Set keySet = map.keySet();
            for (Object key : keySet) {
                sb.append(key.toString()).append(" ： ").append(map.get(key)).append("\n");
            }
            responseData = sb.toString();
        } else if (body instanceof File) {
            File file = (File) body;
            responseData = "数据内容即为文件内容\n下载文件路径：" + file.getAbsolutePath();
        } else if (body instanceof Bitmap) {
            responseData = "图片的内容即为数据";
        } else {
            try {
                responseData = Convert.formatJson(body);
            } catch (Exception e) {
                e.printStackTrace();
                responseData = "";
            }
        }
        return responseData == null ? "" : responseData;
    }

    /**
     * 接口请求的参数序列化为String
     *
     * @param request
     */
    private String paramsString(Request request) {
        if (!request.method().equals("GET")) {
            if (isText(request.body().contentType())) {
                try {
                    Request copy = request.newBuilder().build();
                    Buffer buffer = new Buffer();
                    copy.body().writeTo(buffer);
                    return URLDecoder.decode(buffer.readUtf8(), "UTF-8");
                } catch (IOException e) {
                    return "something error when show requestBody.";
                }
            } else {
                return "content : " + " maybe [file part] , too large too print , ignored!";
            }
        } else {
            return request.url() + "";
        }
    }

    /**
     * 判断是否是Text类型
     *
     * @param mediaType
     */
    private boolean isText(MediaType mediaType) {
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        if (mediaType.subtype() != null) {
            if (mediaType.subtype().equals("x-www-form-urlencoded") ||
                    mediaType.subtype().equals("json") ||
                    mediaType.subtype().equals("xml") ||
                    mediaType.subtype().equals("html") ||
                    mediaType.subtype().equals("webviewhtml"))
                return true;
        }
        return false;
    }
}
