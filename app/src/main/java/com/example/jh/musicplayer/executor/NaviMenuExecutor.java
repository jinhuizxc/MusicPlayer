package com.example.jh.musicplayer.executor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.MenuItem;

import com.example.jh.musicplayer.R;
import com.example.jh.musicplayer.app.AppCache;
import com.example.jh.musicplayer.service.PlayService;
import com.example.jh.musicplayer.ui.activity.AboutActivity;
import com.example.jh.musicplayer.ui.activity.MusicActivity;
import com.example.jh.musicplayer.ui.activity.SettingActivity;
import com.example.jh.musicplayer.utils.Preferences;
import com.example.jh.musicplayer.utils.ToastUtils;

/**
 * Created by jinhui on 2017/6/7.
 * 邮箱: 1004260403@qq.com
 *
 * 导航菜单执行器
 */

public class NaviMenuExecutor {

    public static boolean onNavigationItemSelected(MenuItem item, Context context) {
        switch (item.getItemId()) {
            case R.id.action_setting:
                // 功能设置
                startActivity(context, SettingActivity.class);
                return true;
            case R.id.action_night:
                // 夜间模式
                nightMode(context);
                break;
            case R.id.action_timer:
                // 定时停止播放播放
                timerDialog(context);
                return true;
            case R.id.action_exit:
                // 退出应用
                exit(context);
                return true;
            case R.id.action_about:
                // 关于
                startActivity(context, AboutActivity.class);
                return true;
        }
        return false;
    }

    private static void startActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    private static void nightMode(Context context) {
        if (!(context instanceof MusicActivity)) {
            return;
        }
        final MusicActivity activity = (MusicActivity) context;
        final boolean on = !Preferences.isNightMode();
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setCancelable(false);
        dialog.show();
        AppCache.updateNightMode(on);
        Handler handler = new Handler(activity.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.cancel();
                activity.recreate();
                Preferences.saveNightMode(on);
            }
        }, 500);
    }

    private static void timerDialog(final Context context) {
        if (!(context instanceof MusicActivity)) {
            return;
        }
        new AlertDialog.Builder(context)
                .setTitle(R.string.menu_timer)
                .setItems(context.getResources().getStringArray(R.array.timer_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int[] times = context.getResources().getIntArray(R.array.timer_int);
                        startTimer(context, times[which]);
                    }
                })
                .show();
    }

     // 设置定时播放
    private static void startTimer(Context context, int minute) {
        if (!(context instanceof MusicActivity)) {
            return;
        }

        MusicActivity activity = (MusicActivity) context;
        PlayService service = activity.getPlayService();
        service.startQuitTimer(minute * 60 * 1000);
        if (minute > 0) {
            ToastUtils.show(context.getString(R.string.timer_set, String.valueOf(minute)));
        } else {
            ToastUtils.show(R.string.timer_cancel);
        }
    }

     // 退出应用
    private static void exit(Context context) {
        if (!(context instanceof MusicActivity)) {
            return;
        }

        MusicActivity activity = (MusicActivity) context;
        activity.finish();
        PlayService service = AppCache.getPlayService();
        if (service != null) {
            service.stop();
        }
    }
}
