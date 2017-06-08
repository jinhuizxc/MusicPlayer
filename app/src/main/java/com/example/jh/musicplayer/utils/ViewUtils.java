package com.example.jh.musicplayer.utils;

import android.view.View;

import com.example.jh.musicplayer.enums.LoadStateEnum;

/**
 * Created by jinhui on 2017/6/8.
 * 邮箱: 1004260403@qq.com
 *
 * 视图工具类
 */

public class ViewUtils {

    public static void changeViewState(View loadSuccess, View loading, View loadFail, LoadStateEnum state) {
        switch (state) {
            case LOADING:
                loadSuccess.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                loadFail.setVisibility(View.GONE);
                break;
            case LOAD_SUCCESS:
                loadSuccess.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
                loadFail.setVisibility(View.GONE);
                break;
            case LOAD_FAIL:
                loadSuccess.setVisibility(View.GONE);
                loading.setVisibility(View.GONE);
                loadFail.setVisibility(View.VISIBLE);
                break;
        }
    }
}

