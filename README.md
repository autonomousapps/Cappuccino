# Cappuccino
A sweeter Espresso. At present, there are two main features of Cappuccino:
 1. Never write your own IdlingResource again.
 2. By using the `SystemAnimations` class, along with some code snippets provided below, you can automatically disable and re-enable system animations--never again worry about running an Espresso test without manually disabling animations.

##Current version
0.6.0

##Getting Started
In your `build.gradle`:
```gradle
repositories {
    // Either repository will work
    mavenCentral()
    jcenter()
}
```

```gradle
dependencies {
    
    // ...other dependencies...
    
    debugCompile('com.metova:cappuccino:0.6.0') {
        transitive = false
    }
    releaseCompile 'com.metova:cappuccino-no-op:0.6.0'
}
```

You will also want to declare the following Espresso dependencies, if you haven't already:
```gradle
dependencies {

    // ...other dependencies...

    androidTestCompile 'com.android.support:support-annotations:23.1.1'
    androidTestCompile 'com.android.support.test:runner:0.4.1'
    androidTestCompile 'com.android.support.test:rules:0.4.1'
    androidTestCompile 'com.android.support.test.espresso:espresso-core:2.2.1'
}
```

Check [here](https://google.github.io/android-testing-support-library/docs/espresso/setup/) for Espresso setup instructions, and to ensure you're using the latest version.

That's it! (mostly)

## Automatically disable and re-enable system animations
For automatic disabling and re-enabling system animations, there are (unfortunately) several steps to follow at this time. In summary, they are:
 1. Create or modify a `debug/AndroidManifest.xml` or (`[flavor]Debug/AndroidManifest.xml` for each flavor, if you have them), as below
 2. Modify your app's `build.gradle` file, as below
 3. Add [this file](https://github.com/metova/Cappuccino/blob/master/cappuccino-sample/gradle/grant-animations-permission.gradle) to a (probably new) directory `app/gradle`
 4. Add some code to your test classes, as below

### Sample `debug/AndroidManifest.xml`
```
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <!-- Disable animations on debug builds so that the animations do not interfere with Espresso
             tests. Adding this permission to the manifest is not sufficient - you must also grant the
             permission over adb! -->
    <uses-permission android:name="android.permission.SET_ANIMATION_SCALE" />

</manifest>
```

### Addition to `app/build.gradle` file
```gradle
apply from: project.file("gradle/grant-animations-permission.gradle")
```

### Sample test class
If you have all of your test classes inherit from this class, then you only need to do the following once.

```java
import com.metova.cappuccino.SystemAnimations;

public class BaseTestClass {
    @BeforeClass
    public static void disableAnimations() {
        SystemAnimations.disableAll(InstrumentationRegistry.getContext());
    }

    @AfterClass
    public static void enableAnimations() {
        SystemAnimations.enableAll(InstrumentationRegistry.getContext());
    }
    
    // ...test code...
}
```

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

    // optional
    @BeforeClass
    public static void disableAnimations() {
        SystemAnimations.disableAll(InstrumentationRegistry.getContext());
    }

    // optional
    @AfterClass
    public static void enableAnimations() {
        SystemAnimations.enableAll(InstrumentationRegistry.getContext());
    }

    @Before
    public void setUp() throws Exception {
        mActivityTestRule.launchActivity(new Intent());
    }

    @After
    public void tearDown() throws Exception {
        Cappuccino.reset();
    }

    @Rule
    private ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class, true, false);

    /**
     * This test uses the common Espresso idiom for instantiating, registering, and unregistering
     * custom IdlingResources, but uses our CappuccinoIdlingResource, instead.
     */
    @Test
    public void testCappuccinoIdlingResource() throws Exception {
        CappuccinoIdlingResource idlingResource = new CappuccinoIdlingResource(mActivityTestRule.getActivity());
        Espresso.registerIdlingResources(idlingResource);
        
        onView(withId(R.id.text_hello)).check(matches(isDisplayed()));

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

Please check out the `cappuccino-sample` module for more sample usages!
