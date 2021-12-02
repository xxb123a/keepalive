package com.fork.kp;

import android.content.Context;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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
public class ProcessFile {
    public static final Map<String, String> mLockFileMaps = new HashMap<>();
    public static final Map<String, String> mWaitFileMaps = new HashMap<>();
    public static final Map<String, String> mIndicatorFileMaps = new HashMap<>();
    public static final Map<String, String> mWaitIndicatorFileMaps = new HashMap<>();
    public static final Map<String, String> mProcessMaps = new HashMap<>();
    public static boolean isInit = false;

    public static void init(Context context) {
        if (isInit) return;
        isInit = true;
        String packageName = context.getPackageName();
        String daemonPackage = packageName + ":daemon";

        mProcessMaps.put(packageName, "main");
        mProcessMaps.put(daemonPackage, "daemon");

        mLockFileMaps.put(packageName, "main_c");
        mLockFileMaps.put(daemonPackage, "daemon_c");

        mWaitFileMaps.put(packageName, "daemon_c");
        mWaitFileMaps.put(daemonPackage, "main_c");

        mIndicatorFileMaps.put(packageName, "main_indicator");
        mIndicatorFileMaps.put(daemonPackage, "daemon_indicator");

        mWaitIndicatorFileMaps.put(packageName, "daemon_indicator");
        mWaitIndicatorFileMaps.put(daemonPackage, "main_indicator");

    }

    public static String getProcessName() {
        String processName = ProcessTool.getCurrentProcessName();
        if (processName != null) {
            return mProcessMaps.get(processName);
        }
        throw new IllegalStateException("please init ProcessHolder first");
    }

    public static String getIndicatorFile(Context context) {
        return getProcessFilePath(context, mIndicatorFileMaps);
    }

    public static String getLockFile(Context context) {
        return getProcessFilePath(context, mLockFileMaps);
    }

    public static String getWaitFile(Context context) {
        return getProcessFilePath(context, mWaitFileMaps);
    }

    public static String getWaitIndicatorFile(Context context) {
        return getProcessFilePath(context, mWaitIndicatorFileMaps);
    }


    public static String getProcessFilePath(Context context, Map<String, String> map) {
        return getAbsolutePath(context, ProcessTool.getCurrentProcessName(), map);
    }

    public static String getAbsolutePath(Context context, String processName, Map<String, String> map) {
        if (processName != null) {
            String subpath = map.get(processName);
            File path = getDaemonFile(context); // context.getFilesDir()	路径是: /data/data/< package name >/files/
            if (subpath == null) {
                return null;
            }
            return new File(path, subpath).getAbsolutePath();
        }
        throw new IllegalStateException("please init ProcessHolder first");
    }

    public static void deleteAllIndicatorFile(Context context) {
        File file = getDaemonFile(context);
        try {
            for (String value : mIndicatorFileMaps.values()) {
                File indicator = new File(file, value);
                if (indicator.exists()) {
                    indicator.delete();
                }
            }
        } catch (Exception e) {
        }
    }

    private static File getDaemonFile(Context context) {
        File file = new File(context.getFilesDir(), "daemon");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static void deleteAllDaemonFile(Context context) {
        File file = getDaemonFile(context);
        try {
            File[] files = file.listFiles();
            if (files == null || files.length == 0) return;
            for (File listFile : files) {
                if (listFile.isFile()) {
                    listFile.delete();
                }
            }
        } catch (Exception e) {
        }
    }
}
