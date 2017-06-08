package com.example.jh.musicplayer.key;

import java.lang.reflect.Field;

/**
 * Created by jinhui on 2017/6/8.
 * 邮箱: 1004260403@qq.com
 */

public class KeyStore {

    public static final String BUGLY_APP_ID = "BUGLY_APP_ID";

    public static String getKey(String keyName)   {;
        try {
            String className = KeyStore.class.getPackage().getName() + ".Keys";
            Class apiKey = Class.forName(className);
            Field field = apiKey.getField(keyName);
            field.setAccessible(true);
            return (String) field.get(null);
        } catch (Exception ignored) {
        }
        return "";
    }
}
