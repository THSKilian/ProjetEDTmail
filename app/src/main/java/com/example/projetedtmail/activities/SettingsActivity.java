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

    public static Context context;
    public static EditTextPreference url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
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
        IntentResult intentResult = IntentIntegrator.parseActivityResult(
                requestCode,resultCode,data
        );
        if(intentResult.getContents() != null){

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("url", intentResult.getContents());
            editor.apply();

            url.setText(intentResult.getContents());

            if(sharedPreferences.contains("url")){
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Result");
                builder.setMessage(intentResult.getContents());
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        }
        else{
            //TODO
            //GERER UNE EVENTUELLE ERREUR
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            Preference scanQrCode = this.findPreference("scan");
            scanQrCode.setOnPreferenceClickListener(this);

            url = this.findPreference("url");

        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
           IntentIntegrator intentIntegrator = new IntentIntegrator((Activity) context);
           intentIntegrator.setPrompt("Scannez le QR code");
           intentIntegrator.setBeepEnabled(false);
           intentIntegrator.setOrientationLocked(false);
           intentIntegrator.setCaptureActivity(ScanActivity.class);
           intentIntegrator.initiateScan();
            return true;
        }
    }

}