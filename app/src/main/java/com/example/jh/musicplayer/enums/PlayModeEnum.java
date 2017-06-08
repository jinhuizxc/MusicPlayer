package com.example.jh.musicplayer.enums;

/**
 * Created by jinhui on 2017/6/7.
 * 邮箱: 1004260403@qq.com
 *
 * 播放模式
 */

public enum PlayModeEnum {

    LOOP(0),
    SHUFFLE(1),
    SINGLE(2);

    private int value;

    PlayModeEnum(int value) {
        this.value = value;
    }

    public static PlayModeEnum valueOf(int value) {
        switch (value) {
            case 1:
                return SHUFFLE;
            case 2:
                return SINGLE;
            case 0:
            default:
                return LOOP;
        }
    }

    public int value() {
        return value;
    }
}

