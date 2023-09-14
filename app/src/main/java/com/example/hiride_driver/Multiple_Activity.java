package com.example.hiride_driver;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

public class Multiple_Activity extends AppCompatActivity {

    Handler h;
    Runnable r;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_);
        FirebaseApp.initializeApp(this);
        r=new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(Multiple_Activity.this,LoginQRActivity.class);
                startActivity(intent);
                
                finish();
            }
        };
        h=new Handler();
        h.postDelayed(r,2000);

    }
}
