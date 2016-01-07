package com.autonomousapps.espressosandbox.capuccino;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CapuccinoTest {

    private static final String ANY_NAME = "any_key";

    @Before
    public void setUp() throws Exception {
        Capuccino.reset();
    }

    @Test
    public void testNewIdlingResourceWatcherOperatingForTesting() throws Exception {
        Capuccino.setIsTesting(true);
        CapuccinoResourceWatcher watcher = Capuccino.newIdlingResourceWatcher(ANY_NAME);

        assertThat(watcher, instanceOf(OperatingCapuccinoResourceWatcher.class));
    }

    @Test
    public void testNewIdlingResourceWatcherNoOpForProduction() throws Exception {
        Capuccino.setIsTesting(false);
        CapuccinoResourceWatcher watcher = Capuccino.newIdlingResourceWatcher(ANY_NAME);

        assertThat(watcher, instanceOf(NoOpCapuccinoResourceWatcher.class));
    }

    @Test
    public void testNameOf() throws Exception {
        assertThat(Capuccino.nameOf(new Object()), is(Object.class.getCanonicalName())); // "java.lang.Object"
    }

    @Test
    public void testGetResourceWatcher() throws Exception {
        Capuccino.setIsTesting(true);
        CapuccinoResourceWatcher watcher = Capuccino.newIdlingResourceWatcher(ANY_NAME);

        assertThat(watcher, is(Capuccino.getResourceWatcher(ANY_NAME)));
    }
}