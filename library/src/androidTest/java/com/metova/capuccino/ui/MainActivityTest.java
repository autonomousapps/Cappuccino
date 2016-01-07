package com.metova.capuccino.ui;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import com.autonomousapps.espressosandbox.R;
import com.metova.capuccino.Capuccino;
import com.metova.capuccino.CapuccinoIdlingResource;
import com.metova.ui.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class MainActivityTest {

    @Before
    public void setUp() throws Exception {
        mActivityTestRule.launchActivity(new Intent());
    }

    @After
    public void tearDown() throws Exception {
        Capuccino.reset();
    }

    private ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class, true, false);

    @Test
    public void testCapuccinoIdlingResource() throws Exception {
        CapuccinoIdlingResource idlingResource = new CapuccinoIdlingResource(mActivityTestRule.getActivity());
        Espresso.registerIdlingResources(idlingResource);

        onView(ViewMatchers.withId(R.id.text_hello)).check(matches(isDisplayed()));

        Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    public void testCapuccinoIdlingResource2() throws Exception {
        Capuccino.registerIdlingResource(mActivityTestRule.getActivity());

        onView(withId(R.id.text_hello)).check(matches(isDisplayed()));

        Capuccino.unregisterIdlingResource(mActivityTestRule.getActivity());
    }
}