package com.metova.cappuccino;

public class NoOpCappuccinoResourceWatcher implements CappuccinoResourceWatcher {

    @Override
    public void busy() {
        // no-op
    }

    @Override
    public void idle() {
        // no-op
    }

    @Override
    public boolean isIdle() {
        return true;
    }
}