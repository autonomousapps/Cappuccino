# Cappuccino
A sweeter Espresso. At present, there are two main features of Cappuccino:
 1. Never write your own IdlingResource again.
 2. Never manually disable system animations again. By using the `SystemAnimations` class of Cappuccino, along with the new [Cappuccino Animations Gradle plugin](https://plugins.gradle.org/plugin/com.autonomousapps.cappuccino-animations), you can automatically disable and re-enable system animations.

## Current version
**Cappuccino Android library:** 0.9.1

**Cappuccino Animations Gradle plugin:** 0.8.3

## Getting Started
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
    
    debugCompile "com.autonomousapps:cappuccino:${latest_lib_version}"
    releaseCompile "com.autonomousapps:cappuccino-no-op:${latest_lib_version}"
}
```

You will also want to declare the following Espresso dependencies, if you haven't already:
```gradle
dependencies {

    // ...other dependencies...

    androidTestCompile 'com.android.support:support-annotations:24.2.1'
    androidTestCompile 'com.android.support.test:runner:0.5'
    androidTestCompile 'com.android.support.test:rules:0.5'
    androidTestCompile 'com.android.support.test.espresso:espresso-core:2.2.2'
}
```

Check [here](https://google.github.io/android-testing-support-library/docs/espresso/setup/) for Espresso setup instructions, and to ensure you're using the latest version.

That's it! (mostly)

## Automatically disable and re-enable system animations
If you want to automatically disable and re-enable system animations, there are (unfortunately, but necessarily) several steps to follow. In summary, they are:
 1. Apply the Cappuccino Animations plugin.
 2. Create or modify a `debug/AndroidManifest.xml` or (`[flavor]Debug/AndroidManifest.xml` for each flavor, if you have them), as below.
 3. Add some code to your test classes, as below.

### Apply the Cappuccino Animations plugin at the top of your `build.gradle`:

```gradle
buildscript {
  repositories {
    maven { url "https://plugins.gradle.org/m2/" }
  }
  dependencies {
    classpath "gradle.plugin.com.autonomousapps:cappuccino-plugin:${latest_plugin_version}"
  }
}

apply plugin: "com.autonomousapps.cappuccino-animations"
```

### Sample `debug/AndroidManifest.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <!-- Disable animations on debug builds so that the animations do not interfere with Espresso
         tests. Adding this permission to the manifest is not sufficient - you must also grant the
         permission over adb! -->
    <uses-permission android:name="android.permission.SET_ANIMATION_SCALE" />

</manifest>
```

### Sample test class
If you have all of your test classes inherit from this class, then you only need to do the following once.

```java
import com.autonomousapps.cappuccino.animations.SystemAnimations;

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

# License
> Copyright 2018 AutonomousApps
> 
> Licensed under the Apache License, Version 2.0 (the "License");
> you may not use this file except in compliance with the License.
> You may obtain a copy of the License at
> 
>  http://www.apache.org/licenses/LICENSE-2.0
> 
> Unless required by applicable law or agreed to in writing, software
> distributed under the License is distributed on an "AS IS" BASIS,
> WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
> See the License for the specific language governing permissions and
> limitations under the License.
