package com.metova.cappuccino.fluent;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;

import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;

public abstract class OngoingViewInteraction {

    protected final Deque<Matcher<View>> mMatchers = new LinkedBlockingDeque<>();
    protected boolean mIsNegated = false;

    // package-private
    OngoingViewInteraction() {

    }

    protected abstract OngoingViewInteraction doTheThing();

    @NonNull
    @SuppressWarnings("unchecked")
    protected Matcher<View>[] matchersAsArray() {
        return mMatchers.toArray(new Matcher[mMatchers.size()]);
    }

    @NonNull
    protected Matcher<View> getLatestMatcher() {
        return mMatchers.getLast();
    }

    protected void clearMatchers() {
        mMatchers.clear();
    }

    @NonNull
    private Matcher<View> compose(@NonNull Matcher<View> matcher) {
        Matcher<View> result = mIsNegated ? Matchers.not(matcher) : matcher;
        mIsNegated = false; // consume the `not()`, if it existed
        return result;
    }

    @NonNull
    public OngoingViewInteraction not() {
        mIsNegated = true;
        return this; // TODO tsr: doTheThing()?
    }

    @NonNull
    public OngoingViewInteraction is(@NonNull Matcher<View> customMatcher) {
        mMatchers.add(compose(customMatcher));
        return doTheThing();
    }

