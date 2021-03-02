package com.example.projetedtmail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Button btn = findViewById(R.id.Connexion);
        btn.setOnClickListener(this);
    }

    public void onClick(View btn) {
        if (btn.getId() == R.id.Connexion) {
            Intent SignIn= new Intent(this, SignIn.class);
            startActivity(SignIn);
        }


    }
}