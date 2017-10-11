package com.example.jh.musicplayer.ui.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jh.musicplayer.R;
import com.example.jh.musicplayer.app.AppCache;
import com.example.jh.musicplayer.base.BaseActivity;
import com.example.jh.musicplayer.http.HttpCallback;
import com.example.jh.musicplayer.http.HttpClient;
import com.example.jh.musicplayer.model.Splash;
import com.example.jh.musicplayer.service.PlayService;
import com.example.jh.musicplayer.utils.FileUtils;
import com.example.jh.musicplayer.utils.Preferences;
import com.example.jh.musicplayer.utils.ToastUtils;
import com.example.jh.musicplayer.utils.binding.Bind;
import com.example.jh.musicplayer.utils.permission.PermissionReq;
import com.example.jh.musicplayer.utils.permission.PermissionResult;

import java.io.File;
import java.util.Calendar;

/**
 * Created by jinhui on 2017/6/7.
 * 邮箱: 1004260403@qq.com
 */

public class SplashActivity extends BaseActivity {

    private static final String SPLASH_FILE_NAME = "splash";
    private static final String TAG = "SplashActivity";

    @Bind(R.id.iv_splash)
    private ImageView ivSplash;
    @Bind(R.id.tv_copyright)
    private TextView tvCopyright;
    // 服务连接对象
    private ServiceConnection mPlayServiceConnection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int year = Calendar.getInstance().get(Calendar.YEAR);
        tvCopyright.setText(getString(R.string.copyright, year));//  Copyright © 2015&#045;%d SWUST
        // 开始音乐服务
        checkService();
    }

    private void checkService() {
        if (AppCache.getPlayService() == null) {
            startService();
            // 显示开机广告页
            showSplash();
            updateSplash();

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bindService();
                }
            }, 1000);
        } else {
            startMusicActivity();
            finish();
        }
    }

    // 开始音乐服务
    private void startService() {
        Intent intent = new Intent(this, PlayService.class);
        startService(intent);   // intent = Intent { cmp=com.example.jh.musicplayer/.service.PlayService }
    }

    // 绑定服务
    private void bindService() {
        Intent intent = new Intent();
        intent.setClass(this, PlayService.class);
        mPlayServiceConnection = new PlayServiceConnection();
        bindService(intent, mPlayServiceConnection, Context.BIND_AUTO_CREATE);
    }

    // 服务连接对象
    public class PlayServiceConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            final PlayService playService = ((PlayService.PlayBinder)service).getService();
            // 设置音乐服务
            AppCache.setPlayService(playService);
            // 权限
            PermissionReq.with(SplashActivity.this)
                    .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .result(new PermissionResult() {
                        @Override
                        public void onGranted() {
                            // 扫描音乐
                            scanMusic(playService);
                        }

                        @Override
                        public void onDenied() {
                            ToastUtils.show(getString(R.string.no_permission, "存储空间", "扫描本地歌曲"));
                            finish();
                            playService.stop();
                        }
                    })
                    .request();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    private void showSplash() {
        File splashImg = new File(FileUtils.getSplashDir(this), SPLASH_FILE_NAME);
        if (splashImg.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(splashImg.getPath()); //  splashImg.getPath() = /data/user/0/com.example.jh.musicplayer/files/splash/splash
            ivSplash.setImageBitmap(bitmap);
        }
    }

    private void updateSplash() {
        HttpClient.getSplash(new HttpCallback<Splash>() {
            @Override
            public void onSuccess(Splash response) {
                if (response == null || TextUtils.isEmpty(response.getUrl())) {
                    return;
                }
                final String url = response.getUrl();
                Log.e(TAG, "url 000000000 = " + url);
                // 如果最后一张url与保存的相同则返回。
                String lastImgUrl = Preferences.getSplashUrl();
                if (TextUtils.equals(lastImgUrl, url)) {
                    return;
                }
                HttpClient.downloadFile(url, FileUtils.getSplashDir(AppCache.getContext()), SPLASH_FILE_NAME,
                        new HttpCallback<File>() {
                            @Override
                            public void onSuccess(File file) {
                                Preferences.saveSplashUrl(url);
                                Log.e(TAG, "url 000000000 = " + url);
                            }

                            @Override
                            public void onFail(Exception e) {
                            }
                        });
            }

            @Override
            public void onFail(Exception e) {
            }
        });
    }




    // 扫描音乐
    public void scanMusic(final PlayService playService){
        // 执行异步任务，这步属于耗时操作
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                playService.updateMusicList();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                startMusicActivity();
                finish();
            }
        }.execute();
    }

    private void startMusicActivity() {
        Intent intent = new Intent();
        intent.setClass(this, MusicActivity.class);
        intent.putExtras(getIntent());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayServiceConnection != null) {
            unbindService(mPlayServiceConnection);
        }
    }
}
