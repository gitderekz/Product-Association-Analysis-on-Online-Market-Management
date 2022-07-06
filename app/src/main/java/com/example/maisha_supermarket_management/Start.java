package com.example.maisha_supermarket_management;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class Start extends AppCompatActivity {
    private static int WAIT_TIME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Objects.requireNonNull(getSupportActionBar()).hide();
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);

        boolean b = new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent login = new Intent(Start.this, Login.class);
                startActivity(login);
                finish();
            }
        }, WAIT_TIME);
    }
}