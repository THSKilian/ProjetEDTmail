package com.example.projetedtmail.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.projetedtmail.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Collections;

public class SettingsActivity extends AppCompatActivity {

    public EditTextPreference url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // récupération de la valeur du résultat du scan
        IntentResult intentResult = IntentIntegrator.parseActivityResult(
                requestCode,resultCode,data
        );

        // si le résultat est bien différent de null
        if(intentResult.getContents() != null){

            // on le stock dans les sharedPreferences
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("url", intentResult.getContents());
            editor.apply();

            // on modifie le champs des paramètres pour afficher la nouvelle valeur de l'URL scannée
            url.setText(intentResult.getContents());

            // on notifie l'utilisateur que l'URL a bien été scannée à l'aide d'un AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
            builder.setTitle("Result");
            builder.setMessage(intentResult.getContents());
            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
            builder.show();
        }
        else{
            //TODO
            //GERER UNE EVENTUELLE ERREUR
        }
    }

    public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            Preference scanQrCode = this.findPreference("scan");
            scanQrCode.setOnPreferenceClickListener(this);

            url = this.findPreference("url");
        }

        // gestion du click sur l'onglet Scan QR code des paramètres
        @Override
        public boolean onPreferenceClick(Preference preference) {
            IntentIntegrator intentIntegrator = new IntentIntegrator(SettingsActivity.this);
            intentIntegrator.setPrompt("Scannez le QR code");
            intentIntegrator.setBeepEnabled(false);
            intentIntegrator.setOrientationLocked(false);
            intentIntegrator.setCaptureActivity(ScanActivity.class);
            intentIntegrator.initiateScan();
            return true;
        }
    }

}