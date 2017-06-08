package com.example.jh.musicplayer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.jh.musicplayer.constants.Actions;
import com.example.jh.musicplayer.service.PlayService;

/**
 * Created by jinhui on 2017/6/7.
 * 邮箱: 1004260403@qq.com
 *
 * 来电/耳机拔出时暂停播放
 */

public class NoisyAudioStreamReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PlayService.startCommand(context, Actions.ACTION_MEDIA_PLAY_PAUSE);
    }
}
