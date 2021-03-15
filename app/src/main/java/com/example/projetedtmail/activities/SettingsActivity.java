package com.example.projetedtmail.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;
import androidx.preference.SwitchPreferenceCompat;

import com.example.projetedtmail.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.settings_activity);
        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(this);
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
                requestCode, resultCode, data
        );

        // si le résultat est bien différent de null
        if (intentResult.getContents() != null) {

            // on le stock dans les sharedPreferences
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("url", intentResult.getContents());
            editor.apply();

            // on modifie le champs des paramètres pour afficher la nouvelle valeur de l'URL scannée
            SettingsFragment.url.setText(intentResult.getContents());

            // on notifie l'utilisateur que l'URL a bien été scannée à l'aide d'un AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Result");
            builder.setMessage(intentResult.getContents());
            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
            builder.show();
        } else {
            //TODO
            //GERER UNE EVENTUELLE ERREUR
        }
    }

    @Override
    public void onClick(View v) {
        Intent Back = new Intent(this, CalendarViewActivity.class);
        startActivity(Back);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener{

        public static EditTextPreference url;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            Preference scanQrCode = this.findPreference("scan");
            scanQrCode.setOnPreferenceClickListener(this);
            url = this.findPreference("url");

            Preference dark = findPreference("dark_theme");
            dark.setOnPreferenceClickListener(this);

        }

        // gestion du click sur l'onglet Scan QR code des paramètres
        @Override
        public boolean onPreferenceClick(Preference preference) {

            if (preference.getKey().equals("scan")) {


                IntentIntegrator intentIntegrator = new IntentIntegrator((Activity) SettingsActivity.context);
                intentIntegrator.setPrompt("Scannez le QR code");
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setCaptureActivity(ScanActivity.class);
                intentIntegrator.initiateScan();
                return true;
            }
            if (preference.getKey().equals("dark_theme")) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                boolean check = sharedPreferences.getBoolean("dark_theme", false);
                if (check) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean("dark_theme", true);

                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean("dark_theme", false);

                }
                editor.apply();

            }
            return true;
        }
    }
}


