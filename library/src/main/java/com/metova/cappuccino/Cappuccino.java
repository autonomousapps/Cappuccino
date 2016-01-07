package com.metova.cappuccino;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;

import java.util.HashMap;
import java.util.Map;

public class Cappuccino {

    private static boolean mIsTesting = true;

    // TODO find an elegant way to remove items from this map once they are no longer needed
    private static final Map<String, CappuccinoResourceWatcher> mResourceWatcherRegistry = new HashMap<>();

    private static final Map<String, CappuccinoIdlingResource> mIdlingResourceRegistry = new HashMap<>();

    /**
     * Returns a new {@code CappuccinoResourceWatcher}, which will be associated internally with a name derived from
     * {@param resource}. Internally, this uses either the canonical or simple name of the resource's class,
     * but this may change. Will return either a {@link OperatingCappuccinoResourceWatcher} if we're testing, or a
     * {@link NoOpCappuccinoResourceWatcher} if we're in production. See {@link #isTesting()}.
     *
     * @param resource The object for which this {@link CappuccinoResourceWatcher} will be a member.
     * @return an {@link CappuccinoResourceWatcher}.
     */
    @NonNull
    public static CappuccinoResourceWatcher newIdlingResourceWatcher(@NonNull Object resource) {
        return newIdlingResourceWatcher(nameOf(resource));
    }

    /**
     * Returns a new {@code CappuccinoResourceWatcher}, which will be associated internally with the name supplied.
     * Will return either a {@link OperatingCappuccinoResourceWatcher} if we're testing, or a {@link NoOpCappuccinoResourceWatcher}
     * if we're in production. See {@link #isTesting()}.
     *
     * @param name The name of this {@link CappuccinoResourceWatcher}.
     * @return an {@link CappuccinoResourceWatcher}.
     */
    @NonNull
    public static CappuccinoResourceWatcher newIdlingResourceWatcher(@NonNull String name) {
        CappuccinoResourceWatcher watcher;
        if (isTesting()) {
            watcher = new OperatingCappuccinoResourceWatcher();
        } else {
            watcher = new NoOpCappuccinoResourceWatcher();
        }
        mResourceWatcherRegistry.put(name, watcher);
        return watcher;
    }

    /**
     * Returns a name for the supplied {@code object}. Uses either {@code object.getClass().getCanonicalName()},
     * or {@code object.getClass().getSimpleName()} if the former returns null.
     *
     * @param object The {@code object} for which to generate a name.
     * @return a name for the supplied {@code object}.
     */
    @NonNull
    public static String nameOf(@NonNull Object object) {
        String name = object.getClass().getCanonicalName();
        name = name != null ? name : object.getClass().getSimpleName();
        return name;
    }

    /**
     * Returns the {@code CappuccinoResourceWatcher}, from the internal registry, associated
     * with the given {@param object}.
     *
     * @param object The object associated with the {@link CappuccinoResourceWatcher}.
     * @return the {@code CappuccinoResourceWatcher}, from the internal registry, associated
     * with the given {@param object}.
     * @throws IllegalArgumentException if there is no {@code CappuccinoResourceWatcher} associated
     *                                  with the given {@param object}.
     */
    @NonNull
    public static CappuccinoResourceWatcher getResourceWatcher(@NonNull Object object) {
        return getResourceWatcher(nameOf(object));
    }

    /**
     * Returns the {@code CappuccinoResourceWatcher}, from the internal registry, associated
     * with the given {@param name}.
     *
     * @param name The name associated with the {@link CappuccinoResourceWatcher}.
     * @return the {@code CappuccinoResourceWatcher}, from the internal registry, associated
     * with the given {@param name}.
     * @throws IllegalArgumentException if there is no {@code CappuccinoResourceWatcher} associated
     *                                  with the given {@param name}.
     */
    @NonNull
    public static CappuccinoResourceWatcher getResourceWatcher(@NonNull String name) {
        if (!mResourceWatcherRegistry.containsKey(name)) {
            throw new CappuccinoException(
                    String.format("There is no %s associated with the name %s", CappuccinoResourceWatcher.class.getSimpleName(), name));
        }

        return mResourceWatcherRegistry.get(name);
    }

    /**
     * Convenience method for {@link Espresso#registerIdlingResources(IdlingResource...)}, which first
     * instantiates an {@link CappuccinoIdlingResource}, then registers it with {@code Espresso}.
     *
     * @param object The object from which to generate an {@code CappuccinoIdlingResource}.
     */
    public static void registerIdlingResource(@NonNull Object object) {
        registerIdlingResource(nameOf(object));
    }

    /**
     * Convenience method for {@link Espresso#registerIdlingResources(IdlingResource...)}, which first
     * instantiates an {@link CappuccinoIdlingResource}, then registers it with {@code Espresso}.
     *
     * @param name The name from which to generate an {@code CappuccinoIdlingResource}.
     */
    public static void registerIdlingResource(@NonNull String name) {
        CappuccinoIdlingResource idlingResource = new CappuccinoIdlingResource(name);
        mIdlingResourceRegistry.put(name, idlingResource);
        Espresso.registerIdlingResources(idlingResource);
    }

    /**
     * Convenience method for {@link Espresso#unregisterIdlingResources(IdlingResource...)}, which
     * is the twin of {@link #registerIdlingResource(Object)}.
     *
     * @param object The object associated with the {@link CappuccinoIdlingResource} you wish to
     *               unregister.
     */
    public static void unregisterIdlingResource(@NonNull Object object) {
        unregisterIdlingResource(nameOf(object));
    }

    /**
     * Convenience method for {@link Espresso#unregisterIdlingResources(IdlingResource...)}, which
     * is the twin of {@link #registerIdlingResource(String)}.
     *
     * @param name The name associated with the {@link CappuccinoIdlingResource} you wish to
     *             unregister.
     */
    public static void unregisterIdlingResource(@NonNull String name) {
        CappuccinoIdlingResource idlingResource = mIdlingResourceRegistry.get(name);
        Espresso.unregisterIdlingResources(idlingResource);
        mIdlingResourceRegistry.remove(name);
    }

    /**
     * Returns true if we're testing; false otherwise. If true
     *
     * @return true if we're testing; false otherwise.
     */
    private static boolean isTesting() {
        return mIsTesting;
    }

    /**
     * Set to true if testing / during debug; false in production.
     *
     * @param isTesting True if testing / debug; false for production.
     */
    public static void setIsTesting(boolean isTesting) {
        mIsTesting = isTesting;
    }

    /**
     * Resets {@code Cappuccino}'s internal state, for use in a {@code tearDown()}-type method during testing.
     */
    @VisibleForTesting
    public static void reset() {
        mResourceWatcherRegistry.clear();
        mIdlingResourceRegistry.clear();
        mIsTesting = true;
    }
}