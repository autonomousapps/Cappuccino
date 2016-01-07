package com.metova.capuccino;

public class OperatingCapuccinoResourceWatcher implements CapuccinoResourceWatcher {

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