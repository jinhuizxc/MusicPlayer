package com.example.jh.musicplayer.app;

import android.app.Application;

import com.example.jh.musicplayer.BuildConfig;
import com.example.jh.musicplayer.http.HttpInterceptor;
import com.example.jh.musicplayer.key.KeyStore;
import com.example.jh.musicplayer.utils.Preferences;
import com.tencent.bugly.Bugly;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by jinhui on 2017/6/7.
 * 邮箱: 1004260403@qq.com
 *
 * 自定义Application
 */

public class MusicApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppCache.init(this);
        // 更新夜间主题
        AppCache.updateNightMode(Preferences.isNightMode());
        // 初始化
        initOkHttpUtils();
        initBugly();
    }

    private void initOkHttpUtils() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new HttpInterceptor())
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    private void initBugly() {
        if (!BuildConfig.DEBUG) {
            Bugly.init(this, KeyStore.getKey(KeyStore.BUGLY_APP_ID), false);
        }
    }
}

