package com.example.jh.musicplayer.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jinhui on 2017/6/7.
 * 邮箱: 1004260403@qq.com
 *
 * 歌词
 */

public class Lrc {
    @SerializedName("lrcContent")
    private String lrcContent;

    public String getLrcContent() {
        return lrcContent;
    }

    public void setLrcContent(String lrcContent) {
        this.lrcContent = lrcContent;
    }
}
