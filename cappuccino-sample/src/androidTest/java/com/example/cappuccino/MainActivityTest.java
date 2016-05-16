package com.example.cappuccino;

import com.metova.cappuccino.Cappuccino;
import com.metova.cappuccino.CappuccinoIdlingResource;
import com.metova.cappuccino.SystemAnimations;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class, true, false);

    @BeforeClass
    public static void setupTest() {
        SystemAnimations.disableAll(InstrumentationRegistry.getContext());
    }

    @AfterClass
    public static void teardownTest() {
        SystemAnimations.enableAll(InstrumentationRegistry.getContext());
//        SystemAnimations.restoreAll(InstrumentationRegistry.getContext());
    }

    @Before
    public void setUp() throws Exception {
        mActivityTestRule.launchActivity(new Intent());
    }

    @After
    public void tearDown() throws Exception {
        Cappuccino.reset();
    }

    @Test
    public void testEspressoWay() throws Exception {
        // Instantiate and register the IdlingResource
        CappuccinoIdlingResource idlingResource = new CappuccinoIdlingResource(mActivityTestRule.getActivity());
        Espresso.registerIdlingResources(idlingResource);

        // This view animates in
        onView(withId(R.id.text_hello)).check(matches(isDisplayed()));

        // Unregister the IdlingResource
        Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    public void testCappuccinoWay() throws Exception {
        // Register the IdlingResource
        Cappuccino.registerIdlingResource(mActivityTestRule.getActivity());

        // This view animates in
        onView(withId(R.id.text_hello)).check(matches(isDisplayed()));

        // Unregister the IdlingResource
        Cappuccino.unregisterIdlingResource(mActivityTestRule.getActivity());
    }

    @Test
    public void testMultipleActivities() throws Exception {
        // Tapping this will take us to the second activity
        onView(withId(R.id.button_second_activity)).perform(click());

        // Register the IdlingResource
        Cappuccino.registerIdlingResource(MainActivity.RESOURCE_MULTIPLE_ACTIVITIES);

        // This view animates in
        onView(withId(R.id.text_second)).check(matches(isDisplayed()));

        // Unregister the IdlingResource
        Cappuccino.unregisterIdlingResource(MainActivity.RESOURCE_MULTIPLE_ACTIVITIES);
    }
}