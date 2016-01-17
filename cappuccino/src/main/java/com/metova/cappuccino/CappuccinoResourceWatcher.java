package com.metova.cappuccino;

public class CappuccinoResourceWatcher {

    private boolean mIsIdle = true;

    /**
     * Sets the internal state of the resource to busy, or not idle.
     */
    public void busy() {
        mIsIdle = false;
    }

    /**
     * Sets the internal state of the resource to idle; that is, not busy.
     */
    public void idle() {
        mIsIdle = true;
    }

    /**
     * Returns the busy/idle ({@code false}/{@code true}) state of the resource.
     *
     * @return the busy/idle ({@code false}/{@code true}) state of the resource.
     */
    public boolean isIdle() {
        return mIsIdle;
    }
}