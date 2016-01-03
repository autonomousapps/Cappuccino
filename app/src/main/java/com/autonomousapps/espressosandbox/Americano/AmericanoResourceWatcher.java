package com.autonomousapps.espressosandbox.Americano;

/**
 * Created by Tony on 1/2/2016.
 */
public interface AmericanoResourceWatcher {

    void busy();

    void idle();

    boolean isIdle();
}