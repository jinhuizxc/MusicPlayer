package com.example.jh.musicplayer.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by jinhui on 2017/6/7.
 * 邮箱: 1004260403@qq.com
 *
 * Toast工具类
 */

public class ToastUtils {

    private static Context sContext;
    private static Toast sToast;

    public static void init(Context context){
        sContext = context.getApplicationContext();
    }
    // 显示toast
    public static void show(int resId){
        show(sContext.getString(resId));
    }
    // 显示字符串
    public static void show(String text){
        if(sToast == null){
            sToast = Toast.makeText(sContext, text, Toast.LENGTH_SHORT);
        }else {
            sToast.setText(text);
        }
        sToast.show();
    }
}
