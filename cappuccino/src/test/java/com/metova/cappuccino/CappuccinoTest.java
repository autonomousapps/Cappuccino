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

    }

    @After
    public void tearDown() throws Exception {
        Cappuccino.reset();
    }

    @Test
    public void testNewIdlingResourceWatcherOperatingForTesting() throws Exception {
        Cappuccino.setIsTesting(true);
        CappuccinoResourceWatcher watcher = Cappuccino.newIdlingResourceWatcher(ANY_NAME);

        assertThat(watcher, instanceOf(OperatingCappuccinoResourceWatcher.class));
    }

    @Test
    public void testNewIdlingResourceWatcherNoOpForProduction() throws Exception {
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
        Cappuccino.setIsTesting(true);
        CappuccinoResourceWatcher watcher = Cappuccino.newIdlingResourceWatcher(ANY_NAME);

        assertThat(watcher, is(Cappuccino.getResourceWatcher(ANY_NAME)));
    }

    @Test(expected = CappuccinoException.class)
    public void testNoResourceWatcherThrowsException() throws Exception {
        Cappuccino.setIsTesting(true);
        Cappuccino.getResourceWatcher(new Object());

    }
}