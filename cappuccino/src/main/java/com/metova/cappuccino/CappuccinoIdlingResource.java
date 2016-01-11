package com.metova.cappuccino;

import android.support.annotation.NonNull;
import android.support.test.espresso.IdlingResource;

public class CappuccinoIdlingResource implements IdlingResource {

    private static final String TAG = CappuccinoIdlingResource.class.getSimpleName();

    private final String mName;
    private ResourceCallback mCallback;

    /**
     * Instantiates a new {@code CappuccinoResource}, and associates with a name, or key.
     *
     * @param name The name for this resource.
     */
    public CappuccinoIdlingResource(@NonNull String name) {
        mName = name;
    }

    /**
     * Instantiates a new {@code CappuccinoResource}, and associates with a name, or key,
     * which is derived from the {@code object} supplied.
     *
     * @param object The object from which to get a name for this resource.
     */
    public CappuccinoIdlingResource(@NonNull Object object) {
        mName = Cappuccino.nameOf(object);
    }

    @Override
    public String getName() {
        return TAG + ": " + mName;
    }

    @Override
    public boolean isIdleNow() {
        boolean idle = Cappuccino.getResourceWatcher(mName).isIdle();
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