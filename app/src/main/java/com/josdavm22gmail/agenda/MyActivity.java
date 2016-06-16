package com.josdavm22gmail.agenda;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;


public class MyActivity extends Activity {

    EditText et1;
    EditText et2;
    DatePicker datePicker;
    Calendar calendar;
    EditText dateView;
    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);
        if (isFirstRun) {
            //show start activity
            startActivity(new Intent(MyActivity.this, Preferencias.class));
            Toast.makeText(MyActivity.this, "First Run", Toast.LENGTH_LONG).show();
        }
        getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .edit().putBoolean("isFirstRun", false).commit();
        setContentView(R.layout.activity_my);

        dateView = (EditText) findViewById(R.id.editText);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }


    public void guardar(View view){
        String name = dateView.getText().toString().replace('/','-') + ".txt";
        String contenido = et2.getText().toString();
        try {
            if(optenerStado() == false){
                Toast.makeText(this, "Entre a guardar el archivo en el dispositivo", Toast.LENGTH_LONG).show();
                OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput(
                        name, Activity.MODE_PRIVATE));
                archivo.write(contenido);
                archivo.flush();
                archivo.close();
            }
            else{
                File tarjeta = Environment.getExternalStorageDirectory();
                File file = new File(tarjeta.getAbsolutePath(), name);
                OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file));
                osw.write(contenido);
                osw.flush();
                osw.close();
                Toast.makeText(this, "Los datos fueron grabados correctamente en el SD", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {

        }
    }

    private Boolean optenerStado(){
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
        return pref.getBoolean("almacenamiento",false);
    }

    public void recuperar(View view){
        String[] archivos = fileList();
        String name = dateView.getText().toString().replace('/','-') + ".txt";
        try{
            File tarjeta = Environment.getExternalStorageDirectory();
            File file = new File(tarjeta.getAbsolutePath(), name);
            if(optenerStado() == false){
                if(existe(archivos, name)) {
                    Toast.makeText(this, "Entre a cargar el archivo desde el dispositivo", Toast.LENGTH_LONG).show();
                    InputStreamReader archivo = new InputStreamReader(
                            openFileInput(name));
                    BufferedReader br = new BufferedReader(archivo);
                    String linea = br.readLine();
                    String todo = "";
                    while (linea != null) {
                        todo = todo + linea + "\n";
                        linea = br.readLine();
                    }
                    br.close();
                    archivo.close();
                    et2.setText(todo);
                }
            }
            else{
                Toast.makeText(this, "Entre a cargar el archivo desde SD", Toast.LENGTH_LONG).show();
                FileInputStream fin = new FileInputStream(file);
                InputStreamReader archivo = new InputStreamReader(fin);
                BufferedReader br = new BufferedReader(archivo);
                String linea = br.readLine();
                String todo = "";
                while (linea != null){
                    todo = todo + linea + "";
                    linea = br.readLine();
                }
                br.close();
                archivo.close();
            }
        } catch (IOException e){

        }
    }

    private boolean existe(String[] archivos, String arcbusca){
        for (int f = 0; f < archivos.length; f++)
            if (arcbusca.equals(archivos[f]))
                return true;
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        et2 = (EditText) findViewById(R.id.texto);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            lanzarPreferencias(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void lanzarPreferencias(View view){
        Intent int2 = new Intent(this, Preferencias.class);
        startActivity(int2);
    }
}
