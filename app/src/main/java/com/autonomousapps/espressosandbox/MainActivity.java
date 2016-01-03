package com.autonomousapps.espressosandbox;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.autonomousapps.espressosandbox.Americano.Americano;
import com.autonomousapps.espressosandbox.Americano.AmericanoResourceWatcher;

public class MainActivity extends AppCompatActivity {

    private TextView mTextHello;

    private AmericanoResourceWatcher mAmericanoResourceWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAmericanoResourceWatcher = Americano.newIdlingResourceWatcher(this);

        mTextHello = (TextView) findViewById(R.id.text_hello);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initViews();
    }

    private void initViews() {
        mAmericanoResourceWatcher.busy();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mTextHello.setVisibility(View.VISIBLE);
                mAmericanoResourceWatcher.idle();
            }
        }, 500 /* delay in ms */);
    }
}