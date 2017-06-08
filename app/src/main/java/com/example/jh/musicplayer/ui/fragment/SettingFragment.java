package com.example.jh.musicplayer.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.example.jh.musicplayer.R;
import com.example.jh.musicplayer.utils.ToastUtils;

/**
 * Created by jinhui on 2017/6/7.
 * 邮箱: 1004260403@qq.com
 */

public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    private Preference mSoundEffect;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_setting);

        mSoundEffect = findPreference(getString(R.string.setting_key_sound_effect));
        mSoundEffect.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == mSoundEffect) {
            Intent intent = new Intent("android.media.action.DISPLAY_AUDIO_EFFECT_CONTROL_PANEL");
            intent.putExtra("android.media.extra.PACKAGE_NAME", getActivity().getPackageName());
            intent.putExtra("android.media.extra.CONTENT_TYPE", 0);
            intent.putExtra("android.media.extra.AUDIO_SESSION", 0);
            try {
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.show(R.string.device_not_support);
            }
            return true;
        }
        return false;
    }
}
