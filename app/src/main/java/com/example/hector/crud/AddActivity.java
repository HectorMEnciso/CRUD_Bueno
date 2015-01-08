package com.example.hector.crud;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Hector on 28/12/2014.
 */
public class AddActivity extends Activity {

    final String[] datos=new String[]{"Gasolina","Diesel","Hibrido"};
    private EditText Matricula;
    private EditText Marca;
    private EditText Modelo;
    private Spinner Motorizacion;
    private EditText Cilindrada;
    private DatePicker FechaCompra;
    private Button reset;
    private String opnSpinner;
    ImageView contactImageImgView;
    Uri imageUri;// = Uri.parse("android.resource://com.example.hector.crud/drawable/car.png");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_coches);
        Matricula = (EditText) findViewById(R.id.entradaMatricula);
        Marca = (EditText) findViewById(R.id.entradaMarca);
        Modelo = (EditText) findViewById(R.id.entradaModelo);
        Motorizacion=(Spinner)findViewById(R.id.spnMotorizacion);
        Cilindrada=(EditText)findViewById(R.id.entradaCilindrada);
        reset=(Button)findViewById(R.id.btnReset);
        Motorizacion=(Spinner)findViewById(R.id.spnMotorizacion);
        FechaCompra=(DatePicker)findViewById(R.id.datePicker);
        ArrayAdapter<String> adap=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,datos);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Motorizacion.setPrompt("SELECCIONE MOTORIZACIÓN");//no funciona
        Motorizacion.setAdapter(adap);
        Motorizacion.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {

                        opnSpinner=datos[position].toString();//paso el elemento mediante objeto bundle.
                    }
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        contactImageImgView = (ImageView) findViewById(R.id.imgViewContactImage);

        contactImageImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Contact Image"), 1);
            }

        });
    }


    public void onActivityResult(int reqCode, int resCode, Intent data) {
        if (resCode == RESULT_OK) {
            if (reqCode == 1) {
                imageUri = data.getData();
                Log.e("imaaaaaaaaaaaaaaagennn", imageUri.toString());
                contactImageImgView.setImageURI(data.getData());
            }
        }
    }
    public void onClick(View v){
        Intent data = new Intent();
        Bundle b= new Bundle();

        int dia,mes,anno;
        dia=FechaCompra.getDayOfMonth();
        mes=FechaCompra.getMonth()+1;
        anno=FechaCompra.getYear();
        String fecha=dia+"/"+mes+"/"+anno;
        b.putString("Imagen", imageUri.toString());
        b.putString("Matricula",Matricula.getText().toString());
        b.putString("Marca", Marca.getText().toString());
        b.putString("Modelo", Modelo.getText().toString());
        b.putString("Cilindrada", Cilindrada.getText().toString());
        b.putString("Motorizacion",opnSpinner.toString());
        b.putString("FechaCompra",fecha.toString());
        data.putExtras(b);
        setResult(RESULT_OK, data);
        //---closes the activity---
        finish();
    }
    public void onClickCancelar(View v){
        finish();
    }

public void onClickReset(View v){
    contactImageImgView.setImageResource(R.drawable.car);
    Matricula.setText("");
    Marca.setText("");
    Modelo.setText("");
    Motorizacion.setSelection(0);
    Cilindrada.setText("");

    //FechaCompra.updateDate(fecha.da);
}
}