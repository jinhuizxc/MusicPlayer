package com.example.jh.musicplayer.executor;

import android.content.Context;
import android.content.Intent;

import com.example.jh.musicplayer.R;
import com.example.jh.musicplayer.http.HttpCallback;
import com.example.jh.musicplayer.http.HttpClient;
import com.example.jh.musicplayer.model.DownloadInfo;
import com.example.jh.musicplayer.utils.ToastUtils;

/**
 * Created by jinhui on 2017/6/8.
 * 邮箱: 1004260403@qq.com
 *
 * 分享在线歌曲
 */

public abstract class ShareOnlineMusic implements IExecutor<Void> {
    private Context mContext;
    private String mTitle;
    private String mSongId;

    public ShareOnlineMusic(Context context, String title, String songId) {
        mContext = context;
        mTitle = title;
        mSongId = songId;
    }

    @Override
    public void execute() {
        onPrepare();
        share();
    }

    private void share() {
        // 获取歌曲播放链接
        HttpClient.getMusicDownloadInfo(mSongId, new HttpCallback<DownloadInfo>() {
            @Override
            public void onSuccess(DownloadInfo response) {
                if (response == null) {
                    onFail(null);
                    return;
                }

                onExecuteSuccess(null);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, mContext.getString(R.string.share_music, mContext.getString(R.string.app_name),
                        mTitle, response.getBitrate().getFile_link()));
                mContext.startActivity(Intent.createChooser(intent, mContext.getString(R.string.share)));
            }

            @Override
            public void onFail(Exception e) {
                onExecuteFail(e);
                ToastUtils.show(R.string.unable_to_share);
            }
        });
    }
}
