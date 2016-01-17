package com.example.cappuccino;

import com.metova.cappuccino.Cappuccino;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        mTextView = (TextView) findViewById(R.id.text_second);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initViews();
    }

    private void initViews() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mTextView.setVisibility(View.VISIBLE);

                // Again using the resource watcher, notify Cappuccino that we're now idle
                Cappuccino.markAsIdle(MainActivity.RESOURCE_MULTIPLE_ACTIVITIES);
            }
        }, 500);
    }
}
