package com.autonomousapps.espressosandbox.Americano;

/**
 * Created by Tony on 1/2/2016.
 */
public class NoOpAmericanoResourceWatcher implements AmericanoResourceWatcher {

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