package com.metova.cappuccino.fluent;

public class CappuccinoInteraction {

    private static OngoingViewInteraction sOngoingViewInteraction = null;

    public static OngoingViewInteraction onView() {
        sOngoingViewInteraction = new OngoingFinder();
        return sOngoingViewInteraction;
    }
}