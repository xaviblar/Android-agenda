package com.example.pedro.agenda;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    EditText tFecha;
    EditText et1;
    final Calendar calendar = Calendar.getInstance();
    int yy = calendar.get(Calendar.YEAR);
    int mm = calendar.get(Calendar.MONTH);
    int dd = calendar.get(Calendar.DAY_OF_MONTH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et1=(EditText)findViewById(R.id.tArea);
        tFecha = (EditText)findViewById(R.id.tFecha);
        populateSetDate(yy, mm, dd);
        verPreferecia();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            lanzarPreferencias(null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void lanzarPreferencias(View view){
        Intent int2= new Intent(this, Preferencias.class);
        startActivity(int2);
    }

    public String verPreferecia(){
        SharedPreferences pref=
                PreferenceManager.getDefaultSharedPreferences(this);
        String s= ""+pref.getString("tipo","0");
        if((s.equals("0"))){
            lanzarPreferencias(null);
            return s;
        }
        return s;
    }

    @SuppressLint("ValidFragment")
    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm+1, dd);
        }
    }
    public void populateSetDate(int year, int month, int day) {
        tFecha.setText(day + "/" + month + "/" + year);
    }

    private boolean existe(String[] archivo, String archBusca){
        for (int f =0;f<archivo.length;f++)
            return  true;
        return false;
    }
    public void selectDate(View view) {
        DialogFragment newFragment = new SelectDateFragment();
        newFragment.show(getFragmentManager(), "DatePicker");
    }

    public void onGuardar(View view){
        boolean ver=true;
        if (verPreferecia().equals("1")){
            ver=false;
        }
        escritura(ver);
    }
    public void onRecuperar(View view){
        boolean ver=true;
        if (verPreferecia().equals("1")){
            ver=false;
        }
        lectura(ver);
    }
    public void escritura(boolean modo){
        String nombre=tFecha.getText().toString();
        nombre=nombre.replace('/','-');
        nombre=nombre+".txt";
        if (modo){
            try{
                OutputStreamWriter archivo=new OutputStreamWriter(openFileOutput(
                        nombre, Activity.MODE_PRIVATE));
                archivo.write(et1.getText().toString());
                archivo.flush();
                archivo.close();
                Toast.makeText(this, "Los datos fueron grabados correctamente en el celular",Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try{
                File tarjeta= Environment.getExternalStorageDirectory();
                File file = new File(tarjeta.getAbsolutePath(),nombre);
                OutputStreamWriter osw=new OutputStreamWriter(new FileOutputStream(file));
                osw.write(et1.getText().toString());
                osw.flush();
                osw.close();
                Toast.makeText(this,"Los datos fueron grabados correctamente en la SD",
                        Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void lectura(boolean modo){
        String nombre=tFecha.getText().toString();
        nombre=nombre.replace('/', '-');
        nombre=nombre+".txt";
        et1.setText("");
        if (modo){
            String[] archivos= fileList();
            if(existe(archivos,nombre)){
                try{
                    InputStreamReader archivo=new InputStreamReader(
                            openFileInput(nombre));
                    BufferedReader br = new BufferedReader(archivo);
                    String linea=br.readLine();
                    String todo="";
                    while (linea!=null){
                        todo=todo+linea+"\n";
                        linea=br.readLine();
                    }
                    br.close();
                    archivo.close();
                    et1.setText(todo);

                }catch (IOException e){
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(this, "No existe el archivo en el celular ",
                        Toast.LENGTH_SHORT).show();
            }
        }else {
            File tarjeta= Environment.getExternalStorageDirectory();
            File file= new File(tarjeta.getAbsolutePath(),nombre);
            try{
                FileInputStream fIn= new FileInputStream(file);
                InputStreamReader archivo = new InputStreamReader(fIn);
                BufferedReader br= new BufferedReader(archivo);
                String linea = br.readLine();
                String todo ="";
                while (linea!= null){
                    todo=todo+linea+"";
                    linea=br.readLine();
                }
                br.close();
                archivo.close();
                et1.setText(todo);

            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }
}
