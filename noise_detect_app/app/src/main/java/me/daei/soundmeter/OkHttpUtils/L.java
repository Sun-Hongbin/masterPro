package me.daei.soundmeter.OkHttpUtils;

import android.util.Log;

/**
 * Created by SunHongbin on 2018/10/20
 */
public class L {

    private static final String Tag = "sunhongbin_okHttp";

    private static boolean debug = true;//不想显示debug信息就置为false

    public static void e(String msg) {
        if (debug) {
            Log.e(Tag, msg);
        }
    }
}
