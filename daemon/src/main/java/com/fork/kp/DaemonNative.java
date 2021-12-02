package com.fork.kp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;

import androidx.annotation.Keep;

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
@Keep
public class DaemonNative {

    private static AMSHelper sAMSHelper;

    static {
        Context ctx = DaemonSDK.getContext();
        if (ctx != null) {
            sAMSHelper = new AMSHelper(DaemonSDK.getContext(), ViInstrumentation.class);
        }
        System.loadLibrary("vi_daemon");
    }

    public static native void forkChild(String processName, String lockFile, String waitFile, String indicatorFile, String waitIndicatorFile);

    @SuppressLint({"DiscouragedPrivateApi"})
    public static void setProcessName(String processName) {
        try {
            Process.class.getDeclaredMethod("setArgV0", String.class).invoke(null, processName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void restartProcess() {
        if (sAMSHelper != null) {
            sAMSHelper.startInstrumentByAmsBinder();
        }
        Logger.loge("------------: restartProcess");
    }
}
