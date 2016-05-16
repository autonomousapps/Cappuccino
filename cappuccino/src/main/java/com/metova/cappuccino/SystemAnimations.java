package com.metova.cappuccino;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.NonNull;

import java.lang.reflect.Method;

/**
 * Disable animations so that they do not interfere with Espresso tests.
 */
public final class SystemAnimations {

    private static SystemAnimations INSTANCE = null;

    private static final String ANIMATION_PERMISSION = Manifest.permission.SET_ANIMATION_SCALE;//"android.permission.SET_ANIMATION_SCALE";
    private static final float DISABLED = 0.0f;
    private static final float DEFAULT = 1.0f;

    public SystemAnimations(Context context) {
        int permStatus = context.checkCallingOrSelfPermission(ANIMATION_PERMISSION);
        if (permStatus != PackageManager.PERMISSION_GRANTED) {
            throw new IllegalStateException("Application not granted access to animations");
        }
    }

    private static SystemAnimations getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            synchronized (SystemAnimations.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SystemAnimations(context);
                }
            }
        }
        return INSTANCE;
    }

    private void disableAll() {
        setSystemAnimationsScale(DISABLED);
    }

    private void enableAll() {
        setSystemAnimationsScale(DEFAULT);
    }

    private void setSystemAnimationsScale(float animationScale) {
        try {
            Class<?> windowManagerStubClazz = Class.forName("android.view.IWindowManager$Stub");
            Method asInterface = windowManagerStubClazz.getDeclaredMethod("asInterface", IBinder.class);
            Class<?> serviceManagerClazz = Class.forName("android.os.ServiceManager");
            Method getService = serviceManagerClazz.getDeclaredMethod("getService", String.class);
            Class<?> windowManagerClazz = Class.forName("android.view.IWindowManager");
            Method setAnimationScales = windowManagerClazz.getDeclaredMethod("setAnimationScales", float[].class);
            Method getAnimationScales = windowManagerClazz.getDeclaredMethod("getAnimationScales");

            IBinder windowManagerBinder = (IBinder) getService.invoke(null, "window");
            Object windowManagerObj = asInterface.invoke(null, windowManagerBinder);
            float[] currentScales = (float[]) getAnimationScales.invoke(windowManagerObj);
            for (int i = 0; i < currentScales.length; i++) {
                currentScales[i] = animationScale;
            }
            setAnimationScales.invoke(windowManagerObj, new Object[]{currentScales});
        } catch (Exception e) {
            throw new InternalError("Could not change animation scale to " + animationScale);
        }
    }

    public static void disableAll(Context context) {
        getInstance(context).disableAll();
    }

    public static void enableAll(Context context) {
        getInstance(context).enableAll();
    }
}