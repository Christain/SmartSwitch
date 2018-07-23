package com.chris.base.widget.mvchelper.task;


import com.chris.base.widget.mvchelper.mvc.RequestHandle;

/**
 * 执行类的接口
 */
public interface TaskExecutor<DATA> extends RequestHandle {

    RequestHandle execute();

    ICallback<DATA> getCallback();

    Object getTask();

    boolean isExeRefresh();
}