    // region wrappers around ViewMatchers
    @NonNull
    public OngoingViewInteraction isAssignableFrom(@NonNull Class<? extends View> clazz) {
        mMatchers.add(compose(ViewMatchers.isAssignableFrom(clazz)));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction withClassName(@NonNull Matcher<String> classNameMatcher) {
        mMatchers.add(compose(ViewMatchers.withClassName(classNameMatcher)));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction isDisplayed() {
        mMatchers.add(compose(ViewMatchers.isDisplayed()));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction isCompletelyDisplayed() {
        mMatchers.add(compose(ViewMatchers.isCompletelyDisplayed()));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction isDisplayingAtLeast(int areaPercentage) {
        mMatchers.add(compose(ViewMatchers.isDisplayingAtLeast(areaPercentage)));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction isEnabled() {
        mMatchers.add(compose(ViewMatchers.isEnabled()));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction isFocusable() {
        mMatchers.add(compose(ViewMatchers.isFocusable()));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction hasFocus() {
        mMatchers.add(compose(ViewMatchers.hasFocus()));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction isSelected() {
        mMatchers.add(compose(ViewMatchers.isSelected()));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction hasSibling(@NonNull Matcher<View> siblingMatcher) {
        mMatchers.add(compose(ViewMatchers.hasSibling(siblingMatcher)));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction withContentDescription(@IdRes int resourceId) {
        mMatchers.add(compose(ViewMatchers.withContentDescription(resourceId)));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction withContentDescription(@NonNull String text) {
        mMatchers.add(compose(ViewMatchers.withContentDescription(text)));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction withContentDescription(@NonNull Matcher<? extends CharSequence> charSequenceMatcher) {
        mMatchers.add(compose(ViewMatchers.withContentDescription(charSequenceMatcher)));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction withId(@IdRes int viewId) {
        mMatchers.add(compose(ViewMatchers.withId(viewId)));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction withId(@NonNull Matcher<Integer> integerMatcher) {
        mMatchers.add(compose(ViewMatchers.withId(integerMatcher)));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction withTagKey(int key) {
        mMatchers.add(compose(ViewMatchers.withTagKey(key)));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction withTagKey(int key, @NonNull Matcher<Object> objectMatcher) {
        mMatchers.add(compose(ViewMatchers.withTagKey(key, objectMatcher)));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction withTagValue(@NonNull Matcher<Object> tagValueMatcher) {
        mMatchers.add(compose(ViewMatchers.withTagValue(tagValueMatcher)));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction withText(@NonNull String text) {
        mMatchers.add(compose(ViewMatchers.withText(text)));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction withText(@NonNull Matcher<String> stringMatcher) {
        mMatchers.add(compose(ViewMatchers.withText(stringMatcher)));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction withText(@StringRes int stringId) {
        mMatchers.add(compose(ViewMatchers.withText(stringId)));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction withHint(@NonNull String hint) {
        mMatchers.add(compose(ViewMatchers.withHint(hint)));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction withHint(@NonNull Matcher<String> stringMatcher) {
        mMatchers.add(compose(ViewMatchers.withHint(stringMatcher)));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction withHint(@IdRes int hintId) {
        mMatchers.add(compose(ViewMatchers.withHint(hintId)));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction isChecked() {
        mMatchers.add(compose(ViewMatchers.isChecked()));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction isNotChecked() {
        mMatchers.add(compose(ViewMatchers.isNotChecked()));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction hasContentDescription() {
        mMatchers.add(compose(ViewMatchers.hasContentDescription()));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction hasDescendant(@NonNull Matcher<View> descendantMatcher) {
        mMatchers.add(compose(ViewMatchers.hasDescendant(descendantMatcher)));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction isClickable() {
        mMatchers.add(compose(ViewMatchers.isClickable()));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction isDescendantOfA(@NonNull Matcher<View> ancestorMatcher) {
        mMatchers.add(compose(ViewMatchers.isDescendantOfA(ancestorMatcher)));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction withEffectiveVisibility(ViewMatchers.Visibility visibility) {
        mMatchers.add(compose(ViewMatchers.withEffectiveVisibility(visibility)));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction withParent(@NonNull Matcher<View> parentMatcher) {
        mMatchers.add(compose(ViewMatchers.withParent(parentMatcher)));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction withChild(@NonNull Matcher<View> childMatcher) {
        mMatchers.add(compose(ViewMatchers.withChild(childMatcher)));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction isRoot() {
        mMatchers.add(compose(ViewMatchers.isRoot()));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction supportsInputMethods() {
        mMatchers.add(compose(ViewMatchers.supportsInputMethods()));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction hasImeAction(int imeAction) {
        mMatchers.add(compose(ViewMatchers.hasImeAction(imeAction)));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction hasImeAction(@NonNull Matcher<Integer> imeActionMatcher) {
        mMatchers.add(compose(ViewMatchers.hasImeAction(imeActionMatcher)));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction hasLinks() {
        mMatchers.add(compose(ViewMatchers.hasLinks()));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction withSpinnerText(@IdRes int spinnerTextId) {
        mMatchers.add(compose(ViewMatchers.withSpinnerText(spinnerTextId)));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction withSpinnerText(@NonNull Matcher<String> spinnerTextMatcher) {
        mMatchers.add(compose(ViewMatchers.withSpinnerText(spinnerTextMatcher)));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction withSpinnerText(@NonNull String text) {
        mMatchers.add(compose(ViewMatchers.withSpinnerText(text)));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction isJavascriptEnabled() {
        mMatchers.add(compose(ViewMatchers.isJavascriptEnabled()));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction hasErrorText(@NonNull Matcher<String> errorTextMatcher) {
        mMatchers.add(compose(ViewMatchers.hasErrorText(errorTextMatcher)));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction hasErrorText(@NonNull String expectedError) {
        mMatchers.add(compose(ViewMatchers.hasErrorText(expectedError)));
        return doTheThing();
    }

    @NonNull
    public OngoingViewInteraction withInputType(int inputType) {
        mMatchers.add(compose(ViewMatchers.withInputType(inputType)));
        return doTheThing();
    }
    // region wrappers around ViewMatchers

    /**
     * TODO
     */
    @NonNull
    public OngoingViewAssertion check() {
        ViewInteraction viewInteraction = Espresso.onView(Matchers.allOf(matchersAsArray()));
        return new OngoingViewAssertion(viewInteraction);
    }

    @NonNull
    public OngoingViewPerformer perform() {
        // TODO
        return new OngoingViewPerformer();
    }
}