package com.example.projetedtmail.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.projetedtmail.R;


public class SignInActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        Button btn = findViewById(R.id.Creation);
        btn.setOnClickListener(this);
    }
    public void onClick(View btn) {
        if(btn.getId() == R.id.Creation){
            Intent SignUp = new Intent(this, SignUpActivity.class);
            startActivity(SignUp);
        }

    }
}