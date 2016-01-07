package com.metova.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.autonomousapps.espressosandbox.R;
import com.metova.capuccino.Capuccino;
import com.metova.capuccino.CapuccinoResourceWatcher;

public class MainActivity extends AppCompatActivity {

    private TextView mTextHello;

    private CapuccinoResourceWatcher mCapuccinoResourceWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCapuccinoResourceWatcher = Capuccino.newIdlingResourceWatcher(this);

        mTextHello = (TextView) findViewById(R.id.text_hello);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initViews();
    }

    private void initViews() {
        mCapuccinoResourceWatcher.busy();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mTextHello.setVisibility(View.VISIBLE);
                mCapuccinoResourceWatcher.idle();
            }
        }, 500 /* delay in ms */);
    }
}