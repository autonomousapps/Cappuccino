package com.autonomousapps.espressosandbox.Americano;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Tony on 1/2/2016.
 */
public class AmericanoTest {

    private static final String ANY_NAME = "any_key";

    @Before
    public void setUp() throws Exception {
        Americano.reset();
    }

    @Test
    public void testNewIdlingResourceWatcherOperatingForTesting() throws Exception {
        Americano.setIsTesting(true);
        AmericanoResourceWatcher watcher = Americano.newIdlingResourceWatcher(ANY_NAME);

        assertThat(watcher, instanceOf(OperatingAmericanoResourceWatcher.class));
    }

    @Test
    public void testNewIdlingResourceWatcherNoOpForProduction() throws Exception {
        Americano.setIsTesting(false);
        AmericanoResourceWatcher watcher = Americano.newIdlingResourceWatcher(ANY_NAME);

        assertThat(watcher, instanceOf(NoOpAmericanoResourceWatcher.class));
    }

    @Test
    public void testNameOf() throws Exception {
        assertThat(Americano.nameOf(new Object()), is(Object.class.getCanonicalName())); // "java.lang.Object"
    }

    @Test
    public void testGetResourceWatcher() throws Exception {
        Americano.setIsTesting(true);
        AmericanoResourceWatcher watcher = Americano.newIdlingResourceWatcher(ANY_NAME);

        assertThat(watcher, is(Americano.getResourceWatcher(ANY_NAME)));
    }
}