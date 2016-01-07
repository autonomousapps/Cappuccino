package com.metova.cappuccino;

public interface CappuccinoResourceWatcher {

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