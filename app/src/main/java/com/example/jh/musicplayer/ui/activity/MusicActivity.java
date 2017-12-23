package com.example.jh.musicplayer.ui.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jh.musicplayer.R;
import com.example.jh.musicplayer.app.AppCache;
import com.example.jh.musicplayer.base.BaseActivity;
import com.example.jh.musicplayer.constants.Extras;
import com.example.jh.musicplayer.executor.NaviMenuExecutor;
import com.example.jh.musicplayer.executor.WeatherExecutor;
import com.example.jh.musicplayer.model.Music;
import com.example.jh.musicplayer.receiver.RemoteControlReceiver;
import com.example.jh.musicplayer.service.OnPlayerEventListener;
import com.example.jh.musicplayer.service.PlayService;
import com.example.jh.musicplayer.ui.adapter.FragmentAdapter;
import com.example.jh.musicplayer.ui.fragment.LocalMusicFragment;
import com.example.jh.musicplayer.ui.fragment.PlayFragment;
import com.example.jh.musicplayer.ui.fragment.SongListFragment;
import com.example.jh.musicplayer.utils.CoverLoader;
import com.example.jh.musicplayer.utils.SystemUtils;
import com.example.jh.musicplayer.utils.ToastUtils;
import com.example.jh.musicplayer.utils.binding.Bind;
import com.example.jh.musicplayer.utils.permission.PermissionReq;
import com.example.jh.musicplayer.utils.permission.PermissionResult;

/**
 * 打造一款音乐播放器，借鉴ponyMusic, 以及学习多渠道打包发布apk。
 * test
 */
public class MusicActivity extends BaseActivity implements View.OnClickListener, OnPlayerEventListener,
        NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    @Bind(R.id.drawer_layout)
    private DrawerLayout drawerLayout;
    @Bind(R.id.navigation_view)
    private NavigationView navigationView;
    @Bind(R.id.iv_menu)
    private ImageView ivMenu;
    @Bind(R.id.iv_search)
    private ImageView ivSearch;
    @Bind(R.id.tv_local_music)
    private TextView tvLocalMusic;
    @Bind(R.id.tv_online_music)
    private TextView tvOnlineMusic;
    @Bind(R.id.viewpager)
    private ViewPager mViewPager;
    @Bind(R.id.fl_play_bar)
    private FrameLayout flPlayBar;
    @Bind(R.id.iv_play_bar_cover)
    private ImageView ivPlayBarCover;
    @Bind(R.id.tv_play_bar_title)
    private TextView tvPlayBarTitle;
    @Bind(R.id.tv_play_bar_artist)
    private TextView tvPlayBarArtist;
    @Bind(R.id.iv_play_bar_play)
    private ImageView ivPlayBarPlay;
    @Bind(R.id.iv_play_bar_next)
    private ImageView ivPlayBarNext;
    @Bind(R.id.pb_play_bar)
    private ProgressBar mProgressBar;
    // 天气view
    private View vNavigationHeader;
    private LocalMusicFragment mLocalMusicFragment;
    // 在线音乐列表
    private SongListFragment mSongListFragment;
    // 改PlayFragment 为activity
