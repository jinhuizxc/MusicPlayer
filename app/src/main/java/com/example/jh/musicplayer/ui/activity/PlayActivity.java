package com.example.jh.musicplayer.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.jh.musicplayer.R;
import com.example.jh.musicplayer.base.BaseActivity;
import com.example.jh.musicplayer.constants.Actions;
import com.example.jh.musicplayer.enums.PlayModeEnum;
import com.example.jh.musicplayer.executor.SearchLrc;
import com.example.jh.musicplayer.model.Music;
import com.example.jh.musicplayer.model.Type;
import com.example.jh.musicplayer.service.OnPlayerEventListener;
import com.example.jh.musicplayer.ui.adapter.PlayPagerAdapter;
import com.example.jh.musicplayer.utils.CoverLoader;
import com.example.jh.musicplayer.utils.FileUtils;
import com.example.jh.musicplayer.utils.Preferences;
import com.example.jh.musicplayer.utils.ScreenUtils;
import com.example.jh.musicplayer.utils.SystemUtils;
import com.example.jh.musicplayer.utils.ToastUtils;
import com.example.jh.musicplayer.utils.binding.Bind;
import com.example.jh.musicplayer.widget.AlbumCoverView;
import com.example.jh.musicplayer.widget.IndicatorLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.wcy.lrcview.LrcView;

/**
 * Created by jinhui on 2017/6/14.
 * 邮箱: 1004260403@qq.com
 */

