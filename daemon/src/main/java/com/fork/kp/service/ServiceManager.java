package com.fork.kp.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;

import com.fork.kp.DaemonNative;
import com.fork.kp.Logger;

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
public class ServiceManager {
    public static void startAllService(Context context) {
        startService(context, DaemonService.class);
        startService(context, MainService.class);
    }

    public static void startService(Context context, Class<? extends Service> cls) {
        try {
            context.startService(new Intent(context, cls));
        } catch (Throwable th) {
            Logger.loge("startService error,clz=" + cls.getSimpleName());
            if (th instanceof IllegalStateException) {
                DaemonNative.restartProcess();
            }
        }
    }
}
