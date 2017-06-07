package com.example.jh.musicplayer.executor;

/**
 * Created by jinhui on 2017/6/7.
 * 邮箱: 1004260403@qq.com
 */

public interface IExecutor<T> {
    void execute();

    void onPrepare();

    void onExecuteSuccess(T t);

    void onExecuteFail(Exception e);
}
