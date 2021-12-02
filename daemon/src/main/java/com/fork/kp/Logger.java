package com.fork.kp;

import android.util.Log;

/**
 * _    .--,       .--,
 * _   ( (  \\.---./  ) )
 * _    '.__/o   o\\__.'
 * _       {=  ^  =}
 * _        >  -  <
 * _       /       \\
 * _      //       \\\\
 * _     //|   .   |\\\\
 * _     \"'\\       /'\"_.-~^`'-.
 * _        \\  _  /--'         `
 * _      ___)( )(___
 * _     (((__) (__)))    高山仰止,景行行止.虽不能至,心向往之。
 * author      : xue
 * date        : 2021/12/1
 * email       : xuexiaobo@palmax.cn
 * description :
 */
public class Logger {
    public static boolean logEnable = true;

    public static void logd(String message) {
        if (logEnable) {
            Log.d("daemon : ", message);
        }
    }
    public static void loge(String message) {
        if (logEnable) {
            Log.e("daemon : ", message);
        }
    }
}
