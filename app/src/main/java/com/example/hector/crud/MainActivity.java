package com.example.hector.crud;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;


public class MainActivity extends Activity implements SearchView.OnQueryTextListener {
    private SearchView mSearchView;//////////////////////
    private ListView LstOpciones;
    adaptadorCoches adaptador;
    private ArrayList<Coches> datos = new ArrayList<Coches>();
    int posi,x;
//pruebaError/pruebaeNERO
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adaptador = new adaptadorCoches(this, datos);
        LstOpciones = (ListView) findViewById(R.id.LstOpciones);
        LstOpciones.setAdapter(adaptador);
        //enables filtering for the contents of the given ListView
        LstOpciones.setTextFilterEnabled(true);
        registerForContextMenu(LstOpciones);
        mSearchView = (SearchView) findViewById(R.id.searchView1);////////////////////
        mSearchView.setBackgroundColor(Color.LTGRAY);
        LstOpciones.setTextFilterEnabled(true);////////////////
        setupSearchView();///////////////////
        LstOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Intent data = new Intent(MainActivity.this, editActivity.class);
                TextView matricula=(TextView) v.findViewById(R.id.lblMatricula);
                String m= matricula.getText().toString();
                for(int k=0;k<datos.size();k++) {
                    if (datos.get(k).getMatricula().toString().equalsIgnoreCase(m)) {
                       x=k;
                    }
                }
                data.putExtra("Matricula", datos.get(x).getMatricula().toString());
                data.putExtra("Marca", datos.get(x).getMarca().toString());
                data.putExtra("Modelo", datos.get(x).getModelo().toString());
                data.putExtra("Motorizacion", datos.get(x).getMotorizacion().toString());//PASAR VALOR AL SPINNER DE EDIT
                data.putExtra("Cilindrada", datos.get(x).getCilindrada());
                data.putExtra("Fecha", datos.get(x).getFechaCompra().toString());//PASAR FECHA AL DATEPICKER DE EDIT ACTIVITY
                data.putExtra("Imagen", datos.get(x).getImageURI().toString());
                data.putExtra("Position", x);
                startActivityForResult(data, 2);


                /*data.putExtra("Matricula", datos.get(position).getMatricula().toString());
                data.putExtra("Marca", datos.get(position).getMarca().toString());
                data.putExtra("Modelo", datos.get(position).getModelo().toString());
                data.putExtra("Motorizacion", datos.get(position).getMotorizacion().toString());//PASAR VALOR AL SPINNER DE EDIT
                data.putExtra("Cilindrada", datos.get(position).getCilindrada());
                data.putExtra("Fecha", datos.get(position).getFechaCompra().toString());//PASAR FECHA AL DATEPICKER DE EDIT ACTIVITY
                data.putExtra("Imagen", datos.get(position).getImageURI().toString());
                data.putExtra("Position", position);
                startActivityForResult(data, 2);*/
            }
        });
    }

    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Introduzca matricula....");
    }
    @Override
    public boolean onQueryTextChange(String newText) {

        if (TextUtils.isEmpty(newText)) {
            LstOpciones.clearTextFilter();
        } else {
            LstOpciones.setFilterText(newText);
        }
        return true;
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();

        if (v.getId() == R.id.LstOpciones) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

            menu.setHeaderTitle(LstOpciones.getAdapter().getItem(info.position).toString());

            inflater.inflate(R.menu.opciones_elementos, menu);
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.EliminarSeleccionada:
                posi = info.position;
                Toast.makeText(getBaseContext(), datos.get(posi).getMatricula(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getBaseContext(), datos.get(posi).getMarca(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getBaseContext(), datos.get(posi).getModelo(), Toast.LENGTH_SHORT).show();
                adaptador.delCoches(datos, posi);
                adaptador.notifyDataSetChanged();//Refresca adaptador.
                return true;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.AnadirCocheOverflow:
                Intent i = new Intent(this, AddActivity.class);
                startActivityForResult(i, 1);
                return true;
            case R.id.AnadirCoche:
                Intent in = new Intent(this, AddActivity.class);
                startActivityForResult(in, 1);
                return true;
            case R.id.BorrarTodas:
                adaptador.deleteAll(datos);
                adaptador.notifyDataSetChanged();//Refresca adaptador.
                return true;
            case R.id.GuardarFichero:
                saveCoches(datos);
                return true;
            case R.id.GuardarFicheroOverflow:
                saveCoches(datos);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                adaptador.addCoche(bundle.getString("Matricula"), bundle.getString("Marca"), bundle.getString("Modelo"), bundle.getString("Motorizacion"), bundle.getString("Cilindrada"), bundle.getString("FechaCompra"), Uri.parse(bundle.getString("Imagen")));
                Toast.makeText(getBaseContext(), "Coche agredado correctamente", Toast.LENGTH_SHORT).show();
                adaptador.notifyDataSetChanged();//Refresca adaptador.
            }
        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                adaptador.editCoche(new Coches(bundle.getString("Matricula"), bundle.getString("Marca"), bundle.getString("Modelo"), bundle.getString("Motorizacion"), bundle.getString("Cilindrada"), bundle.getString("FechaCompra"), Uri.parse(bundle.getString("Imagen"))), bundle.getInt("Position"));
                Toast.makeText(getBaseContext(), "Coche modificado correctamente", Toast.LENGTH_SHORT).show();
                adaptador.notifyDataSetChanged();//Refresca adaptador.
            }
        }
    }
    public void onResume() {
        super.onResume();
        if (datos.isEmpty()) {//Si el arraylist esta vacio
            loadCoches();
        }
    }
    public void onPause() {
        super.onPause();
        saveCoches(datos);
    }
    public void saveCoches(ArrayList<Coches> d) {
        Coches Coches = null;
        try {
            File ruta_sd = Environment.getExternalStorageDirectory();
            File f = new File(ruta_sd.getAbsolutePath(), "lista_Coches.txt");
            PrintWriter printWriter = new PrintWriter(f);
            for (int x = 0; x < datos.size(); x++) {
                Coches = new Coches(datos.get(x).getMatricula(), datos.get(x).getMarca(), datos.get(x).getModelo(), datos.get(x).getMotorizacion(), datos.get(x).getCilindrada(), datos.get(x).getFechaCompra(), datos.get(x).getImageURI());
                printWriter.println(Coches.toString());
            }
            printWriter.close();
        } catch (Exception e) {
            Log.e("Ficheros", "Error al escribir en SD");
        }
    }
    public void loadCoches() {
        String Matricula = null;
        String Marca = null;
        String Modelo = null;
        String Motorizacion = null;
        String Cilindrada = null;
        String FechaCompra = null;
        String Imagen = null;
        String sCadena;
        File ruta_sd = Environment.getExternalStorageDirectory();
        File f = new File(ruta_sd.getAbsolutePath(), "lista_Coches.txt");
        try {
            BufferedReader fin = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            while ((sCadena = fin.readLine()) != null) {
                Matricula = sCadena;// debido a que el puntero se queda al final.
                Marca = fin.readLine();
                Modelo = fin.readLine();
                Motorizacion = fin.readLine();
                Cilindrada = fin.readLine();
                FechaCompra = fin.readLine();
                Imagen = fin.readLine();
                adaptador.addCoche(Matricula, Marca, Modelo, Motorizacion, Cilindrada, FechaCompra, Uri.parse(Imagen));
            }
            adaptador.notifyDataSetChanged();//Refresca adaptador.
        } catch (Exception e) {
            Log.e("Ficheros", "Error al leer en SD");
        }
    }
}
