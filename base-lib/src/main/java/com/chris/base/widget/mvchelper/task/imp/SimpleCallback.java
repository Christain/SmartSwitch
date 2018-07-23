package com.chris.base.widget.mvchelper.task.imp;


import com.chris.base.widget.mvchelper.task.ICallback;

/**
 * Created by LuckyJayce on 2016/7/17.
 * ICallback的空实现.
 *
 */
public abstract class SimpleCallback<DATA> implements ICallback<DATA> {

    @Override
    public void onPreExecute(Object task) {

    }

    @Override
    public void onProgress(Object task, int percent, long current, long total, Object extraData) {

    }
}
