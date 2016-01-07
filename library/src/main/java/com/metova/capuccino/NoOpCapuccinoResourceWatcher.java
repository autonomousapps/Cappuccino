package com.metova.capuccino;

public class NoOpCapuccinoResourceWatcher implements CapuccinoResourceWatcher {

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