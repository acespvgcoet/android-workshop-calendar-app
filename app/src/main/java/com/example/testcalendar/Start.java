package com.example.testcalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

public class Start extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // Progress bar is shown on UI
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        // Check whether user is already logged in or not
        // If yes then directly show main page otherwise show login page
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null)   {
            Intent intent = new Intent(Start.this, MainActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(Start.this, Login.class);
            startActivity(intent);
        }

    }
}