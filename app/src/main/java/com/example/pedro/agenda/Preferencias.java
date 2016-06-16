package com.example.pedro.agenda;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Pedro on 18/08/2015.
 */
public class Preferencias extends PreferenceActivity {
    @SuppressWarnings("deprecation")
    @Override protected void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
        addPreferencesFromResource(R.xml.preferencias);
    }
}