//    private PlayActivity mPlayActivity;
    private PlayFragment mPlayFragment;
    private AudioManager mAudioManager;
    private ComponentName mRemoteReceiver;
    private boolean isPlayFragmentShow = false;
    private MenuItem timerItem;
    private long time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        if (!checkServiceAlive()) {
            return;
        }
        getPlayService().setOnPlayEventListener(this);

        setupView();
        // 更新天气
        updateWeather();
        registerReceiver();
        onChange(getPlayService().getPlayingMusic());
        // 显示playFragment
        parseIntent();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        parseIntent();
    }

    @Override
    protected void setListener() {
        ivMenu.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        tvLocalMusic.setOnClickListener(this);
        tvOnlineMusic.setOnClickListener(this);
        mViewPager.setOnPageChangeListener(this);
        flPlayBar.setOnClickListener(this);
        ivPlayBarPlay.setOnClickListener(this);
        ivPlayBarNext.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupView() {
        // add navigation header
        vNavigationHeader = LayoutInflater.from(this).inflate(R.layout.navigation_header, navigationView, false);
        navigationView.addHeaderView(vNavigationHeader);

        // setup view pager
        mLocalMusicFragment = new LocalMusicFragment();
        mSongListFragment = new SongListFragment();
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(mLocalMusicFragment);
        adapter.addFragment(mSongListFragment);
        mViewPager.setAdapter(adapter);
        tvLocalMusic.setSelected(true);
    }

    private void updateWeather() {
        PermissionReq.with(this)
                .permissions(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .result(new PermissionResult() {
                    @Override
                    public void onGranted() {
                        new WeatherExecutor(getPlayService(), vNavigationHeader).execute();
                    }

                    @Override
                    public void onDenied() {
                        ToastUtils.show(getString(R.string.no_permission, "位置信息", "更新天气"));
                    }
                })
                .request();
    }

    private void registerReceiver() {
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mRemoteReceiver = new ComponentName(getPackageName(), RemoteControlReceiver.class.getName());
        mAudioManager.registerMediaButtonEventReceiver(mRemoteReceiver);
    }

    private void parseIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(Extras.EXTRA_NOTIFICATION)) {

            showPlayingFragment();
            setIntent(new Intent());
        }
    }

    /**
     * 更新播放进度
     */
    @Override
    public void onPublish(int progress) {
        mProgressBar.setProgress(progress);
        if (mPlayFragment != null) {
            mPlayFragment.onPublish(progress);
        }
    }

    @Override
    public void onBufferingUpdate(int percent) {
        if (mPlayFragment != null) {
            mPlayFragment.onBufferingUpdate(percent);
        }
    }

    @Override
    public void onChange(Music music) {
        onPlay(music);
        if (mPlayFragment != null) {
            mPlayFragment.onChange(music);
        }
    }

    @Override
    public void onPlayerPause() {
        ivPlayBarPlay.setSelected(false);
        if (mPlayFragment != null) {
            mPlayFragment.onPlayerPause();
        }
    }

    @Override
    public void onPlayerResume() {
        ivPlayBarPlay.setSelected(true);
        if (mPlayFragment != null) {
            mPlayFragment.onPlayerResume();
        }
    }

    // 定时播放
    @Override
    public void onTimer(long remain) {
        if (timerItem == null) {
            timerItem = navigationView.getMenu().findItem(R.id.action_timer);
        }
        String title = getString(R.string.menu_timer);
        timerItem.setTitle(remain == 0 ? title : SystemUtils.formatTime(title + "(mm:ss)", remain));
    }

    @Override
    public void onMusicListUpdate() {
        if (mLocalMusicFragment != null) {
            mLocalMusicFragment.onMusicListUpdate();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_menu:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.iv_search:
                // 进入搜索界面
                startActivity(new Intent(this, SearchMusicActivity.class));
                break;
            case R.id.tv_local_music:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.tv_online_music:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.fl_play_bar:
                // 显示播放fragment
                // 进入PlayActivity
//                startActivity(new Intent(MusicActivity.this, PlayActivity.class));
                showPlayingFragment();
                break;
            case R.id.iv_play_bar_play:
                play();
                break;
            case R.id.iv_play_bar_next:
                next();
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        drawerLayout.closeDrawers();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                item.setChecked(false);
            }
        }, 500);
        return NaviMenuExecutor.onNavigationItemSelected(item, this);
    }



    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            tvLocalMusic.setSelected(true);
            tvOnlineMusic.setSelected(false);
        } else {
            tvLocalMusic.setSelected(false);
            tvOnlineMusic.setSelected(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    // onPlay
    public void onPlay(Music music) {
        if (music == null) {
            return;
        }

        Bitmap cover = CoverLoader.getInstance().loadThumbnail(music);
        ivPlayBarCover.setImageBitmap(cover);
        tvPlayBarTitle.setText(music.getTitle());
        tvPlayBarArtist.setText(music.getArtist());
        if (getPlayService().isPlaying() || getPlayService().isPreparing()) {
            ivPlayBarPlay.setSelected(true);
        } else {
            ivPlayBarPlay.setSelected(false);
        }
        mProgressBar.setMax((int) music.getDuration());
        mProgressBar.setProgress(0);

        if (mLocalMusicFragment != null) {
            mLocalMusicFragment.onItemPlay();
        }
    }

    private void play() {
        getPlayService().playPause();
    }

    private void next() {
        getPlayService().next();
    }

    private void showPlayingFragment() {
        if (isPlayFragmentShow) {
            return;
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_up, 0);
        if (mPlayFragment == null) {
            mPlayFragment = new PlayFragment();
            ft.replace(android.R.id.content, mPlayFragment);
        } else {
            ft.show(mPlayFragment);
        }
        ft.commitAllowingStateLoss();
        isPlayFragmentShow = true;
    }

    private void hidePlayingFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(0, R.anim.fragment_slide_down);
        ft.hide(mPlayFragment);
        ft.commitAllowingStateLoss();
        isPlayFragmentShow = false;
    }

    @Override
    public void onBackPressed() {
        if (mPlayFragment != null && isPlayFragmentShow) {
            hidePlayingFragment();
            return;
        }
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // 切换夜间模式不保存状态
    }

//    /**
//     * 双击返回桌面
//     */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if ((System.currentTimeMillis() - time > 1000)) {
//                Toast.makeText(this, "再按一次返回桌面", Toast.LENGTH_SHORT).show();
//                time = System.currentTimeMillis();
//            } else {
//                finish();
//            }
//            return true;
//        } else {
//            return super.onKeyDown(keyCode, event);
//        }
//
//    }


    @Override
    protected void onDestroy() {
        if (mRemoteReceiver != null) {
            mAudioManager.unregisterMediaButtonEventReceiver(mRemoteReceiver);
        }
        PlayService service = AppCache.getPlayService();
        if (service != null) {
            service.setOnPlayEventListener(null);
        }
        super.onDestroy();
    }

}
