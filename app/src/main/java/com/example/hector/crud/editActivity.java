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

/**
 * Created by Hector on 28/12/2014.
 */
public class editActivity extends Activity {
    final String[] datos=new String[]{"Gasolina","Diesel","Hibrido"};
    private EditText Matricula;
    private EditText Marca;
    private EditText Modelo;
    private Spinner Motorizacion;
    private EditText Cilindrada;
    private DatePicker FechaCompra;
    private String opnSpinner;
    boolean entroGaleria=false;
    int position;
    Uri imageUri = Uri.parse("android.resource://com.example.hector.crud/drawable/no_moto.png");
    ImageView contactImageImgView;
    int posMoto;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_coches);
    }

    public void onClick(View v){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("¿Está seguro que desea modificar el coche? ");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Sí",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent data = new Intent();
                        Matricula = (EditText) findViewById(R.id.entradaMatricula);
                        Marca = (EditText) findViewById(R.id.entradaMarca);
                        Modelo = (EditText) findViewById(R.id.entradaModelo);
                        Motorizacion=(Spinner)findViewById(R.id.spnMotorizacion);
                        Cilindrada=(EditText)findViewById(R.id.entradaCilindrada);
                        FechaCompra=(DatePicker)findViewById(R.id.datePicker);
                        int dia,mes,anno;
                        dia=FechaCompra.getDayOfMonth();
                        mes=FechaCompra.getMonth()+1;
                        anno=FechaCompra.getYear();
                        String fecha=dia+"/"+mes+"/"+anno;
                        Bundle b= new Bundle();
                        b.putInt("Position", position);
                        b.putString("Imagen",imageUri.toString());
                        b.putString("Matricula",Matricula.getText().toString().toUpperCase());
                        b.putString("Marca", Marca.getText().toString().toUpperCase());
                        b.putString("Modelo", Modelo.getText().toString().toUpperCase());
                        b.putString("Cilindrada", Cilindrada.getText().toString());
                        b.putString("Motorizacion",opnSpinner.toString().toUpperCase());
                        b.putString("FechaCompra",fecha.toString());
                        data.putExtras(b);
                        setResult(RESULT_OK,data);
                        //---closes the activity---
                        finish();
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

    public void onActivityResult(int reqCode, int resCode, Intent data) {
        if (resCode == RESULT_OK) {
            if (reqCode == 1) {
                imageUri = data.getData();
                entroGaleria=true;
                contactImageImgView.setImageURI(data.getData());
            }
        }
    }
    public void onClickCancelar(View v){
        finish();
    }

    public void onResume() {

        Toast.makeText(getBaseContext(),"Entro onResume()", Toast.LENGTH_LONG);
        super.onResume();
        String matricula,marca,modelo,cilindrada,imagen,moto,fecha;
        Matricula = (EditText) findViewById(R.id.entradaMatricula);
        Marca = (EditText) findViewById(R.id.entradaMarca);
        Modelo = (EditText) findViewById(R.id.entradaModelo);
        Cilindrada=(EditText)findViewById(R.id.entradaCilindrada);
        FechaCompra=(DatePicker)findViewById(R.id.datePicker);
        contactImageImgView=(ImageView)findViewById(R.id.imgViewContactImage);
        moto=getIntent().getStringExtra("Motorizacion");
        switch (moto){
            case "Gasolina":
                posMoto=0;
                break;
            case "Diesel":
                posMoto=1;
                break;
            case "Hibrido":
                posMoto=2;
                break;
        }
        fecha=getIntent().getStringExtra("Fecha");
        matricula=getIntent().getStringExtra("Matricula");
        marca=getIntent().getStringExtra("Marca");
        modelo=getIntent().getStringExtra("Modelo");
        cilindrada=getIntent().getStringExtra("Cilindrada");

        if(!entroGaleria) {
            imageUri = Uri.parse(getIntent().getStringExtra("Imagen"));
            entroGaleria=false;
        }

        position=getIntent().getIntExtra("Position",0);
        Motorizacion=(Spinner)findViewById(R.id.spnMotorizacion);
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

        ///////////////////////////////////////////////////////////////////////////

        String str[] = fecha.split("/");
        int day = Integer.parseInt(str[0]);
        int month = Integer.parseInt(str[1]);
        int year = Integer.parseInt(str[2]);

        Log.e("DIAAAA", String.valueOf(day));
        Log.e("MEEES", String.valueOf(month));
        Log.e("AÑOOOOO", String.valueOf(year));

        //definir valor spinner.
        FechaCompra.updateDate(year,month-1,day);//definir valor del datepicker con los valores del main.

        ///////////////////////////////////////////////////////////////////////////////////

        contactImageImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Contact Image"), 1);
            }

        });

       /* if (imagen!=null){
            imageUri= Uri.parse(imagen);
        }*/

        contactImageImgView.setImageURI(imageUri);
    }

}
