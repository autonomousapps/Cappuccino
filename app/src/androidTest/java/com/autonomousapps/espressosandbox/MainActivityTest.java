package com.autonomousapps.espressosandbox;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;

import com.autonomousapps.espressosandbox.Americano.Americano;
import com.autonomousapps.espressosandbox.Americano.AmericanoIdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Tony on 1/1/2016.
 */
public class MainActivityTest {

    @Before
    public void setUp() throws Exception {
        mActivityTestRule.launchActivity(new Intent());
    }

    @After
    public void tearDown() throws Exception {
        Americano.reset();
    }

    private ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class, true, false);

    @Test
    public void testAmericanoIdlingResource() throws Exception {
        AmericanoIdlingResource idlingResource = new AmericanoIdlingResource(mActivityTestRule.getActivity());
        Espresso.registerIdlingResources(idlingResource);

        onView(withId(R.id.text_hello)).check(matches(isDisplayed()));

        Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    public void testAmericanoIdlingResource2() throws Exception {
        Americano.registerIdlingResource(mActivityTestRule.getActivity());

        onView(withId(R.id.text_hello)).check(matches(isDisplayed()));

        Americano.unregisterIdlingResource(mActivityTestRule.getActivity());
    }
}