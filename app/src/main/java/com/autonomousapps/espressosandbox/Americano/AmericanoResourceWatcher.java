package com.autonomousapps.espressosandbox.Americano;

/**
 * Created by Tony on 1/2/2016.
 */
public interface AmericanoResourceWatcher {

    /**
     * Sets the internal state of the resource to busy, or not idle.
     */
    void busy();

    /**
     * Sets the internal state of the resource to idle; that is, not busy.
     */
    void idle();

    /**
     * Returns the busy/idle ({@code false}/{@code true}) state of the resource.
     *
     * @return the busy/idle ({@code false}/{@code true}) state of the resource.
     */
    boolean isIdle();
}