package com.chris.base.http.model;

import java.io.Serializable;

/**
 * ===============================
 * 描    述：与服务器约定的基本数据返回格式
 * 作    者：Christain
 * 创建日期：2018/7/18 下午3:10
 * ===============================
 */
public class SimpleResponse implements Serializable {

    private static final long serialVersionUID = -1477609349345966116L;

    public int code;
    public String msg;

    public BaseResponse toBaseResponse() {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.code = code;
        baseResponse.msg = msg;
        return baseResponse;
    }
}
