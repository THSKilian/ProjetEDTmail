package com.example.projetedtmail.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceManager;

import com.example.projetedtmail.R;
import com.example.projetedtmail.classes.EventAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;

import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.component.VEvent;

import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Comparator;
import java.util.Locale;
import java.util.TimeZone;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CalendarViewActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView listView;
    private TextView textView;
    private FloatingActionButton fab;

    private Boolean returnButton;

    public static Context context;

    private java.util.Calendar day;
    private String save_day;

    private Calendar calendar;
    private SimpleDateFormat formatHeureMinute;
    private SimpleDateFormat formatDate;

    /*
    TODO :
        - faire en sorte de pouvoir sync avec un appuie sur le bouton Synchroniser de la barre d'outils JAJA
        - faire en sorte de paramétrer l'activité à l'aide des sharedPreferences
        - faire la gestion des paramètres (appuie et changement des valeurs) JAJA
        - faire le mode paysage
        - utiliser un service pour accomplir un truc
        - utiliser des thèmes et des styles JAJA
        - faire en sorte que l'application s'addapte à toutes les tailles d'écran
        - finir internationnalisation
        - mettre en place les modes connecté et non connecté pour la navigation
        - mettre en place le changement de mot de passe et d'identifiant / gérer les mdp oubliés
        - vérifier si les permissions sont demandées partout
        - faire en sorte que chaque utilisateur puisse avoir son propre calendrier
    */

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);
        ImageButton setting = findViewById(R.id.setting);
        setting.setOnClickListener(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean check = sharedPreferences.getBoolean("dark_theme", false);
        if (check) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            editor.putBoolean("dark_theme", true);

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            editor.putBoolean("dark_theme", false);

        }


        // récupération des Views de l'affichage
        listView = findViewById(R.id.listView);
        textView = findViewById(R.id.date);

        // récupération des boutons de défilement de la date
        ImageButton btn_left = findViewById(R.id.imageButton_left);
        ImageButton btn_right = findViewById(R.id.imageButton_right);
        btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);

        // récupération du bouton retour
        fab = findViewById(R.id.button_today);
        fab.setOnClickListener(this);
        fab.setVisibility(View.INVISIBLE);

        // permet de lancer le téléchargement depuis le thread principal
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // récupératation des paramètres stockés dans les sharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean sync_edt = sharedPreferences.getBoolean("sync_edt",false);
        String langue = sharedPreferences.getString("language", "fr");
        String url = sharedPreferences.getString("url",null);
        returnButton = sharedPreferences.getBoolean("return_button", false);

        // si la synchro au démarrage est activée
        if(sync_edt){
            // réucpérer le fichier ICS depuis l'url scannée ou saisie
            downloadCalendar(url);
        }

        switch (langue){
            case "fr":
                formatHeureMinute = new SimpleDateFormat("HH:mm", Locale.FRANCE);
                formatDate = new SimpleDateFormat("EEEE dd MMMM", Locale.FRANCE);
                break;
            case "en":
                formatHeureMinute = new SimpleDateFormat("HH:mm", Locale.UK);
                formatDate = new SimpleDateFormat("EEEE dd MMMM", Locale.UK);
                break;
            default:
                break;
        }

        // initialisation de la date du jour
        day = java.util.Calendar.getInstance();
        day.add(java.util.Calendar.HOUR, 1);

        save_day = formatDate.format(day.getTime());

        // récupérer le fichier calendar
        calendar = this.getCalendar();

        // récupération de la date de jour
        String date_du_jour = formatDate.format(day.getTime());

        // affichage de la date du jour dans le layout
        textView.setText(date_du_jour);

        // si le calendrier existe, on l'affiche
        if(calendar != null){

            // récupération de la liste des événement du jour
            ArrayList<VEvent> eventList = this.getDayEvents(date_du_jour, calendar);

            // création de l'adapter permettant de générer l'affichage des activités
            EventAdapter eventAdapter = new EventAdapter(listView.getContext(), eventList);

            // afficher le contenu du celendar
            listView.setAdapter(eventAdapter);
        }
        else{
            // TODO: gérer le cas où l'application démarre sans fichier *.ics
        }

    }

    // TODO: Peut éventuellement être intégré dans un service
    // méthode de synchro de l'EDT :
    // Télécharge le fichier *.ics depuis l'URL fournie à chaque appel
    public void downloadCalendar(String _url){
        // récupération du fichier ICS
        File calendar = this.getIcsFile();
        try {
            Response response = new OkHttpClient().newCall(new Request.Builder().url(_url).build()).execute();
            if(response.isSuccessful() && response.body() != null){
                String fileContent = response.body().string();
                PrintStream printStream = new PrintStream(calendar);
                printStream.println(fileContent);
                printStream.close();
            }
            else{
                // TODO: gérer l'erreur
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // métode de récupération du fichier *.ics
    public File getIcsFile(){
        String path = Environment.getExternalStorageDirectory().getPath() +
                "/Download" +
                "/ADEcal.ics";
        return new File(path);
    }

    // métode de récupération du fichier calendar
    public Calendar getCalendar() {
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(this.getIcsFile());
            CalendarBuilder builder = new CalendarBuilder();
            return builder.build(fin);
        } catch (IOException | ParserException e) {
            e.printStackTrace();
            return null;
        }
    }

    // méthode de récupération des activités d'un jour donnée sous forme d'une String respectant le format "EEEE dd MMMM"
    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<VEvent> getDayEvents(String date, Calendar calendar){
        // récupération de la liste des activités
        ComponentList componentList = calendar.getComponents();
        ArrayList<VEvent> eventList = new ArrayList<>();

        // ajout des événements de la date du jour dans eventList
        for(Object component : componentList){
            VEvent event = (VEvent) component;
            if(date.equals(formatDate.format(event.getStartDate().getDate()))){
                eventList.add(event);
            }
        }

        // trie de la liste par ordre chronologique
        eventList.sort(new Comparator<VEvent>() {
            @Override
            public int compare(VEvent e1, VEvent e2) {
                Date date1 = e1.getStartDate().getDate();
                Date date2 = e2.getStartDate().getDate();
                if(date1.getTime() < date2.getTime()) return -1;
                else return 1;
            }
        });

        return eventList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.setting) {
            Intent Setting = new Intent(this, SettingsActivity.class);
            startActivity(Setting);
            if (v.getId() == R.id.refresh) {

            }
        }
        // click sur le bouton de gauche
        if(v.getId() == R.id.imageButton_left){

            // incrémenter la date du jour
            day.add(java.util.Calendar.HOUR, -24);

            if(returnButton){
                if(save_day.equals(formatDate.format(day.getTime()))){
                    fab.setVisibility(View.INVISIBLE);
                }
                else{
                    fab.setVisibility(View.VISIBLE);
                }
            }

            // afficher la nouvelle date
            textView.setText(formatDate.format(day.getTime()));

            // récupérer les activité du nouveu jour
            ArrayList<VEvent> eventList = this.getDayEvents(formatDate.format(day.getTime()), calendar);

            // création de l'adapter permettant de générer l'affichage des activités
            EventAdapter eventAdapter = new EventAdapter(listView.getContext(), eventList);

            // rafraichir les activités
            listView.setAdapter(eventAdapter);
        }

        // click sur le bouton de droite
        if(v.getId() == R.id.imageButton_right){

            // décrémenter la date du jour
            day.add(java.util.Calendar.HOUR, 24);

            if(returnButton){
                if(save_day.equals(formatDate.format(day.getTime()))){
                    fab.setVisibility(View.INVISIBLE);
                }
                else{
                    fab.setVisibility(View.VISIBLE);
                }
            }

            // afficher la nouvelle date
            textView.setText(formatDate.format(day.getTime()));

            // récupérer les activité du nouveu jour
            ArrayList<VEvent> eventList = this.getDayEvents(formatDate.format(day.getTime()), calendar);

            // création de l'adapter permettant de générer l'affichage des activités
            EventAdapter eventAdapter = new EventAdapter(listView.getContext(), eventList);

            // rafraichir les activités
            listView.setAdapter(eventAdapter);
        }

        // click sur le bouton retour
        if(v.getId() == R.id.button_today){

            if(returnButton) fab.setVisibility(View.INVISIBLE);

            day = java.util.Calendar.getInstance();
            day.add(java.util.Calendar.HOUR, 1);

            textView.setText(formatDate.format(day.getTime()));

            // récupérer les activité du nouveu jour
            ArrayList<VEvent> eventList = this.getDayEvents(formatDate.format(day.getTime()), calendar);

            // création de l'adapter permettant de générer l'affichage des activités
            EventAdapter eventAdapter = new EventAdapter(listView.getContext(), eventList);

            // rafraichir les activités
            listView.setAdapter(eventAdapter);
        }

}