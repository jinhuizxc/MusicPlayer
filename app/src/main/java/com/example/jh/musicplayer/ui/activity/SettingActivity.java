package com.example.jh.musicplayer.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.jh.musicplayer.R;
import com.example.jh.musicplayer.base.BaseActivity;
import com.example.jh.musicplayer.ui.fragment.SettingFragment;

/**
 * Created by jinhui on 2017/6/7.
 * 邮箱: 1004260403@qq.com
 *
 * 功能设置界面
 */

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置toolbar
        setContentView(R.layout.activity_setting);
        // 检查服务是否存活
        if (!checkServiceAlive()) {
            return;
        }
        // 加载设置fragment
        getFragmentManager().beginTransaction().replace(R.id.ll_fragment_container, new SettingFragment()).commit();
    }


}
