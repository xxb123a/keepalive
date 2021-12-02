package com.palmax.helloworld;

import android.app.Application;
import android.content.Context;
import android.os.SystemClock;

import com.fork.kp.DaemonSDK;
import com.fork.kp.ProcessTool;

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
public class TheApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DaemonSDK.onCreate(this);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    SystemClock.sleep(2000);
//                    System.out.println(ProcessTool.getProcessName() + " 我又回来了-----" + ProcessTool.getProcessName(getApplicationContext()));
//                }
//            }
//        }).start();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        DaemonSDK.attachBaseContext(base);
    }
}
