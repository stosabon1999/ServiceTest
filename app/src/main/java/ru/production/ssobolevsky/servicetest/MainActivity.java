package ru.production.ssobolevsky.servicetest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mButtonOne;

    private Button mButtonTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initListeners();
    }

    private void init() {
        mButtonOne = findViewById(R.id.button_one);
        mButtonTwo = findViewById(R.id.button_two);
    }

    private void initListeners() {
        mButtonOne.setOnClickListener(new OneButtonClickListener());
        mButtonTwo.setOnClickListener(new TwoButtonClickListener());
    }

    private class OneButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            startService(MyService.newIntent(MainActivity.this));
        }
    }

    private class TwoButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            startActivity(SecondActivity.newIntent(MainActivity.this));
        }
    }
}
