package com.metova.cappuccino;

public class OperatingCappuccinoResourceWatcher implements CappuccinoResourceWatcher {

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