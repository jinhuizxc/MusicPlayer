package com.example.jh.musicplayer.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.util.LongSparseArray;

import com.amap.api.location.AMapLocalWeatherLive;
import com.example.jh.musicplayer.model.Music;
import com.example.jh.musicplayer.model.SongListInfo;
import com.example.jh.musicplayer.service.PlayService;
import com.example.jh.musicplayer.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinhui on 2017/6/7.
 * 邮箱: 1004260403@qq.com
 *
 * 音乐缓存类
 */

public class AppCache {

    private Context mContext;
    private PlayService mPlayService;

    // 本地歌曲列表
    private final List<Music> mMusicList = new ArrayList<>();
    // 歌单列表
    private final List<SongListInfo> mSongListInfos = new ArrayList<>();
    private final List<Activity> mActivityStack = new ArrayList<>();
    private final LongSparseArray<String> mDownloadList = new LongSparseArray<>();
    private AMapLocalWeatherLive mAMapLocalWeatherLive;

    public AppCache() {
    }
    // 单例模式
    private static class SingletonHolder{
        private static AppCache sAppCache = new AppCache();
    }
    private static AppCache getInstance(){
        return  SingletonHolder.sAppCache;
    }

    public static void init(Application application) {
        getInstance().onInit(application);
    }

    private void onInit(Application application) {
        mContext = application.getApplicationContext();
        ToastUtils.init(mContext);
//        Preferences.init(mContext);
//        ScreenUtils.init(mContext);
//        CrashHandler.getInstance().init();
//        application.registerActivityLifecycleCallbacks(new ActivityLifecycle());
    }

    public static void setPlayService(PlayService service) {
        getInstance().mPlayService = service;
    }

    public static PlayService getPlayService() {
        return getInstance().mPlayService;
    }

    // 得到当地天气
    public static AMapLocalWeatherLive getAMapLocalWeatherLive() {
        return getInstance().mAMapLocalWeatherLive;
    }
    public static void setAMapLocalWeatherLive(AMapLocalWeatherLive aMapLocalWeatherLive) {
        getInstance().mAMapLocalWeatherLive = aMapLocalWeatherLive;
    }
}
