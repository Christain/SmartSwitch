package com.chris.base.widget.mvchelper.mvc;

/**
 * 用于外部取消请求的处理。
 */
public interface RequestHandle {

    void cancle();

    boolean isRunning();

}