package com.metova.cappuccino.fluent;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

public class OngoingInteraction {

    private static OngoingInteraction sCurrentInstance;

    private boolean mIsNot = false;

    // Espresso ViewInteraction
    private ViewInteraction mViewInteraction;

    private final Set<Matcher<View>> mMatchers = new HashSet<>();

    // Main entry point
    public static OngoingInteraction onView() {
//        sCurrentInstance = new OngoingFinder();
        sCurrentInstance = new OngoingInteraction();
        return sCurrentInstance;
    }

    private OngoingInteraction() {
    }

    // Main entry point into ViewAssertions
    public OngoingInteraction check() {
        ViewInteraction interaction = Espresso.onView(Matchers.allOf(matchersAsArray()));
        setViewInteraction(interaction);
        clearMatchers(); // reset for next phase
        return this;
    }

    private OngoingInteraction doCheck() {
        getViewInteraction().check(ViewAssertions.matches(Matchers.allOf(matchersAsArray())));
        clearMatchers();
        return this;
    }

    protected void setViewInteraction(@NonNull ViewInteraction viewInteraction) {
        mViewInteraction = viewInteraction;
    }

    @NonNull
    protected ViewInteraction getViewInteraction() {
        return mViewInteraction;
    }

    protected void clearMatchers() {
        mMatchers.clear();
    }


    @NonNull
    @SuppressWarnings("unchecked")
    private Matcher<View>[] matchersAsArray() {
        return mMatchers.toArray(new Matcher[mMatchers.size()]);
    }

    // region ViewMatchers
    @NonNull
    private Matcher<View> compose(@NonNull Matcher<View> matcher) {
        Matcher<View> result = mIsNot ? Matchers.not(matcher) : matcher;
        mIsNot = false; // consume the `not()`, if it existed
        return result;
    }

    public OngoingInteraction doesNotExist() {
        // not valid if there are any matchers or if we're not in one of the CHECKING states
//        if (mIsNegated || mMatchers.size() > 0 || mState == State.FINDING || mState == State.PERFORMING) {
//            throw new IllegalStateException("Cannot make this assertion in conjunction with any other assertion");
//        }
        getViewInteraction().check(ViewAssertions.doesNotExist());
        return this;
    }

    @NonNull
    public OngoingInteraction not() {
        mIsNot = true;
        return this;
    }

    /**
     * For use with custom View Matchers.
     *
     * @param customViewMatcher the custom view matcher.
     * @return An OngoingInteraction for continuing to check against.
     */
    public OngoingInteraction is(Matcher<View> customViewMatcher) {
        mMatchers.add(compose(customViewMatcher));
        return doCheck();
    }

    @NonNull
    public OngoingInteraction isAssignableFrom(@NonNull Class<? extends View> clazz) {
        mMatchers.add(compose(ViewMatchers.isAssignableFrom(clazz)));
        return doCheck();
    }

    @NonNull
    public OngoingInteraction withClassName(@NonNull Matcher<String> classNameMatcher) {
        mMatchers.add(compose(ViewMatchers.withClassName(classNameMatcher)));
        return doCheck();
    }

    @NonNull
    public OngoingInteraction isDisplayed() {
        mMatchers.add(compose(ViewMatchers.isDisplayed()));
        return doCheck();
    }

    @NonNull
    public OngoingInteraction isCompletelyDisplayed() {
        mMatchers.add(compose(ViewMatchers.isCompletelyDisplayed()));
        return doCheck();
    }

    @NonNull
    public OngoingInteraction withId(@IdRes int viewId) {
        mMatchers.add(compose(ViewMatchers.withId(viewId)));
        return doCheck();
    }

    @NonNull
    public OngoingInteraction withText(@NonNull String text) {
        mMatchers.add(compose(ViewMatchers.withText(text)));
        return doCheck();
    }

    @NonNull
    public OngoingInteraction withText(@StringRes int stringId) {
        mMatchers.add(compose(ViewMatchers.withText(stringId)));
        return doCheck();
    }

    @NonNull
    public OngoingInteraction isClickable() {
        mMatchers.add(compose(ViewMatchers.isClickable()));
        return doCheck();
    }

    @NonNull
    public OngoingInteraction isFocusable() {
        mMatchers.add(compose(ViewMatchers.isFocusable()));
        return doCheck();
    }
    // endregion ViewMatchers
}