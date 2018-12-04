package com.example.rumyantsevok.thirdpractice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstActivity extends AppCompatActivity {

    private Button startServiceBtn, startSecondActivityBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        init();
        setClickListeners();
    }

    private void setClickListeners() {
        startServiceBtn.setOnClickListener(new startServiceClickListener());
        startSecondActivityBtn.setOnClickListener(new startSecActivityListener());
    }

    private void init() {
        startServiceBtn = findViewById(R.id.startServiceBtn);
        startSecondActivityBtn = findViewById(R.id.startSecondActivityBtn);
    }

    private class startServiceClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = MyIntentService.newIntent(FirstActivity.this);
            startService(intent);
        }
    }

    private class startSecActivityListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = SecondActivity.newIntent(FirstActivity.this);
            startActivity(intent);
        }
    }
}
