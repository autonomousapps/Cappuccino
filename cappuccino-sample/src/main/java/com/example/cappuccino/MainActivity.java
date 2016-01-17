package com.example.cappuccino;

import com.metova.cappuccino.Cappuccino;
import com.metova.cappuccino.CappuccinoResourceWatcher;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String RESOURCE_MULTIPLE_ACTIVITIES = "resource_mult_activities";

    private CappuccinoResourceWatcher mWatcher;

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.text_hello);

        // Get an idling resource watcher associated with `this`. Internally, Cappuccino
        // translates `this` into a String based on the class name, so don't worry about
        // resource leaks
        mWatcher = Cappuccino.newIdlingResourceWatcher(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initViews();
    }

    private void initViews() {
        // Using the resource watcher, notify Cappuccino that we're now busy
        mWatcher.busy();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mTextView.setVisibility(View.VISIBLE);

                // Again using the resource watcher, notify Cappuccino that we're now idle
                mWatcher.idle();
            }
        }, 500);
    }

    /**
     * For some reason, we expect navigating to a new {@code Activity} to take a while (perhaps a network request
     * is involved). Really what this is demonstrating is that a {@code CappuccinoIdlingResource} can be
     * declared and used across multiple Activities.
     */
    public void onClickSecondActivity(View view) {
        // Declare a new CappuccinoIdlingResource
        Cappuccino.newIdlingResourceWatcher(RESOURCE_MULTIPLE_ACTIVITIES);

        // Tell Cappuccino that the new resource is busy TODO make this more laconic
        Cappuccino.markAsBusy(RESOURCE_MULTIPLE_ACTIVITIES);

        startActivity(new Intent(this, SecondActivity.class));
    }
}