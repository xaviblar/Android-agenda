package com.josdavm22gmail.agenda;


import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Josué on 17/08/2015.
 */
public class Preferencias extends PreferenceActivity {
    @SuppressWarnings("deprecation")
    @Override protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
    }
}