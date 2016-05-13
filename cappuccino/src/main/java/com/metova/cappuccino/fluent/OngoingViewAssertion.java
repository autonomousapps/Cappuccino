package com.metova.cappuccino.fluent;

import android.support.annotation.NonNull;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.assertion.ViewAssertions;

public class OngoingViewAssertion extends OngoingViewInteraction {

    private final ViewInteraction mViewInteraction;

    protected OngoingViewAssertion(@NonNull ViewInteraction viewInteraction) {
        mViewInteraction = viewInteraction;
    }

    private ViewInteraction viewInteraction() {
        return mViewInteraction;
    }

    @Override
    protected OngoingViewInteraction doTheThing() {
        viewInteraction().check(ViewAssertions.matches(getLatestMatcher()));
        clearMatchers();
        return this;
    }

    // TODO this check would normally return a ViewInteraction, but do I want/need to permit that?
    public void doesNotExist() {
        viewInteraction().check(ViewAssertions.doesNotExist());
    }
}