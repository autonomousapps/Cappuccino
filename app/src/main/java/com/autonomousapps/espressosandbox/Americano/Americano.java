package com.autonomousapps.espressosandbox.Americano;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tony on 1/2/2016.
 */
public class Americano {

    private static boolean mIsTesting = true;

    private static final Map<String, AmericanoResourceWatcher> mMap = new HashMap<>();

    @NonNull
    public static AmericanoResourceWatcher newIdlingResourceWatcher(@NonNull Object resource) {
        return newIdlingResourceWatcher(nameOf(resource));
    }

    @NonNull
    public static AmericanoResourceWatcher newIdlingResourceWatcher(@NonNull String name) {
        AmericanoResourceWatcher watcher;
        if (isTesting()) {
            watcher = new OperatingAmericanoResourceWatcher();
        } else {
            watcher = new NoOpAmericanoResourceWatcher();
        }
        mMap.put(name, watcher);
        return watcher;
    }

    @NonNull
    public static String nameOf(@NonNull Object object) {
        String name = object.getClass().getCanonicalName();
        name = name != null ? name : object.getClass().getSimpleName();
        return name;
    }

    @NonNull
    public static AmericanoResourceWatcher getResourceWatcher(@NonNull Object object) {
        return getResourceWatcher(nameOf(object));
    }

    @NonNull
    public static AmericanoResourceWatcher getResourceWatcher(@NonNull String name) {
        if (!mMap.containsKey(name)) {
            throw new IllegalArgumentException( // TODO define a new Exception type
                    String.format("There is no %s associated with the name %s", AmericanoResourceWatcher.class.getName(), name));
        }

        return mMap.get(name);
    }

    @NonNull
    public static IdlingResource registerIdlingResource(@NonNull Object object) {
        return registerIdlingResource(nameOf(object));
    }

    @NonNull
    public static IdlingResource registerIdlingResource(@NonNull String name) {
        AmericanoResource idlingResource = new AmericanoResource(name);
        Espresso.registerIdlingResources(idlingResource);
        return idlingResource;
    }

    public static void unregisterIdlingResource(@NonNull Object object) {
        unregisterIdlingResource(nameOf(object));
    }

    public static void unregisterIdlingResource(@NonNull String name) {
        AmericanoResource idlingResource = new AmericanoResource(name);
        Espresso.unregisterIdlingResources(idlingResource);
    }

    private static boolean isTesting() {
        return mIsTesting;
    }

    @VisibleForTesting
    static void setIsTesting(boolean isTesting) {
        mIsTesting = isTesting;
    }

    @VisibleForTesting
    static void reset() {
        mMap.clear();
        mIsTesting = true;
    }
}