package com.metova.cappuccino.animations;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

/**
 * Disable animations so that they do not interfere with Espresso tests.
 */
public final class SystemAnimations {

    private static SystemAnimations INSTANCE = null;

    private PermissionChecker mPermissionChecker = new PermissionChecker();
    private WindowManager mWindowManager = new WindowManager();

    private static final float DISABLED = 0.0f;
    private static final float DEFAULT = 1.0f;
    private static final float RESTORE = -1f;

    private float[] restoreScale = null;

    @VisibleForTesting
    SystemAnimations() {
    }

    private static SystemAnimations getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            synchronized (SystemAnimations.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SystemAnimations();
                    INSTANCE.checkPermissionStatus(context);
                }
            }
        }
        return INSTANCE;
    }

    // TODO add a Policy class that allows a warning to be emitted, instead
    @VisibleForTesting
    void checkPermissionStatus(@NonNull Context context) {
        boolean hasPermission = getPermissionChecker().hasAnimationPermission(context);
        if (!hasPermission) {
            throw new IllegalStateException(getPermissionErrorMessage());
        }
    }

    // region public API
    public static void disableAll(@NonNull Context context) {
        getInstance(context).disableAll();
    }

    public static void enableAll(@NonNull Context context) {
        getInstance(context).enableAll();
    }

    public static void restoreAll(@NonNull Context context) {
        getInstance(context).restoreAll();
    }
    // endregion public API

    @VisibleForTesting
    void disableAll() {
        setSystemAnimationsScale(DISABLED);
    }

    @VisibleForTesting
    void enableAll() {
        setSystemAnimationsScale(DEFAULT);
    }

    @VisibleForTesting
    void restoreAll() {
        if (restoreScale == null) {
            throw new IllegalStateException(getRestoreErrorMessage());
        }
        setSystemAnimationsScale(RESTORE);
    }

    private void setSystemAnimationsScale(float animationScale) {
        try {
            float[] currentScales = getWindowManager().getAnimationScales();
            float[] oldScales = new float[currentScales.length];
            if (restoreScale == null || animationScale != RESTORE) {
                restoreScale = new float[currentScales.length];
            }

            for (int i = 0; i < currentScales.length; i++) {
                oldScales[i] = currentScales[i];   // remember for later
                // Restoring previous scale
                if (animationScale == RESTORE) {
                    currentScales[i] = restoreScale[i];
                }
                // Not restoring, so use value passed in
                else {
                    currentScales[i] = animationScale; // set new
                }
                restoreScale[i] = oldScales[i];
            }
            getWindowManager().setAnimationScales(currentScales);
        } catch (Exception e) {
            throw new InternalError(getReflectionErrorMessage(animationScale));
        }
    }

    // region messages
    @VisibleForTesting
    @NonNull
    static String getPermissionErrorMessage() {
        return "Application not granted access to animations. Common causes for this exception include:\n" +
                "\tFailure to declare `<uses-permission android:name=\"android.permission.SET_ANIMATION_SCALE\" />` in the manifest;\n" +
                "\tFailure to apply the Cappuccino Animations plugin in your application's build.gradle file; and\n" +
                "\tFailure of the Cappuccino Animations plugin.\n" +
                "\tIf this last is the issue, consider adding a configuration to the `cappuccino {}` closure in the build.gradle file. For example:\n" +
                "\tcappuccino {\n" +
                "\t\texcludedConfigurations = ['badConfiguration', 'otherBadConfiguration', ...]\n" +
                "\t}";
    }

    @VisibleForTesting
    @NonNull
    static String getRestoreErrorMessage() {
        return "restoreAll() called when there is nothing to restore!";
    }

    @VisibleForTesting
    @NonNull
    static String getReflectionErrorMessage(float animationScale) {
        return "Could not change animation scale to " + animationScale;
    }
    // endregion messages

    // region methods to make testing easier
    private PermissionChecker getPermissionChecker() {
        return mPermissionChecker;
    }

    @VisibleForTesting
    void setPermissionChecker(@NonNull PermissionChecker permissionChecker) {
        mPermissionChecker = permissionChecker;
    }

    private WindowManager getWindowManager() {
        return mWindowManager;
    }

    @VisibleForTesting
    void setWindowManager(@NonNull WindowManager windowManager) {
        mWindowManager = windowManager;
    }
    // endregion methods to make testing easier
}