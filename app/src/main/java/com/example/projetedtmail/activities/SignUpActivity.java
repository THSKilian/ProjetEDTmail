package com.example.projetedtmail.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetedtmail.R;
import com.example.projetedtmail.activities.SignInActivity;
import com.example.projetedtmail.beans.User;
import com.example.projetedtmail.dao.UserDAO;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText username;
    private EditText firstname;
    private EditText lastname;
    private EditText password;

    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Button register_btn = findViewById(R.id.Inscription);
        register_btn.setOnClickListener(this);

        Button connection_btn = findViewById(R.id.Connexion);
        connection_btn.setOnClickListener(this);

        this.username = findViewById(R.id.Username);
        this.firstname = findViewById(R.id.Firstname);
        this.lastname = findViewById(R.id.Lastname);
        this.password = findViewById(R.id.Password);

        userDAO = new UserDAO(this);
        userDAO.open();
    }

    public void onClick(View btn) {

        // Gestion du bouton d'inscription
        if(btn.getId() == R.id.Inscription){
            String usernameText = username.getText().toString().trim();
            String firstnameText = firstname.getText().toString().trim();
            String lastnameText = lastname.getText().toString().trim();
            String passwordText = password.getText().toString().trim();
            boolean test = usernameText.isEmpty() || firstnameText.isEmpty() || lastnameText.isEmpty() || passwordText.isEmpty();

            if(!test){
                try {
                    // Création d'un objet User permettant de sauvegarder les saisies du formulaire
                    User user = new User(usernameText,firstnameText,lastnameText,passwordText);

                    // Ajout en BDD des saisies du formulaire via le BEANS User créer précédemment
                    userDAO.ajouter(user);

                    // Remise à zéro des champs du formulaire
                    username.setText(null);
                    firstname.setText(null);
                    lastname.setText(null);
                    password.setText(null);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        if(btn.getId() == R.id.Connexion){
            Intent signIn = new Intent(this, SignInActivity.class);
            startActivity(signIn);
            this.finish();
        }
    }

}