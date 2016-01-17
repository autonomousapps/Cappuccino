package com.metova.cappuccino;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CappuccinoTest {

    private static final String ANY_NAME = "any_key";

    @Before
    public void setUp() throws Exception {
        Cappuccino.setIsTesting(true); // TODO I'm going to remove this method
    }

    @After
    public void tearDown() throws Exception {
        Cappuccino.reset();
    }

    @Test
    public void testNewIdlingResourceWatcherOperatingForTesting() throws Exception {
        CappuccinoResourceWatcher watcher = Cappuccino.newIdlingResourceWatcher(ANY_NAME);

        assertThat(watcher, instanceOf(OperatingCappuccinoResourceWatcher.class));
    }

    @Test
    public void testNewIdlingResourceWatcherNoOpForProduction() throws Exception {
        // TODO remove this test
        Cappuccino.setIsTesting(false);
        CappuccinoResourceWatcher watcher = Cappuccino.newIdlingResourceWatcher(ANY_NAME);

        assertThat(watcher, instanceOf(NoOpCappuccinoResourceWatcher.class));
    }

    @Test
    public void testNameOf() throws Exception {
        assertThat(Cappuccino.nameOf(new Object()), is(Object.class.getCanonicalName())); // "java.lang.Object"
    }

    @Test
    public void testGetResourceWatcher() throws Exception {
        CappuccinoResourceWatcher watcher = Cappuccino.newIdlingResourceWatcher(ANY_NAME);

        assertThat(watcher, is(Cappuccino.getResourceWatcher(ANY_NAME)));
    }

    @Test
    public void testMarkAsBusy() throws Exception {
        // This test is doing double-duty, testing both the Object and String versions of the method
        CappuccinoResourceWatcher watcher1 = Cappuccino.newIdlingResourceWatcher(ANY_NAME);
        CappuccinoResourceWatcher watcher2 = Cappuccino.newIdlingResourceWatcher(new Object());

        Cappuccino.markAsBusy(ANY_NAME);
        Cappuccino.markAsBusy(new Object());

        assertThat(watcher1.isIdle(), is(false));
        assertThat(watcher2.isIdle(), is(false));
    }

    @Test
    public void testMarkAsIdle() throws Exception {
        // This test is doing double-duty, testing both the Object and String versions of the method
        CappuccinoResourceWatcher watcher1 = Cappuccino.newIdlingResourceWatcher(ANY_NAME);
        CappuccinoResourceWatcher watcher2 = Cappuccino.newIdlingResourceWatcher(new Object());

        Cappuccino.markAsIdle(ANY_NAME);
        Cappuccino.markAsIdle(new Object());

        assertThat(watcher1.isIdle(), is(true));
        assertThat(watcher2.isIdle(), is(true));
    }

    @Test(expected = CappuccinoException.class)
    public void testNoResourceWatcherThrowsException() throws Exception {
        Cappuccino.getResourceWatcher(new Object());
    }

    @Test(expected = CappuccinoException.class)
    public void testRegisteringWithoutResourceWatcherThrowsException() throws Exception {
        Cappuccino.registerIdlingResource(new Object());
    }

    @Test(expected = CappuccinoException.class)
    public void testUnregisteringWithoutResourceWatcherThrowsException() throws Exception {
        Cappuccino.unregisterIdlingResource(new Object());
    }
}