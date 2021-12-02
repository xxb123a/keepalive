package com.fork.kp;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcel;

import java.lang.reflect.Field;


public class AMSHelper {

    private IBinder mRemote;
    private Parcel mInstrumentData;
    private Parcel mServiceData;
    private Parcel mBroadcastData;

    public final int mInstrumentatCode;

    private Context mContext;
    private Class<?> mInstrumentClass;
    private ComponentName mComponentName;

    public AMSHelper(Context context, Class<?> instrument) {
        mContext = context;
        mInstrumentClass = instrument;
        mComponentName = new ComponentName(context, mInstrumentClass);
        initAmsBinder();
        mInstrumentatCode = getFieldValue("TRANSACTION_startInstrumentation", "START_INSTRUMENTATION_TRANSACTION");
        Logger.logd(String.format("AMSHelper() instrument code %1$d", Integer.valueOf(mInstrumentatCode)));
        initInstrumentParcel();
    }

    private int getFieldValue(String str, String str2) {
        try {
            Class<?> ams = Class.forName("android.app.IActivityManager$Stub");
            Field declaredField = ams.getDeclaredField(str);
            declaredField.setAccessible(true);
            return declaredField.getInt(ams);
        } catch (Exception e) {
            try {
                Class<?> ams = Class.forName("android.app.IActivityManager");
                Field declaredField = ams.getDeclaredField(str2);
                declaredField.setAccessible(true);
                return declaredField.getInt(ams);
            } catch (Exception e2) {
                return -1;
            }
        }
    }

    @SuppressLint({"PrivateApi"})
    private void initAmsBinder() {
        try {
            Class<?> cls = Class.forName("android.app.ActivityManagerNative");
            Object invoke = cls.getMethod("getDefault").invoke(cls);
            Field remoteField = invoke.getClass().getDeclaredField("mRemote");
            remoteField.setAccessible(true);
            mRemote = (IBinder) remoteField.get(invoke);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void initInstrumentParcel() {
        ComponentName componentName = new ComponentName(mContext, mInstrumentClass);
        mInstrumentData = Parcel.obtain();
        if (Build.VERSION.SDK_INT >= 26) {
            mInstrumentData.writeInterfaceToken("android.app.IActivityManager");
            mInstrumentData.writeInt(1);
            componentName.writeToParcel(mInstrumentData, 0);
            mInstrumentData.writeString(null);
            mInstrumentData.writeInt(0);
            mInstrumentData.writeInt(0);
            mInstrumentData.writeStrongBinder(null);
            mInstrumentData.writeStrongBinder(null);
            mInstrumentData.writeInt(0);
            mInstrumentData.writeString(null);
        } else if (Build.VERSION.SDK_INT >= 23) {
            mInstrumentData.writeInterfaceToken("android.app.IActivityManager");
            ComponentName.writeToParcel(componentName, mInstrumentData);
            mInstrumentData.writeString(null);
            mInstrumentData.writeInt(0);
            mInstrumentData.writeBundle(null);
            mInstrumentData.writeStrongBinder(null);
            mInstrumentData.writeStrongBinder(null);
            mInstrumentData.writeInt(0);
            mInstrumentData.writeString(null);
        } else {
            mInstrumentData.writeInterfaceToken("android.app.IActivityManager");
            ComponentName.writeToParcel(componentName, mInstrumentData);
            mInstrumentData.writeString(null);
            mInstrumentData.writeInt(0);
            mInstrumentData.writeBundle(null);
            mInstrumentData.writeStrongBinder(null);
            mInstrumentData.writeStrongBinder(null);
            mInstrumentData.writeInt(0);
        }
    }


    public boolean startInstrumentByAmsBinder() {
        try {
            if (mInstrumentData != null) {
                Logger.logd("startInstrumentByAmsBinder");
                mRemote.transact(mInstrumentatCode, mInstrumentData, null, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                mContext.startInstrumentation(mComponentName, null, null);
            } catch (Exception e1) {
            }
        }
        return true;
    }
}
