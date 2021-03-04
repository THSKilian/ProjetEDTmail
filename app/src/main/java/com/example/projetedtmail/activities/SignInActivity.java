package com.example.projetedtmail.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.projetedtmail.R;
import com.example.projetedtmail.beans.User;
import com.example.projetedtmail.dao.UserDAO;

import java.util.ArrayList;
import java.util.Iterator;


public class SignInActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText username;
    private EditText password;

    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        Button btn_signup_activity = findViewById(R.id.Creation);
        btn_signup_activity.setOnClickListener(this);

        Button btn_signin = findViewById(R.id.Connexion);
        btn_signin.setOnClickListener(this);

        this.username = findViewById(R.id.Username);
        this.password = findViewById(R.id.Password);

        userDAO = new UserDAO(this);
        userDAO.open();
    }
    public void onClick(View btn) {

        // Gestion du clique sur le bouton de création de compte
        if(btn.getId() == R.id.Creation){
            Intent SignUp = new Intent(this, SignUpActivity.class);
            startActivity(SignUp);
            this.finish();
        }

        // Gestion du clique sur le bouton de connexion
        if(btn.getId() == R.id.Connexion) {

            // Récupération des objets user stockés en BdD
            ArrayList<User> userList = userDAO.getAllData();

            // Récupération des valeurs saisies par l'utilisateur
            String usernameText = this.username.getText().toString().trim();
            String passwordText = this.password.getText().toString().trim();
            boolean test = usernameText.isEmpty() || passwordText.isEmpty();
            //Test si les champs sont remplis
            if (test) {
                createDialog("Error", "ATTENTION!! Certain(s) champ(s) sont vides");
            } else if (!test) {

                // Parcours de la liste des objets user et vérification de username et password identiques
                Iterator<User> it = userList.iterator();
                boolean canConnect = false;

                while (it.hasNext() && !canConnect) {
                    User user = it.next();
                    if (user.getUsername().equals(usernameText) && user.getPassword().equals(passwordText))
                        canConnect = true;

                }

                // L'utilisateur peut se connecter : on lance l'activité du calendrier et on ferme l'activité de login
                if (canConnect) {
                    Intent calendarView = new Intent(this, CalendarViewActivity.class);
                    startActivity(calendarView);
                    this.finish();
                }
                // L'utilisateur ne peut pas se connecter : on affiche un message d'erreur
                else {
                    createDialog("Error", "Le Pseudo ou le mot de passe est incorrect");

                }

            }
        }
    }
    //Création d'un AlertDialog
    private void  createDialog(String title, String text){
        AlertDialog ad = new AlertDialog.Builder(this).setPositiveButton("OK",null).setTitle(title).setMessage(text).create();
        ad.show();

    }
}