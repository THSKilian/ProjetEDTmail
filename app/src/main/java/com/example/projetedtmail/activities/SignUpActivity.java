package com.example.projetedtmail.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetedtmail.R;
import com.example.projetedtmail.activities.SignInActivity;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Button btn = findViewById(R.id.Connexion);
        btn.setOnClickListener(this);
    }

    public void onClick(View btn) {
        if (btn.getId() == R.id.Connexion) {
            Intent SignIn= new Intent(this, SignInActivity.class);
            startActivity(SignIn);
        }


    }
}