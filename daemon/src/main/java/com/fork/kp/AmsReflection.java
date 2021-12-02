package com.fork.kp;

import android.app.ActivityManager;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class AmsReflection {

    private static class InvocationHandlerImpl implements InvocationHandler {

        private Object object;

        public InvocationHandlerImpl(Object obj) {
            object = obj;
        }

        @Override
        public Object invoke(Object obj, Method method, Object[] objArr) {
            if (!"reportSizeConfigurations".equals(method.getName())) {
                try {
                    return method.invoke(object, objArr);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            try {
                Log.w("AmsFix", "reportSizeConfigurations invoke execute ");
                return method.invoke(object, objArr);
            } catch (Exception e) {
                Log.w("AmsFix", "reportSizeConfigurations exception: " + e.getMessage());
                return null;
            }
        }
    }

    public static void fix() {
        Class<? super Object> superclass;
        if (Build.VERSION.SDK_INT >= 26) {
            try {
                Field declaredField = ActivityManager.class.getDeclaredField("IActivityManagerSingleton");
                declaredField.setAccessible(true);
                Object obj = declaredField.get(null);
                if (obj != null && (superclass = (Class<? super Object>) obj.getClass().getSuperclass()) != null) {
                    Field declaredField2 = superclass.getDeclaredField("mInstance");
                    declaredField2.setAccessible(true);
                    Object obj2 = declaredField2.get(obj);
                    Class<?> cls = Class.forName("android.app.IActivityManager");
                    declaredField2.set(obj, Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, new InvocationHandlerImpl(obj2)));
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }
}
