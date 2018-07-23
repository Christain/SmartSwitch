package com.chris.base.http.model;

import java.io.Serializable;

/**
 * ===============================
 * 描    述：与服务器约定的基本数据返回格式
 * 作    者：Christain
 * 创建日期：2018/7/18 下午3:10
 * ===============================
 */
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = 5213230387175987834L;

    public int code;
    public String msg;
    public T data;

    @Override
    public String toString() {
        return "BaseResponse{\n" +//
               "\tcode=" + code + "\n" +//
               "\tmsg='" + msg + "\'\n" +//
               "\tdata=" + data + "\n" +//
               '}';
    }
}
