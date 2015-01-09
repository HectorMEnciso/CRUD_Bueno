package com.example.hector.crud;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
    private SearchView mSearchView; //Declaracion global del SearchView sSearchView
    private ListView LstOpciones; //Declaracion GLobal del listView LstOpciones.
    adaptadorCoches adaptador; //Declaracion global del adapdatorCoches adaptador.
    private ArrayList<Coches> datos = new ArrayList<Coches>();//Declaracion global del ArrayList<Coches> datos.
    int posi,x; //Variables globales para las posiciones.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//Cargamos layout
        adaptador = new adaptadorCoches(this, datos);//Instanciación del adaptador al que le pasamos el arralist con los datos datos.
        LstOpciones = (ListView) findViewById(R.id.LstOpciones);//Obtenemos la referencia al listView
        LstOpciones.setAdapter(adaptador);//añadimos el adaptador al listView LstOpciones.
        LstOpciones.setTextFilterEnabled(true);//Habilitamos la busqueda en el listView LstOpciones
        registerForContextMenu(LstOpciones);//Asociamos el menu contextual al listView LstOpciones
        mSearchView = (SearchView) findViewById(R.id.searchView1);//Obtenemos la referencia al SearchView mSearchView
        mSearchView.setBackgroundColor(Color.LTGRAY);//Color de fondo para el SearchView mSearchView
        setupSearchView();//Llamada al metodo
        LstOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener() { //Escuchador para cada fila del listView
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Intent data = new Intent(MainActivity.this, editActivity.class);//Intent explicito a editActivity
                TextView matricula=(TextView) v.findViewById(R.id.lblMatricula);//Obtenemos la referencia al listView TextView lblMatricula
                String m= matricula.getText().toString();//Almacenamos el texto de lblMatricula
                for(int k=0;k<datos.size();k++) {//Recorremos el ArrayList<Coches> datos
                    if (datos.get(k).getMatricula().toString().equalsIgnoreCase(m)) {//Para cada elemento comparamos cada matricula
                       x=k;//Guardamos aquella posicion cuyo elemento coincida.
                    }
                }
                /*
                Pasamos todos los datos del elemento al editActivity
                 */
                data.putExtra("Matricula", datos.get(x).getMatricula().toString());
                data.putExtra("Marca", datos.get(x).getMarca().toString());
                data.putExtra("Modelo", datos.get(x).getModelo().toString());
                data.putExtra("Motorizacion", datos.get(x).getMotorizacion().toString());
                data.putExtra("Cilindrada", datos.get(x).getCilindrada());
                data.putExtra("Fecha", datos.get(x).getFechaCompra().toString());
                data.putExtra("Imagen", datos.get(x).getImageURI().toString());
                data.putExtra("Position", x);//mando la posicion correcta del elemento buscado.
                Log.e("posClick",String.valueOf(x));
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
        mSearchView.setIconifiedByDefault(false); //Define el estado del campo de busqueda.
        mSearchView.setOnQueryTextListener(this);//Define un escuchador para para las acciones dentro del searchView
        mSearchView.setSubmitButtonEnabled(true);//Habilita el boton Submit cuando no esta vacia.
        mSearchView.setQueryHint("Introduzca matricula....");//Texto a mostrar.
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
        TextView matricula=(TextView) v.findViewById(R.id.lblMatricula);
        if (v.getId() == R.id.LstOpciones) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

            menu.setHeaderTitle(datos.get(info.position).getMatricula());

            inflater.inflate(R.menu.opciones_elementos, menu);
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.EliminarSeleccionada:
                posi = info.position;
                String dataCarDelete="¡COCHE ELIMINADO!\n";
                dataCarDelete=dataCarDelete+"MATRICULA:"+datos.get(posi).getMatricula()+"\nMARCA: "+datos.get(posi).getMarca()+"\nMODELO: "+datos.get(posi).getModelo();
                Toast.makeText(getBaseContext(),dataCarDelete, Toast.LENGTH_LONG).show();
                dataCarDelete="";
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
                adaptador.addCoche(bundle.getString("Matricula"), bundle.getString("Marca"), bundle.getString("Modelo"), bundle.getString("Motorizacion"), bundle.getString("Cilindrada"), bundle.getString("FechaCompra"), Uri.parse(bundle.getString("Imagen")),datos);
                //adaptador.addCoche(datos); No he visto como implementarlo de esta forma.
                Toast.makeText(getBaseContext(), "Coche agredado correctamente", Toast.LENGTH_SHORT).show();
                adaptador.notifyDataSetChanged();//Refresca adaptador.
            }
        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                adaptador.editCoche(new Coches(bundle.getString("Matricula"), bundle.getString("Marca"), bundle.getString("Modelo"), bundle.getString("Motorizacion"), bundle.getString("Cilindrada"), bundle.getString("FechaCompra"), Uri.parse(bundle.getString("Imagen"))), bundle.getInt("Position"),datos);
               Log.e("possssssssssssss",String.valueOf(bundle.getInt("Position")));
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
               // adaptador.addCoche(Matricula, Marca, Modelo, Motorizacion, Cilindrada, FechaCompra, Uri.parse(Imagen));
               adaptador.addCoche(Matricula, Marca, Modelo, Motorizacion, Cilindrada, FechaCompra, Uri.parse(Imagen),datos);
            }
            adaptador.notifyDataSetChanged();//Refresca adaptador.
        } catch (Exception e) {
            Log.e("Ficheros", "Error al leer en SD");
        }
    }
}
