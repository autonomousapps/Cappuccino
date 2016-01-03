package com.autonomousapps.espressosandbox.Americano;

import android.support.annotation.NonNull;
import android.support.test.espresso.IdlingResource;

/**
 * Created by Tony on 1/1/2016.
 */
public class AmericanoIdlingResource implements IdlingResource {

    private static final String TAG = AmericanoIdlingResource.class.getSimpleName();

    private final String mName;
    private ResourceCallback mCallback;

    /**
     * Instantiates a new {@code AmericanoResource}, and associates with a name, or key.
     *
     * @param name The name for this resource.
     */
    public AmericanoIdlingResource(@NonNull String name) {
        mName = name;
    }

    /**
     * Instantiates a new {@code AmericanoResource}, and associates with a name, or key,
     * which is derived from the {@code object} supplied.
     *
     * @param object The object from which to get a name for this resource.
     */
    public AmericanoIdlingResource(@NonNull Object object) {
        mName = Americano.nameOf(object);
    }

    @Override
    public String getName() {
        return TAG + ": " + mName;
    }

    @Override
    public boolean isIdleNow() {
        boolean idle = Americano.getResourceWatcher(mName).isIdle();
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