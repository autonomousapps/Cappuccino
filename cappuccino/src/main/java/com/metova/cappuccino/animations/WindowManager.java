package com.metova.cappuccino.animations;

import android.os.IBinder;
import android.support.annotation.NonNull;

import java.lang.reflect.Method;

public class WindowManager {

    private Method setAnimationScales;
    private Object windowManagerObj;

    float[] getAnimationScales() throws Exception {
        Class<?> windowManagerStubClazz = Class.forName("android.view.IWindowManager$Stub");
        Method asInterface = windowManagerStubClazz.getDeclaredMethod("asInterface", IBinder.class);
        Class<?> serviceManagerClazz = Class.forName("android.os.ServiceManager");
        Method getService = serviceManagerClazz.getDeclaredMethod("getService", String.class);
        Class<?> windowManagerClazz = Class.forName("android.view.IWindowManager");
        setAnimationScales = windowManagerClazz.getDeclaredMethod("setAnimationScales", float[].class);
        Method getAnimationScales = windowManagerClazz.getDeclaredMethod("getAnimationScales");

        IBinder windowManagerBinder = (IBinder) getService.invoke(null, "window");
        windowManagerObj = asInterface.invoke(null, windowManagerBinder);

        return (float[]) getAnimationScales.invoke(windowManagerObj);
    }

    void setAnimationScales(@NonNull float[] currentScales) throws Exception {
        setAnimationScales.invoke(windowManagerObj, new Object[]{currentScales});
    }
}