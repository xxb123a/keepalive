package com.fork.kp;

import android.app.Instrumentation;
import android.os.Bundle;

import androidx.annotation.Keep;

@Keep
/* renamed from: com.vi.daemon.ViInstrumentation */
public class ViInstrumentation extends Instrumentation {
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Logger.logd("ViInstrumentation onCreate");
    }

    public void onDestroy() {
        super.onDestroy();
        Logger.logd("ViInstrumentation onDestroy");
    }
}
