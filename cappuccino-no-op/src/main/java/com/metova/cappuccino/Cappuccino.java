package com.metova.cappuccino;

import android.view.View;

public class Cappuccino {

    private static final CappuccinoResourceWatcher NO_OP_RESOURCE_WATCHER = new CappuccinoResourceWatcher();

    private Cappuccino() {
        // satisfy checkstyle
    }

    public static CappuccinoResourceWatcher newIdlingResourceWatcher(Object ignored) {
        return NO_OP_RESOURCE_WATCHER;
    }

    public static CappuccinoResourceWatcher newIdlingResourceWatcher(String ignored) {
        return NO_OP_RESOURCE_WATCHER;
    }

    public static String nameOf(Object ignored) {
        return "";
    }

    public static CappuccinoResourceWatcher getResourceWatcher(Object ignored) {
        return NO_OP_RESOURCE_WATCHER;
    }

    public static CappuccinoResourceWatcher getResourceWatcher(String ignored) {
        return NO_OP_RESOURCE_WATCHER;
    }

    public static void markAsBusy(Object ignored) {
    }

    public static void markAsBusy(String ignored) {
    }

    public static void markAsIdle(Object ignored) {
    }

    public static void markAsIdle(String ignored) {
    }

    public static void registerIdlingResource(Object ignored) {
    }

    public static void registerIdlingResource(String ignored) {
    }

    public static void unregisterIdlingResource(Object ignored) {
    }

    public static void unregisterIdlingResource(String ignored) {
    }

    private static boolean isTesting() {
        return false;
    }

    public static void setIsTesting(boolean ignored) {
    }

    public static void reset() {
    }

    public static void setTagForTesting(View view, Object tag) {
    }

    public static void setTagForTesting(View view, int key, Object tag) {
    }
}