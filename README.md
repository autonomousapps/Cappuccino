# Cappuccino
A sweeter Espresso. Cappuccino makes it easy to add custom IdlingResources.

##Current version
0.5.0

##Getting Started
In your `build.gradle`:
```gradle
debugCompile('com.metova:cappuccino:0.5.0') {
    transitive false
}
releaseCompile 'com.metova:cappuccino-no-op:0.5.0'
```

You will also want to declare the following Espresso dependencies, if you haven't already:
```gradle
androidTestCompile 'com.android.support:support-annotations:23.1.1'
androidTestCompile 'com.android.support.test:runner:0.4.1'
androidTestCompile 'com.android.support.test:rules:0.4.1'
androidTestCompile 'com.android.support.test.espresso:espresso-core:2.2.1'
```

Check [here](https://google.github.io/android-testing-support-library/docs/espresso/setup/) for Espresso setup instructions, and to ensure you're using the latest version.

That's it!

## Sample usage

```java
public class MainActivity extends AppCompatActivity {

    private TextView mTextHello;

    // Declare your CappuccinoResourceWatcher
    private CappuccinoResourceWatcher mCappuccinoResourceWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            
        // Get a reference to a CappuccinoResourceWatcher. This also registers it with Cappuccino
        // It will return a no-op version for release builds.
        // We pass in 'this' as a convenience. Internally, Cappuccino converts 'this' into a String
        mCappuccinoResourceWatcher = Cappuccino.newIdlingResourceWatcher(this);

        mTextHello = (TextView) findViewById(R.id.text_hello);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initViews();
    }

    private void initViews() {
        // This tells Cappuccino that the resoure associated with this Activity is busy, 
        // and instrumentation tests should wait...
        mCappuccinoResourceWatcher.busy();
        
        // This is our custom animation, that Espresso knows nothing about, but
        // Cappuccino does!
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mTextHello.setVisibility(View.VISIBLE);
                    
                // ...and this tells Cappuccino that the Activity is now idle, and instrumentation tests
                // may now proceed
                mCappuccinoResourceWatcher.idle();
            }
        }, 500 /* delay in ms */);
    }
}
```
    
...and in a test...

```java
public class MainActivityTest {

    @Before
    public void setUp() throws Exception {
        mActivityTestRule.launchActivity(new Intent());
    }

    @After
    public void tearDown() throws Exception {
        Cappuccino.reset();
    }

    private ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class, true, false);

    /**
     * This test uses the common Espresso idiom for instantiating, registering, and unregistering
     * custom IdlingResources, but uses our CappuccinoIdlingResource, instead.
     */
    @Test
    public void testCappuccinoIdlingResource() throws Exception {
        CappuccinoIdlingResource idlingResource = new CappuccinoIdlingResource(mActivityTestRule.getActivity());
        Espresso.registerIdlingResources(idlingResource);
        
        onView(ViewMatchers.withId(R.id.text_hello)).check(matches(isDisplayed()));

        Espresso.unregisterIdlingResources(idlingResource);
    }

    /**
     * As a convenience, Cappuccino permits the following idiom, which keeps track of 
     * CappuccinoIdlingResources in an internal registry, keyed by the name of the object passed in.
     * In this case, this is our MainActivity, retrieved by calling {@code mActivityTestRule.getActivity()}.
     */
    @Test
    public void testCappuccinoIdlingResource2() throws Exception {
        Cappuccino.registerIdlingResource(mActivityTestRule.getActivity());
        
        onView(withId(R.id.text_hello)).check(matches(isDisplayed()));

        Cappuccino.unregisterIdlingResource(mActivityTestRule.getActivity());
    }
}
```

Please check on the `cappuccino-sample` module for more sample usages!
