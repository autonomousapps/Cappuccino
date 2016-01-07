# Capuccino
A sweeter Espresso. Capuccino makes it easy to add custom IdlingResources.

Sample usage

    public class MainActivity extends AppCompatActivity {

        private TextView mTextHello;

        // Declare your CapuccinoResourceWatcher
        private CapuccinoResourceWatcher mCapuccinoResourceWatcher;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            
            // Get a reference to a CapuccinoResourceWatcher. This also registers it with Capuccino
            // It will return a no-op version for release builds.
            // We pass in 'this' as a convenience. Internally, Capuccino converts 'this' into a String
            mCapuccinoResourceWatcher = Capuccino.newIdlingResourceWatcher(this);

            mTextHello = (TextView) findViewById(R.id.text_hello);
        }

        @Override
        protected void onResume() {
            super.onResume();
            initViews();
        }

        private void initViews() {
            // This tells Capuccino that the resoure associated with this Activity is busy, 
            // and instrumentation tests should wait...
            mCapuccinoResourceWatcher.busy();
            
            // This is our custom animation, that Espresso knows nothing about, but
            // Capuccino does!
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mTextHello.setVisibility(View.VISIBLE);
                    
                    // ...and this tells Capuccino that the Activity is now idle, and instrumentation tests
                    // may not proceed
                    mCapuccinoResourceWatcher.idle();
                }
            }, 500 /* delay in ms */);
        }
    }
    
...and in a test...

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

        /**
         * This test uses the common Espresso idiom for instantiating, registering, and unregistering
         * custom IdlingResources, but uses our CapuccinoIdlingResource, instead.
         */
        @Test
        public void testCapuccinoIdlingResource() throws Exception {
            CapuccinoIdlingResource idlingResource = new CapuccinoIdlingResource(mActivityTestRule.getActivity());
            Espresso.registerIdlingResources(idlingResource);

            onView(ViewMatchers.withId(R.id.text_hello)).check(matches(isDisplayed()));

            Espresso.unregisterIdlingResources(idlingResource);
        }

        /**
         * As a convenience, Capuccino permits the following idiom, which keeps track of 
         * CapuccinoIdlingResources in an internal registry, keyed by the name of the object passed in.
         * In this case, this is our MainActivity, retrieved by calling {@code mActivityTestRule.getActivity()}.
         * custom IdlingResources, but uses our CapuccinoIdlingResource, instead.
         */
        @Test
        public void testCapuccinoIdlingResource2() throws Exception {
            Capuccino.registerIdlingResource(mActivityTestRule.getActivity());

            onView(withId(R.id.text_hello)).check(matches(isDisplayed()));

            Capuccino.unregisterIdlingResource(mActivityTestRule.getActivity());
        }
    }
    
