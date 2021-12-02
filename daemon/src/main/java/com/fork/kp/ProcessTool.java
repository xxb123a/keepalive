package com.fork.kp;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
public class ProcessTool {
    private static ProcessTool _instance = new ProcessTool();

    private boolean isMainProcess = false;
    private boolean isDaemonProcess = false;
    private String mProcessName;


    public boolean isMainProcess() {
        return isMainProcess;
    }

    public boolean isDaemonProcess() {
        return isDaemonProcess;
    }

    public static ProcessTool getInstance(){
        return _instance;
    }

    public void init(Context context) {
        requestProcessName(context);
        String pkgName = context.getPackageName();
        if (TextUtils.equals(mProcessName, pkgName)) {
            isMainProcess = true;
        } else if (TextUtils.equals(mProcessName, pkgName + ":daemon")) {
            isDaemonProcess = true;
        }
    }

    private void requestProcessName(Context context) {
        if (TextUtils.isEmpty(mProcessName)) {
            mProcessName = getProcessName();
        }
        if (TextUtils.isEmpty(mProcessName)) {
            mProcessName = getProcessName(context);
        }
    }

    public static String getCurrentProcessName(){
       return _instance.mProcessName;
    }


    public static String getProcessName() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + Process.myPid() + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    // get by ams, 有时候获取进程名失败
    public static String getProcessName(Context context) {
        int myPid = Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : activityManager.getRunningAppProcesses()) {
            if (runningAppProcessInfo.pid == myPid) {
                return runningAppProcessInfo.processName;
            }
        }
        return null;
    }
}
