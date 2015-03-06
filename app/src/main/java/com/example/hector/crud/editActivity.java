package com.example.hector.crud;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by Hector on 28/12/2014.
 */
public class editActivity extends Activity {
    final String[] datos=new String[]{"GASOLINA","DIESEL","HIBRIDO"};
    private EditText Matricula;
    private EditText Marca;
    private EditText Modelo;
    private Spinner Motorizacion;
    private EditText Cilindrada;
    private DatePicker FechaCompra;
    private String opnSpinner;
    boolean entroGaleria=false;

    DBController controller = new DBController(this);
    HashMap<String, String> CochesList;
    String moto,fecha;
    Uri imageUri;// = Uri.parse("android.resource://com.example.hector.crud/drawable/car.png");
    ImageView contactImageImgView;
    int posMoto;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_coches);

        contactImageImgView=(ImageView)findViewById(R.id.imgViewContactImage);
        Matricula = (EditText) findViewById(R.id.entradaMatricula);
        Marca = (EditText) findViewById(R.id.entradaMarca);
        Modelo = (EditText) findViewById(R.id.entradaModelo);
        Cilindrada=(EditText)findViewById(R.id.entradaCilindrada);
        Motorizacion=(Spinner)findViewById(R.id.spnMotorizacion);
        FechaCompra=(DatePicker)findViewById(R.id.datePicker);

        Intent objIntent = getIntent();
        String id = objIntent.getStringExtra("id");
        CochesList = controller.getCocheInfo(id);

        Log.e("FotoBien",Uri.parse(CochesList.get("idfoto")).toString());

         if(CochesList.get("idfoto").toString().equals("2130837558")){
             Log.e("if","entro if");
             Uri imageUri1;
             imageUri1=Uri.parse(String.valueOf(R.drawable.car));
             contactImageImgView.setImageURI(imageUri1);
        }
        else{
             Log.e("else","entro else");
             contactImageImgView.setImageURI(Uri.parse(CochesList.get("idfoto")));
         }
        //contactImageImgView.setImageURI(Uri.parse(CochesList.get("idfoto")));
        Matricula.setText(CochesList.get("matricula"));
        Marca.setText(CochesList.get("marca"));
        Modelo.setText(CochesList.get("modelo"));
        Cilindrada.setText(CochesList.get("cilindrada"));
        moto=getIntent().getStringExtra("Motorizacion");

        Log.e("MOTO",moto);
        switch (moto){
            case "GASOLINA":
                posMoto=0;
                break;
            case "DIESEL":
                posMoto=1;
                break;
            case "HIBRIDO":
                posMoto=2;
                break;
        }
        Motorizacion.setSelection(posMoto);
        fecha=getIntent().getStringExtra("Fecha");
        Log.d("Fecha",fecha.toString());
        String str[] = fecha.split("/");//Vector con caracter delimitador
        int day = Integer.parseInt(str[0]);
        int month = Integer.parseInt(str[1]);
        int year = Integer.parseInt(str[2]);

        FechaCompra.updateDate(year,month-1,day);
    }

    public void onClick(final View v){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("¿Está seguro que desea modificar el coche? ");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Sí",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        HashMap<String, String> queryValues =  new  HashMap<String, String>();
                        //Intent data = new Intent();
                        Matricula = (EditText) findViewById(R.id.entradaMatricula);
                        Marca = (EditText) findViewById(R.id.entradaMarca);
                        Modelo = (EditText) findViewById(R.id.entradaModelo);
                        Motorizacion=(Spinner)findViewById(R.id.spnMotorizacion);
                        Cilindrada=(EditText)findViewById(R.id.entradaCilindrada);
                        FechaCompra=(DatePicker)findViewById(R.id.datePicker);
                        Intent objIntent = getIntent();
                        String CocheId = objIntent.getStringExtra("id");
                        queryValues.put("id", CocheId);
                        queryValues.put("idfoto", imageUri.toString());
                        queryValues.put("matricula", Matricula.getText().toString().toUpperCase());
                        queryValues.put("marca", Marca.getText().toString().toUpperCase());
                        queryValues.put("modelo", Modelo.getText().toString().toUpperCase());
                        queryValues.put("cilindrada", Cilindrada.getText().toString());
                        queryValues.put("motorizacion", opnSpinner.toString().toUpperCase());
                        int dia,mes,anno;
                        dia=FechaCompra.getDayOfMonth();
                        mes=FechaCompra.getMonth()+1;
                        anno=FechaCompra.getYear();
                        String fecha=dia+"/"+mes+"/"+anno;
                        queryValues.put("fechaCompra",fecha.toString());
                        controller.updateCoche(queryValues);
                        callHomeActivity(v);
                    }
                });
        builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }
    public void callHomeActivity(View view) {
        Intent objIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(objIntent);
    }
    public void onActivityResult(int reqCode, int resCode, Intent data) {
        if (resCode == RESULT_OK) {
            if (reqCode == 1) {
                imageUri = data.getData();

                contactImageImgView.setImageURI(imageUri);
            }
        }
    }
    public void onClickCancelar(View v){
        finish();
    }

    public void onResume() {
        super.onResume();
        String matricula,marca,modelo,cilindrada,fecha;

        fecha=getIntent().getStringExtra("Fecha");
        matricula=getIntent().getStringExtra("Matricula");
        marca=getIntent().getStringExtra("Marca");
        modelo=getIntent().getStringExtra("Modelo");
        cilindrada=getIntent().getStringExtra("Cilindrada");

       /*if(!entroGaleria) {
            imageUri = Uri.parse(getIntent().getStringExtra("Imagen"));
            Log.e("valorrrr",imageUri.toString());
            entroGaleria=false;
        }*/

        ArrayAdapter<String> adap=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,datos);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Motorizacion.setAdapter(adap);
        Motorizacion.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {
                        opnSpinner=datos[position].toString();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        Matricula.setText(matricula);
        Marca.setText(marca);
        Modelo.setText(modelo);
        Cilindrada.setText(cilindrada);
        Motorizacion.setSelection(posMoto);//definir valor spinner mediante la posición.

        String str[] = fecha.split("/");//Vector con caracter delimitador
        int day = Integer.parseInt(str[0]);
        int month = Integer.parseInt(str[1]);
        int year = Integer.parseInt(str[2]);

        FechaCompra.updateDate(year,month-1,day);//Actualizacion del valor de DatePicker con los valores provenientes del elemento a modificar desde el listView

        contactImageImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Contact Image"), 1);
                entroGaleria=true;
            }

        });
       //contactImageImgView.setImageURI(imageUri);
    }

}
