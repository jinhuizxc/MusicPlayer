package com.example.jh.musicplayer.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.jh.musicplayer.R;
import com.example.jh.musicplayer.app.AppCache;
import com.example.jh.musicplayer.base.BaseFragment;
import com.example.jh.musicplayer.constants.Extras;
import com.example.jh.musicplayer.enums.LoadStateEnum;
import com.example.jh.musicplayer.model.SongListInfo;
import com.example.jh.musicplayer.ui.activity.OnlineMusicActivity;
import com.example.jh.musicplayer.ui.adapter.SongListAdapter;
import com.example.jh.musicplayer.utils.NetworkUtils;
import com.example.jh.musicplayer.utils.ViewUtils;
import com.example.jh.musicplayer.utils.binding.Bind;

import java.util.List;

/**
 * Created by jinhui on 2017/6/7.
 * 邮箱: 1004260403@qq.com
 *
 * 在线音乐
 */

public class SongListFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    @Bind(R.id.lv_song_list)
    private ListView lvSongList;
    @Bind(R.id.ll_loading)
    private LinearLayout llLoading;
    @Bind(R.id.ll_load_fail)
    private LinearLayout llLoadFail;
    private List<SongListInfo> mSongLists;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_song_list, container, false);
    }

    @Override
    protected void init() {
        // 初始化网络
        if (!NetworkUtils.isNetworkAvailable(getContext())) {
            ViewUtils.changeViewState(lvSongList, llLoading, llLoadFail, LoadStateEnum.LOAD_FAIL);
            return;
        }
        mSongLists = AppCache.getSongListInfos();
        if (mSongLists.isEmpty()) {
            String[] titles = getResources().getStringArray(R.array.online_music_list_title);
            String[] types = getResources().getStringArray(R.array.online_music_list_type);
            for (int i = 0; i < titles.length; i++) {
                SongListInfo info = new SongListInfo();
                info.setTitle(titles[i]);
                info.setType(types[i]);
                mSongLists.add(info);
            }
        }
        SongListAdapter adapter = new SongListAdapter(mSongLists);
        lvSongList.setAdapter(adapter);
    }

    @Override
    protected void setListener() {
        lvSongList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SongListInfo songListInfo = mSongLists.get(position);
        Intent intent = new Intent(getContext(), OnlineMusicActivity.class);
        intent.putExtra(Extras.MUSIC_LIST_TYPE, songListInfo);
        startActivity(intent);
    }
}

