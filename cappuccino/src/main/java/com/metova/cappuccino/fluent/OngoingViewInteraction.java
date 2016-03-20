package com.metova.cappuccino.fluent;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

public class OngoingViewInteraction {

    private final Set<Matcher<View>> mMatchers = new HashSet<>();

    // region OngoingViewInteractions
    @NonNull
    public OngoingViewInteraction isAssignableFrom(@NonNull Class<? extends View> clazz) {
        mMatchers.add(ViewMatchers.isAssignableFrom(clazz));
        return this;
    }

    @NonNull
    public OngoingViewInteraction withClassName(@NonNull Matcher<String> classNameMatcher) {
        mMatchers.add(ViewMatchers.withClassName(classNameMatcher));
        return this;
    }

    @NonNull // TODO need to avoid duplication between this class and OngoingViewAssertion
    public OngoingViewInteraction isCompletelyDisplayed() {
        mMatchers.add(ViewMatchers.isCompletelyDisplayed());
        return this;
    }

    @NonNull
    public OngoingViewInteraction withId(@IdRes int viewId) {
        mMatchers.add(ViewMatchers.withId(viewId));
        return this;
    }

    @NonNull
    public OngoingViewInteraction withText(@NonNull String text) {
        mMatchers.add(ViewMatchers.withText(text));
        return this;
    }

    @NonNull
    public OngoingViewInteraction withText(@StringRes int stringId) {
        mMatchers.add(ViewMatchers.withText(stringId));
        return this;
    }

    // endregion OngoingViewInteractions

    // region check

    /**
     * Returns an {@link OngoingViewAssertion} for asserting properties of the matched {@link android.view.View}.
     */
    @NonNull
    public OngoingViewAssertion check() {
        return new OngoingViewAssertion(mMatchers);
    }

    /**
     * <p>This method exists to return from the Cappuccino "fluent" idiom to the standard Hamcrest matching
     * idiom used by Espresso.</p>
     *
     * <p>Creates a ViewInteraction for a given view. Note: the view has to be part of the view hierarchy.
     * This may not be the case if it is rendered as part of an AdapterView (e.g. ListView). If this is the
     * case, use Espresso.onData to load the view first.</p>
     *
     * @return A {@link ViewInteraction}, on which you may call {@link ViewInteraction#check(ViewAssertion)},
     * or for further perform/verification calls.
     */
    @SuppressWarnings("unchecked")
    @NonNull
    public ViewInteraction toEspresso() {
        return Espresso.onView(Matchers.allOf(mMatchers.toArray(new Matcher[mMatchers.size()])));
    }

    /**
     * <p>This method exists to return from the Cappuccino "fluent" idiom to the standard Hamcrest matching
     * idiom used by Espresso.</p>
     *
     * <p>Checks the given {@link ViewAssertion} on the the view selected by the current view matcher.</p>
     *
     * @param viewAssertion the assertion to check.
     * @return A {@link ViewInteraction} for further perform/verification calls.
     */
    @SuppressWarnings("unchecked")
    @NonNull
    public ViewInteraction espressoCheck(@NonNull ViewAssertion viewAssertion) {
        return Espresso.onView(Matchers.allOf(mMatchers.toArray(new Matcher[mMatchers.size()]))).check(viewAssertion);
    }
    // endregion check
}