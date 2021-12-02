package com.fork.kp;

import android.content.Context;

import com.fork.kp.service.ServiceManager;

import java.lang.ref.WeakReference;

import me.weishu.reflection.Reflection;
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
public class DaemonSDK {
    private static DaemonSDK _instance = new DaemonSDK();
    private static WeakReference<Context> mCtxWeak;

    public static void onCreate(Context context) {
        mCtxWeak = new WeakReference<>(context.getApplicationContext());
        _instance.initImpl(context);
    }

    public static void attachBaseContext(Context context) {
        Reflection.unseal(context);
    }

    public static Context getContext() {
        return mCtxWeak == null ? null : mCtxWeak.get();
    }

    private void initImpl(Context context) {
        ProcessTool.getInstance().init(context);
        //进来先清空一次文件
        if (ProcessTool.getInstance().isMainProcess()) {
            ProcessFile.init(context);
            ProcessFile.deleteAllIndicatorFile(context);
        }

        if (ProcessTool.getInstance().isMainProcess() || ProcessTool.getInstance().isDaemonProcess()) {
            final Context ctx = context;
            new Thread(() -> {
                Thread.currentThread().setPriority(10);
                forkChild(ctx);
            }).start();
        }

        ServiceManager.startAllService(context);
    }


    public void forkChild(Context context) {
        ProcessFile.init(context);

        Logger.logd("forkChild,context=" + context);
        String processName = ProcessFile.getProcessName();
        String lockFile = ProcessFile.getLockFile(context);
        String waitFile = ProcessFile.getWaitFile(context);
        String indicatorFile = ProcessFile.getIndicatorFile(context);
        String waitIndicatorFile = ProcessFile.getWaitIndicatorFile(context);
        Logger.logd("===============forkChild log start ==============");
        Logger.logd("forkChild,forkName=" + processName);
        Logger.logd("forkChild,forkLockFile=" + lockFile);
        Logger.logd("forkChild,forkWaitFile=" + waitFile);
        Logger.logd("forkChild,forkIndicatorFile=" + indicatorFile);
        Logger.logd("forkChild,forkWaitIndicatorFile=" + waitIndicatorFile);
        Logger.logd("===============forkChild log end==============");
        DaemonNative.forkChild(processName, lockFile, waitFile, indicatorFile, waitIndicatorFile);
    }

}
