package com.autonomousapps.espressosandbox.Americano;

import android.support.annotation.NonNull;
import android.support.test.espresso.IdlingResource;

/**
 * Created by Tony on 1/1/2016.
 */
public class AmericanoResource implements IdlingResource {

    private static final String TAG = AmericanoResource.class.getSimpleName();

    private final String mName;
    private ResourceCallback mCallback;

    public AmericanoResource(@NonNull String name) {
        mName = name;
    }

    public AmericanoResource(@NonNull Object resource) {
        mName = Americano.nameOf(resource);
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