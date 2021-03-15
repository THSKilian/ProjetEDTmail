package com.example.projetedtmail.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.preference.PreferenceManager;

import com.example.projetedtmail.R;
import com.example.projetedtmail.activities.CalendarViewActivity;

import net.fortuna.ical4j.model.component.VEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class EventAdapter extends ArrayAdapter<VEvent> {

    private final SimpleDateFormat formatHeureMinute = new SimpleDateFormat("HH:mm", Locale.FRANCE);

    public EventAdapter(Context context, ArrayList<VEvent> events) {
        super(context, 0, events);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VEvent event = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.calendar_event_view, parent, false);
        }

        // récupération des champs de la view
        LinearLayout background = convertView.findViewById(R.id.calendar_event_view);
        TextView start_hour = convertView.findViewById(R.id.start_hour_textView);
        TextView end_hour = convertView.findViewById(R.id.end_hour_textView);
        TextView location = convertView.findViewById(R.id.location_textView);
        TextView title = convertView.findViewById(R.id.event_title_textView);

        // insertion des données dans les champs
        title.setText(event.getSummary().getValue());
        location.setText(event.getLocation().getValue());
        start_hour.setText(formatHeureMinute.format(event.getStartDate().getDate()));
        end_hour.setText(formatHeureMinute.format(event.getEndDate().getDate()));

        // gestion de la couleur personnalisée des événements
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(CalendarViewActivity.context);
        boolean usePersonalizedColor = sharedPreferences.getBoolean("custom_color", false);
        if(usePersonalizedColor){
            String personalizedColor = sharedPreferences.getString("color", "#DC143C");
            background.setBackgroundColor(Color.parseColor(personalizedColor));
            title.setBackgroundColor(manipulateColor(Color.parseColor(personalizedColor), (float) 0.8));
        }

        return convertView;
    }

    // méthode permettant de manipuler une couleur donnée pour l'éclaircir(facteur > 1) ou la foncer (facteur<1)
    public static int manipulateColor(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r,255),
                Math.min(g,255),
                Math.min(b,255));
    }

}
