package com.metova.cappuccino.fluent;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import android.support.annotation.NonNull;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;

import java.util.Set;

public class OngoingViewAssertion {

    private final Set<Matcher<View>> mMatchers;
    private Matcher<View>[] mMatchersArray = null;
    private ViewInteraction mViewInteraction = null;

    private boolean mIsNegated = false;

    public OngoingViewAssertion(@NonNull Set<Matcher<View>> matchers) {
        mMatchers = matchers;
    }

    private Matcher<View>[] matchers() {
        if (mMatchersArray != null) {
            return mMatchersArray;
        }
        //noinspection unchecked
        mMatchersArray = mMatchers.toArray(new Matcher[mMatchers.size()]);
        return mMatchersArray;
    }

    private ViewInteraction viewInteraction() {
        if (mViewInteraction != null) {
            return mViewInteraction;
        }
        mViewInteraction = Espresso.onView(Matchers.allOf(matchers()));
        return mViewInteraction;
    }

    public OngoingViewAssertion not() {
        mIsNegated = true;
        return this;
    }

    private Matcher<View> compose(Matcher<View> matcher) {
        Matcher<View> result = mIsNegated ? Matchers.not(matcher) : matcher;
        mIsNegated = false; // consume the `not()`, if it existed
        return result;
    }

    public OngoingViewAssertion doesNotExist() {
        viewInteraction().check(ViewAssertions.doesNotExist());
        return this;
    }

    public OngoingViewAssertion isDisplayed() {
        viewInteraction().check(ViewAssertions.matches(compose(ViewMatchers.isDisplayed())));
        return this;
    }

    public OngoingViewAssertion isClickable() {
        viewInteraction().check(ViewAssertions.matches(compose(ViewMatchers.isClickable())));
        return this;
    }

    public OngoingViewAssertion isFocusable() {
        viewInteraction().check(ViewAssertions.matches(compose(ViewMatchers.isFocusable())));
        return this;
    }
}