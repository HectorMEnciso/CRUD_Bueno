package com.example.hector.crud;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends Activity implements SearchView.OnQueryTextListener {
    private SearchView mSearchView; //Declaracion global del SearchView sSearchView
    private ListView lstCoches; //Declaracion GLobal del listView lstCoches.
    //adaptadorCoches adaptador; //Declaracion global del adapdatorCoches adaptador.
    //private ArrayList<Coches> datos = new ArrayList<Coches>();//Declaracion global del ArrayList<Coches> datos.
    private TextView ID;
    int posi,x; //Variables globales para las posiciones.
    DBController controller = new DBController(this);
    SimpleAdapter adaptador;
    ArrayList<HashMap<String, String>> cochesList;
    private ArrayList<Coches> coches= new ArrayList<Coches>();
    HashMap<String, String> queryValues =  new  HashMap<String, String>();

    public final static int SOCKET_PORT = 6000;
    public final static String SERVER = "10.0.2.2";  // localhost
    Socket sock = null;
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;
    String file="/data/data/com.example.hector.crud/files/Coches.xml";
    public final static int FILE_SIZE = 6022386;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//Cargamos layout
        try
        {
            OutputStreamWriter fout=
                    new OutputStreamWriter(
                            openFileOutput("prueba_int.txt", Context.MODE_PRIVATE));
            fout.write("Texto de prueba.");
            fout.close();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al escribir fichero a memoria interna");
        }
        cochesList =  controller.getAllCoches();
      //  adaptador = new adaptadorCoches(this, datos);//Instanciación del adaptador al que le pasamos el arralist con los datos datos.
        lstCoches = (ListView) findViewById(R.id.LstOpciones);//Obtenemos la referencia al listView
        lstCoches.setAdapter(adaptador);//añadimos el adaptador al listView lstCoches.
        lstCoches.setTextFilterEnabled(true);//Habilitamos la busqueda en el listView lstCoches
        registerForContextMenu(lstCoches);//Asociamos el menu contextual al listView lstCoches
        mSearchView = (SearchView) findViewById(R.id.searchView1);//Obtenemos la referencia al SearchView mSearchView
        mSearchView.setBackgroundColor(Color.LTGRAY);//Color de fondo para el SearchView mSearchView
        setupSearchView();//Llamada al metodo
        ID=(TextView) findViewById(R.id.ID);
        if(cochesList.size()!=0) {
            lstCoches.setOnItemClickListener(new AdapterView.OnItemClickListener() { //Escuchador para cada fila del listView
                @Override
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    ID=(TextView) v.findViewById(R.id.ID);
                    Intent data = new Intent(MainActivity.this, editActivity.class);//Intent explicito a editActivity
                    ImageView contactImageImgView=(ImageView)findViewById(R.id.imgViewContactImage);
                    TextView ID = (TextView) v.findViewById(R.id.ID);
                    TextView matricula = (TextView) v.findViewById(R.id.lblMatricula);//Obtenemos la referencia al listView TextView lblMatricula
                    TextView marca = (TextView) v.findViewById(R.id.lblMarca);
                    TextView modelo = (TextView) v.findViewById(R.id.lblModelo);
                    TextView motorizacion = (TextView) v.findViewById(R.id.lblMotorizacion);
                    TextView cilindrada = (TextView) v.findViewById(R.id.lblCilindrada);
                    TextView fechaCompra = (TextView) v.findViewById(R.id.lblFechaCompra);
                   String m = matricula.getText().toString();//Almacenamos el texto de lblMatricula

                    for (int k = 0; k < cochesList.size(); k++) {//Recorremos el ArrayList<Coches> datos
                        if (cochesList.get(k).get("matricula").toString().equalsIgnoreCase(m)) {//Para cada elemento comparamos cada matricula
                            x = k;//Guardamos aquella posicion cuyo elemento coincida.
                        }
                    }

               // Pasamos todos los datos del elemento al editActivity
                    data.putExtra("id", cochesList.get(x).get("id"));
                    data.putExtra("Matricula", cochesList.get(x).get("matricula").toString());
                    data.putExtra("Marca",cochesList.get(x).get("marca").toString());
                    data.putExtra("Modelo", cochesList.get(x).get("modelo").toString());
                    data.putExtra("Motorizacion", cochesList.get(x).get("motorizacion").toString());
                    data.putExtra("Cilindrada", cochesList.get(x).get("cilindrada").toString());
                    data.putExtra("Fecha", cochesList.get(x).get("fechaCompra").toString());
                    data.putExtra("Imagen", cochesList.get(x).get("idfoto").toString());
                    Log.e("rrrrr",cochesList.get(x).get("idfoto").toString());
                    data.putExtra("Position", x);//mando la posicion correcta del elemento buscado.
                    Log.e("posClick", String.valueOf(x));
                    startActivityForResult(data, 2);
                }
            });
            adaptador = new SimpleAdapter(MainActivity.this,cochesList, R.layout.mi_layout, new String[] { "id" ,"idfoto","matricula","marca","modelo","motorizacion","cilindrada","fechaCompra"}, new int[] {R.id.ID,R.id.ivContactImage, R.id.lblMatricula, R.id.lblMarca,R.id.lblModelo,R.id.lblMotorizacion,R.id.lblCilindrada,R.id.lblFechaCompra});
            lstCoches.setAdapter(adaptador);
        }
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
            lstCoches.clearTextFilter();
        } else {
            lstCoches.setFilterText(newText);
        }
        return true;
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();

        if (v.getId() == R.id.LstOpciones) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

            menu.setHeaderTitle("SE BORRARÁ EL VEHICULO CON MATRÍCULA: "+cochesList.get(info.position).get("matricula"));

            inflater.inflate(R.menu.opciones_elementos, menu);
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final TextView ID=(TextView) info.targetView.findViewById(R.id.ID);
        switch (item.getItemId()) {
            case R.id.EliminarSeleccionada:
                posi = info.position;
                String dataCarDelete="¡COCHE ELIMINADO!\n";
                dataCarDelete=dataCarDelete+"MATRICULA: "+cochesList.get(posi).get("matricula")+"\nMARCA: "+cochesList.get(posi).get("marca")+"\nMODELO: "+cochesList.get(posi).get("modelo");
                Toast.makeText(getBaseContext(),dataCarDelete, Toast.LENGTH_LONG).show();
                String CocheId = ID.getText().toString();
                controller.deleteCoche(CocheId);
                cochesList.remove(posi);
                dataCarDelete="";
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
                controller.deleteAllCoches();
                Intent objIntent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(objIntent1);
                adaptador.notifyDataSetChanged();//Refresca adaptador.
                return true;
            case R.id.GuardarBD:
                return true;
            case R.id.GuardarBDOverflow:
                return true;
            case R.id.Generarxml:
                controller.GenerarXMl(controller.getAllCoches());
                return true;
            case R.id.Cargarxml:
                //coches.clear();
                CargarXmlTask tarea = new CargarXmlTask();
                tarea.execute("http://10.0.2.2/Coches.xml");

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onResume(){
        super.onResume();
        Log.e("entroOnResume","");
        cochesList =  controller.getAllCoches();

        adaptador = new SimpleAdapter(MainActivity.this,cochesList, R.layout.mi_layout, new String[] { "id" ,"idfoto","matricula","marca","modelo","motorizacion","cilindrada","fechaCompra"}, new int[] {R.id.ID,R.id.ivContactImage, R.id.lblMatricula, R.id.lblMarca,R.id.lblModelo,R.id.lblMotorizacion,R.id.lblCilindrada,R.id.lblFechaCompra});
        lstCoches.setAdapter(adaptador);
    }

    /*public void onStop(){
        super.onStop();
        Log.e("entroOnStop","");
        Log.e("cocheSize onStop",String.valueOf(coches.size()));
        for(int i=0;i<coches.size();i++){
            {
                String matricula= coches.get(i).getMatricula();

                if (!controller.existeCoche(matricula)){
                    HashMap<String, String> queryValues =  new  HashMap<String, String>();
                    queryValues.put("idfoto",String.valueOf(coches.get(i).getImageURI()));
                    queryValues.put("matricula",coches.get(i).getMatricula());
                    queryValues.put("marca",coches.get(i).getMarca());
                    queryValues.put("modelo",coches.get(i).getModelo());
                    queryValues.put("motorizacion",coches.get(i).getMotorizacion());
                    queryValues.put("cilindrada",coches.get(i).getCilindrada());
                    queryValues.put("fechaCompra",coches.get(i).getFechaCompra());
                    controller.insertCoche(queryValues);
                }
            }
        }
    }*/

    private class CargarXmlTask extends AsyncTask<String,Integer,Boolean> {
        @Override
        protected Boolean doInBackground(String... params)  {
            Log.e("entro doInBackground","");
            try {
                Log.e("PreConnecting...", "");
                sock = new Socket(SERVER, SOCKET_PORT);
                Log.e("Conectando.........", sock.toString());
                obtenerfichero();

                RssParserDom saxparser = new RssParserDom(file);

                coches = saxparser.parse();//parsear y guarda datos en lista de noticias
                Log.e("cochesAsynTask",String.valueOf(coches.size()));
                return true;
            }
            catch(IOException e) {
                 /*if (fos != null) fos.close();
                 if (bos != null) bos.close();
                 if (sock != null) sock.close();*/
            }
            return true;

        }
        protected void onPostExecute(Boolean result) {
            int cochesRepetidos=0;
            Log.e("entro onPostExecute","onPostExecute");
            Log.e("coches size onPostExecute",String.valueOf(coches.size()));
            for(int i=0; i<coches.size(); i++){
                Log.e("entro for",String.valueOf(coches.size()));

                String matricula= coches.get(i).getMatricula();
                if (!controller.existeCoche(matricula)) {
                    HashMap<String, String> queryValues = new HashMap<String, String>();
                    queryValues.put("idfoto", String.valueOf(coches.get(i).getImageURI()));
                    Log.e("idfoto", coches.get(i).getImageURI().toString());
                    queryValues.put("matricula", coches.get(i).getMatricula());
                    queryValues.put("marca", coches.get(i).getMarca());
                    queryValues.put("modelo", coches.get(i).getModelo());
                    queryValues.put("cilindrada", coches.get(i).getCilindrada());
                    queryValues.put("motorizacion", coches.get(i).getMotorizacion());
                    queryValues.put("fechaCompra", coches.get(i).getFechaCompra());
                    controller.insertCoche(queryValues);
                    Intent objIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(objIntent);
                }
                else {
                    cochesRepetidos++;
                }
            }
            if(cochesRepetidos>0){
                Toast.makeText(getApplicationContext(),cochesRepetidos+" ya se encuentran y no se actualizarán.",Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void obtenerfichero () throws IOException {
        int bytesRead;
        int current = 0;
        try {
            // receive file
            Log.e("Mensaje","entro en obtener fichero");
            byte[] mybytearray = new byte[FILE_SIZE];
            InputStream is = sock.getInputStream();
            //fos = new OutputStreamWriter(openFileOutput(file, Context.MODE_PRIVATE));
            Log.e("prueba",file.toString());
            Log.e("Mensaje","obtengo inputstream");
            File f = new File(file);
            Log.e("Mensaje","creo file");
            fos = new FileOutputStream(f);
            Log.e("Mensaje","creo fos");
            bos = new BufferedOutputStream(fos);
            Log.e("Mensaje","creo bos");
            bytesRead = is.read(mybytearray, 0, mybytearray.length);
            current = bytesRead;

            do {
                bytesRead =
                        is.read(mybytearray, current, (mybytearray.length - current));
                if (bytesRead >= 0) current += bytesRead;
            } while (bytesRead > -1);
            bos.write(mybytearray,0,current);
            bos.flush();
            Log.e("prueba",file.toString());
        }
        finally {
            if (fos != null) fos.close();
            if (bos != null) bos.close();
            if (sock != null) sock.close();
        }
    }
}
