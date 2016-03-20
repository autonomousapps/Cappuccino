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
    public OngoingViewInteraction withId(@IdRes int viewId) {
        mMatchers.add(ViewMatchers.withId(viewId));
        return this;
    }

    public OngoingViewInteraction withText(@NonNull String text) {
        mMatchers.add(ViewMatchers.withText(text));
        return this;
    }

    public OngoingViewInteraction withText(@StringRes int stringId) {
        mMatchers.add(ViewMatchers.withText(stringId));
        return this;
    }


    // endregion OngoingViewInteractions

    // region check

    /**
     * Returns an {@link OngoingViewAssertion} for asserting properties of the matches {@link android.view.View}.
     */
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
     * @return A {@link ViewInteraction}, on which you may call {@link ViewInteraction#check(ViewAssertion)}.
     */
    @SuppressWarnings("unchecked")
    public ViewInteraction toEspresso() {
        return Espresso.onView(Matchers.allOf(mMatchers.toArray(new Matcher[mMatchers.size()])));
    }
    // endregion check
}