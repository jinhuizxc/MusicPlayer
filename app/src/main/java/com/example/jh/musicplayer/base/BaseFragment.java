package com.example.jh.musicplayer.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.example.jh.musicplayer.app.AppCache;
import com.example.jh.musicplayer.service.PlayService;
import com.example.jh.musicplayer.utils.binding.ViewBinder;
import com.example.jh.musicplayer.utils.permission.PermissionReq;

/**
 * Created by jinhui on 2017/6/8.
 * 邮箱: 1004260403@qq.com
 *
 * 基类<br>
 */

public abstract class BaseFragment extends Fragment {

    protected Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ViewBinder.bind(this, view);
        init();
        setListener();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionReq.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected abstract void init();

    protected abstract void setListener();

    protected PlayService getPlayService() {
        PlayService playService = AppCache.getPlayService();
        if (playService == null) {
            throw new NullPointerException("play service is null");
        }
        return playService;
    }
}

