package com.metova.capuccino;

import android.support.annotation.NonNull;
import android.support.test.espresso.IdlingResource;

public class CapuccinoIdlingResource implements IdlingResource {

    private static final String TAG = CapuccinoIdlingResource.class.getSimpleName();

    private final String mName;
    private ResourceCallback mCallback;

    /**
     * Instantiates a new {@code CapuccinoResource}, and associates with a name, or key.
     *
     * @param name The name for this resource.
     */
    public CapuccinoIdlingResource(@NonNull String name) {
        mName = name;
    }

    /**
     * Instantiates a new {@code CapuccinoResource}, and associates with a name, or key,
     * which is derived from the {@code object} supplied.
     *
     * @param object The object from which to get a name for this resource.
     */
    public CapuccinoIdlingResource(@NonNull Object object) {
        mName = Capuccino.nameOf(object);
    }

    @Override
    public String getName() {
        return TAG + ": " + mName;
    }

    @Override
    public boolean isIdleNow() {
        boolean idle = Capuccino.getResourceWatcher(mName).isIdle();
        if (idle) {
            mCallback.onTransitionToIdle();
        }
        return idle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mCallback = callback;
    }
}