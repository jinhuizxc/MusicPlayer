package com.example.jh.musicplayer.http;

/**
 * Created by jinhui on 2017/6/7.
 * 邮箱: 1004260403@qq.com
 */

public abstract class HttpCallback<T> {

    public abstract void onSuccess(T t);

    public abstract void onFail(Exception e);

    public void onFinish() {
    }
}
