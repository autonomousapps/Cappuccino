package com.autonomousapps.espressosandbox.Americano;

/**
 * Created by Tony on 1/2/2016.
 */
public class OperatingAmericanoResourceWatcher implements AmericanoResourceWatcher {

    private boolean mIsIdle = true;

    @Override
    public void busy() {
        mIsIdle = false;
    }

    @Override
    public void idle() {
        mIsIdle = true;
    }

    @Override
    public boolean isIdle() {
        return mIsIdle;
    }
}