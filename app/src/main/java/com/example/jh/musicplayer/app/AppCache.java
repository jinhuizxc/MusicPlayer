package com.example.jh.musicplayer.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.util.LongSparseArray;
import android.util.DisplayMetrics;
import android.util.Log;

import com.amap.api.location.AMapLocalWeatherLive;
import com.example.jh.musicplayer.model.Music;
import com.example.jh.musicplayer.model.SongListInfo;
import com.example.jh.musicplayer.service.PlayService;
import com.example.jh.musicplayer.utils.Preferences;
import com.example.jh.musicplayer.utils.ScreenUtils;
import com.example.jh.musicplayer.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinhui on 2017/6/7.
 * 邮箱: 1004260403@qq.com
 *
 * App——缓存类
 */

public class AppCache {

    private static final String TAG = "AppCache";
    private Context mContext;
    // 音乐服务
    private PlayService mPlayService;
    // 本地歌曲列表
    private final List<Music> mMusicList = new ArrayList<>();
    // 歌单列表
    private final List<SongListInfo> mSongListInfos = new ArrayList<>();
    // Activity链表
    private final List<Activity> mActivityStack = new ArrayList<>();
    // 下载链表
    private final LongSparseArray<String> mDownloadList = new LongSparseArray<>();
    // 高德地图
    private AMapLocalWeatherLive mAMapLocalWeatherLive;

    public AppCache() {
    }
    // 单例模式
    private static AppCache getInstance(){
        return  SingletonHolder.sAppCache;
    }
    private static class SingletonHolder{
        private static AppCache sAppCache = new AppCache();
    }

    // 初始化
    public static void init(Application application) {
        getInstance().onInit(application);
    }

    private void onInit(Application application) {
        mContext = application.getApplicationContext();
        ToastUtils.init(mContext);
        Preferences.init(mContext);
        ScreenUtils.init(mContext);
        CrashHandler.getInstance().init();
        // app注册生命周期的回调方法
        application.registerActivityLifecycleCallbacks(new ActivityLifecycle());
    }

    public static Context getContext() {
        return getInstance().mContext;
    }

    public static PlayService getPlayService() {
        return getInstance().mPlayService;   // 返回null
    }

    public static void setPlayService(PlayService service) {
        getInstance().mPlayService = service;
    }

    // 更改主题
    public static void updateNightMode(boolean on) {
        Resources resources = getContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        config.uiMode &= ~Configuration.UI_MODE_NIGHT_MASK;
        config.uiMode |= on ? Configuration.UI_MODE_NIGHT_YES : Configuration.UI_MODE_NIGHT_NO;
        resources.updateConfiguration(config, dm);
    }

    public static List<Music> getMusicList() {
        return getInstance().mMusicList;
    }

    public static List<SongListInfo> getSongListInfos() {
        return getInstance().mSongListInfos;
    }

    // 清理Activity栈
    public static void clearStack() {
        List<Activity> activityStack = getInstance().mActivityStack;
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            Activity activity = activityStack.get(i);
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        activityStack.clear();
    }

    public static LongSparseArray<String> getDownloadList() {
        return getInstance().mDownloadList;
    }

    // 得到当地天气
    public static AMapLocalWeatherLive getAMapLocalWeatherLive() {
        return getInstance().mAMapLocalWeatherLive;
    }

    public static void setAMapLocalWeatherLive(AMapLocalWeatherLive aMapLocalWeatherLive) {
        getInstance().mAMapLocalWeatherLive = aMapLocalWeatherLive;
    }

    // ActivityLifecycle
    private static class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {

        private static final String TAG = "Activity";

        // 实现其回调的7个方法
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Log.i(TAG, "onCreate: " + activity.getClass().getSimpleName());
            getInstance().mActivityStack.add(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Log.i(TAG, "onDestroy: " + activity.getClass().getSimpleName());
            getInstance().mActivityStack.remove(activity);
        }

    }

}
