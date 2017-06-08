package com.example.jh.musicplayer.service;

import com.example.jh.musicplayer.model.Music;

/**
 * Created by jinhui on 2017/6/7.
 * 邮箱: 1004260403@qq.com
 *
 * 播放进度监听器
 */

public interface OnPlayerEventListener {
    /**
     * 更新进度
     */
    void onPublish(int progress);

    /**
     * 缓冲百分比
     */
    void onBufferingUpdate(int percent);

    /**
     * 切换歌曲
     */
    void onChange(Music music);

    /**
     * 暂停播放
     */
    void onPlayerPause();

    /**
     * 继续播放
     */
    void onPlayerResume();

    /**
     * 更新定时停止播放时间
     */
    void onTimer(long remain);

    void onMusicListUpdate();
}