//public class PlayActivity extends BaseActivity implements View.OnClickListener,OnPlayerEventListener,
//        SeekBar.OnSeekBarChangeListener, ViewPager.OnPageChangeListener {
//
//    @Bind(R.id.ll_content)
//    private LinearLayout llContent;
//    @Bind(R.id.iv_play_page_bg)
//    private ImageView ivPlayingBg;
//    @Bind(R.id.iv_back)
//    private ImageView ivBack;
//    @Bind(R.id.tv_title)
//    private TextView tvTitle;
//    @Bind(R.id.tv_artist)
//    private TextView tvArtist;
//    @Bind(R.id.vp_play_page)
//    private ViewPager vpPlay;
//    @Bind(R.id.il_indicator)
//    private IndicatorLayout ilIndicator;
//    @Bind(R.id.sb_progress)
//    private SeekBar sbProgress;
//    @Bind(R.id.tv_current_time)
//    private TextView tvCurrentTime;
//    @Bind(R.id.tv_total_time)
//    private TextView tvTotalTime;
//    @Bind(R.id.iv_mode)
//    private ImageView ivMode;
//    @Bind(R.id.iv_play)
//    private ImageView ivPlay;
//    @Bind(R.id.iv_next)
//    private ImageView ivNext;
//    @Bind(R.id.iv_prev)
//    private ImageView ivPrev;
//    private AlbumCoverView mAlbumCoverView;
//    private LrcView mLrcViewSingle;
//    private LrcView mLrcViewFull;
//    private SeekBar sbVolume;
//    private AudioManager mAudioManager;
//    private List<View> mViewPagerContent;
//    private int mLastProgress;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_play);
//        // 初始化布局
//        initView();
//    }
//
//    @Override
//    protected void initView() {
//        initSystemBar();
//        initViewPager();
//        ilIndicator.create(mViewPagerContent.size());
//        initPlayMode();
//        onChange(getPlayService().getPlayingMusic());
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        IntentFilter filter = new IntentFilter(Actions.VOLUME_CHANGED_ACTION);
////        getContext().registerReceiver(mVolumeReceiver, filter);
//         this.registerReceiver(mVolumeReceiver, filter);
//    }
//
//    @Override
//    protected void setListener() {
//        ivBack.setOnClickListener(this);
//        ivMode.setOnClickListener(this);
//        ivPlay.setOnClickListener(this);
//        ivPrev.setOnClickListener(this);
//        ivNext.setOnClickListener(this);
//        sbProgress.setOnSeekBarChangeListener(this);
//        sbVolume.setOnSeekBarChangeListener(this);
//        vpPlay.setOnPageChangeListener(this);
//    }
//
//    /**
//     * 沉浸式状态栏
//     */
//    private void initSystemBar() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            int top = ScreenUtils.getSystemBarHeight();
//            llContent.setPadding(0, top, 0, 0);
//        }
//    }
//
//    private void initViewPager() {
//        View coverView = LayoutInflater.from(this).inflate(R.layout.fragment_play_page_cover, null);
//        View lrcView = LayoutInflater.from(this).inflate(R.layout.fragment_play_page_lrc, null);
//        mAlbumCoverView = (AlbumCoverView) coverView.findViewById(R.id.album_cover_view);
//        mLrcViewSingle = (LrcView) coverView.findViewById(R.id.lrc_view_single);
//        mLrcViewFull = (LrcView) lrcView.findViewById(R.id.lrc_view_full);
//        sbVolume = (SeekBar) lrcView.findViewById(R.id.sb_volume);
//        mAlbumCoverView.initNeedle(getPlayService().isPlaying());
//        initVolume();
//
//        mViewPagerContent = new ArrayList<>(2);
//        mViewPagerContent.add(coverView);
//        mViewPagerContent.add(lrcView);
//        vpPlay.setAdapter(new PlayPagerAdapter(mViewPagerContent));
//    }
//
//    private void initVolume() {
//        mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
//        sbVolume.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
//        sbVolume.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
//    }
//
//    private void initPlayMode() {
//        int mode = Preferences.getPlayMode();
//        ivMode.setImageLevel(mode);
//    }
//
//    /**
//     * 更新播放进度
//     */
//    public void onPublish(int progress) {
//
//            sbProgress.setProgress(progress);
//            if (mLrcViewSingle.hasLrc()) {
//                mLrcViewSingle.updateTime(progress);
//                mLrcViewFull.updateTime(progress);
//
//            //更新当前播放时间
//            if (progress - mLastProgress >= 1000) {
//                tvCurrentTime.setText(formatTime(progress));
//                mLastProgress = progress;
//            }
//        }
//    }
//
//    public void onBufferingUpdate(int percent) {
//
//            sbProgress.setSecondaryProgress(sbProgress.getMax() * 100 / percent);
//
//    }
//
//    public void onChange(Music music) {
//
//            onPlay(music);
//
//    }
//
//    public void onPlayerPause() {
//
//            ivPlay.setSelected(false);
//            mAlbumCoverView.pause();
//
//    }
//
//    public void onPlayerResume() {
//
//            ivPlay.setSelected(true);
//            mAlbumCoverView.start();
//
//    }
//
//    @Override
//    public void onTimer(long remain) {
//
//    }
//
//    @Override
//    public void onMusicListUpdate() {
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.iv_back:
//                onBackPressed();
//                break;
//            case R.id.iv_mode:
//                switchPlayMode();
//                break;
//            case R.id.iv_play:
//                play();
//                break;
//            case R.id.iv_next:
//                next();
//                break;
//            case R.id.iv_prev:
//                prev();
//                break;
//        }
//    }
//
//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//    }
//
//    @Override
//    public void onPageSelected(int position) {
//        ilIndicator.setCurrent(position);
//    }
//
//    @Override
//    public void onPageScrollStateChanged(int state) {
//    }
//
//    @Override
//    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//    }
//
//    @Override
//    public void onStartTrackingTouch(SeekBar seekBar) {
//    }
//
//    @Override
//    public void onStopTrackingTouch(SeekBar seekBar) {
//        if (seekBar == sbProgress) {
//            if (getPlayService().isPlaying() || getPlayService().isPausing()) {
//                int progress = seekBar.getProgress();
//                getPlayService().seekTo(progress);
//                mLrcViewSingle.onDrag(progress);
//                mLrcViewFull.onDrag(progress);
//                tvCurrentTime.setText(formatTime(progress));
//                mLastProgress = progress;
//            } else {
//                seekBar.setProgress(0);
//            }
//        } else if (seekBar == sbVolume) {
//            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, seekBar.getProgress(),
//                    AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
//        }
//    }
//
//    private void onPlay(Music music) {
//        if (music == null) {
//            return;
//        }
//
//        tvTitle.setText(music.getTitle());
//        tvArtist.setText(music.getArtist());
//        sbProgress.setProgress(0);
//        sbProgress.setSecondaryProgress(0);
//        sbProgress.setMax((int) music.getDuration());
//        mLastProgress = 0;
//        tvCurrentTime.setText(R.string.play_time_start);
//        tvTotalTime.setText(formatTime(music.getDuration()));
//        setCoverAndBg(music);
//        setLrc(music);
//        if (getPlayService().isPlaying() || getPlayService().isPreparing()) {
//            ivPlay.setSelected(true);
//            mAlbumCoverView.start();
//        } else {
//            ivPlay.setSelected(false);
//            mAlbumCoverView.pause();
//        }
//    }
//
//    private void play() {
//        getPlayService().playPause();
//    }
//
//    private void next() {
//        getPlayService().next();
//    }
//
//    private void prev() {
//        getPlayService().prev();
//    }
//
//    private void switchPlayMode() {
//        PlayModeEnum mode = PlayModeEnum.valueOf(Preferences.getPlayMode());
//        switch (mode) {
//            case LOOP:
//                mode = PlayModeEnum.SHUFFLE;
//                ToastUtils.show(R.string.mode_shuffle);
//                break;
//            case SHUFFLE:
//                mode = PlayModeEnum.SINGLE;
//                ToastUtils.show(R.string.mode_one);
//                break;
//            case SINGLE:
//                mode = PlayModeEnum.LOOP;
//                ToastUtils.show(R.string.mode_loop);
//                break;
//        }
//        Preferences.savePlayMode(mode.value());
//        initPlayMode();
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        this.onBackPressed();
//        ivBack.setEnabled(false);
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ivBack.setEnabled(true);
//            }
//        }, 300);
//    }
//
//    private void setCoverAndBg(Music music) {
//        mAlbumCoverView.setCoverBitmap(CoverLoader.getInstance().loadRound(music));
//        ivPlayingBg.setImageBitmap(CoverLoader.getInstance().loadBlur(music));
//    }
//
//    private void setLrc(final Music music) {
//        if (music.getType() == Type.LOCAL) {
//            String lrcPath = FileUtils.getLrcFilePath(music);
//            if (!TextUtils.isEmpty(lrcPath)) {
//                loadLrc(lrcPath);
//            } else {
//                new SearchLrc(music.getArtist(), music.getTitle()) {
//                    @Override
//                    public void onPrepare() {
//                        // 设置tag防止歌词下载完成后已切换歌曲
//                        vpPlay.setTag(music);
//
//                        loadLrc("");
//                        setLrcLabel("正在搜索歌词");
//                    }
//
//                    @Override
//                    public void onExecuteSuccess(@NonNull String lrcPath) {
//                        if (vpPlay.getTag() != music) {
//                            return;
//                        }
//
//                        // 清除tag
//                        vpPlay.setTag(null);
//
//                        loadLrc(lrcPath);
//                        setLrcLabel("暂无歌词");
//                    }
//
//                    @Override
//                    public void onExecuteFail(Exception e) {
//                        if (vpPlay.getTag() != music) {
//                            return;
//                        }
//
//                        // 清除tag
//                        vpPlay.setTag(null);
//
//                        setLrcLabel("暂无歌词");
//                    }
//                }.execute();
//            }
//        } else {
//            String lrcPath = FileUtils.getLrcDir() + FileUtils.getLrcFileName(music.getArtist(), music.getTitle());
//            loadLrc(lrcPath);
//        }
//    }
//
//    private void loadLrc(String path) {
//        File file = new File(path);
//        mLrcViewSingle.loadLrc(file);
//        mLrcViewFull.loadLrc(file);
//    }
//
//    private void setLrcLabel(String label) {
//        mLrcViewSingle.setLabel(label);
//        mLrcViewFull.setLabel(label);
//    }
//
//    private String formatTime(long time) {
//        return SystemUtils.formatTime("mm:ss", time);
//    }
//
//    private BroadcastReceiver mVolumeReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            sbVolume.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
//        }
//    };
//
//    @Override
//    public void onDestroy() {
//        this.unregisterReceiver(mVolumeReceiver);
//        super.onDestroy();
//    }
//
//}
