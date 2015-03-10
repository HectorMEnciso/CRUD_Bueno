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
import java.util.HashMap;

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
    boolean entroGaleria=false;
    private String opnSpinner;
    ImageView contactImageImgView;
    Uri imageUri;
    DBController controller = new DBController(this);

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
        FechaCompra=(DatePicker)findViewById(R.id.datePicker);
        contactImageImgView = (ImageView) findViewById(R.id.imgViewContactImage);

        ArrayAdapter<String> adap=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,datos);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Motorizacion.setAdapter(adap);

        Motorizacion.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {

                        opnSpinner=datos[position].toString();
                    }
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
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
    }
    public void callHomeActivity(View view) {
        Intent objIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(objIntent);
    }
    public void onActivityResult(int reqCode, int resCode, Intent data) {
        if (resCode == RESULT_OK) {
            if (reqCode == 1) {
                imageUri = data.getData();
                Log.e("Imagen", imageUri.toString());
                contactImageImgView.setImageURI(imageUri);
            }
        }
    }
    public void onClick(View v){
        int dia,mes,anno;
        dia=FechaCompra.getDayOfMonth();
        mes=FechaCompra.getMonth()+1;
        anno=FechaCompra.getYear();
        String fecha=dia+"/"+mes+"/"+anno;

        if(!entroGaleria) {
            imageUri = Uri.parse(String.valueOf(R.drawable.car));
            entroGaleria=false;
        }

        Log.e("imageUri", imageUri.toString());

        HashMap<String, String> queryValues =  new  HashMap<String, String>();
        queryValues.put("idfoto", imageUri.toString());
        queryValues.put("matricula", Matricula.getText().toString().toUpperCase());
        queryValues.put("marca", Marca.getText().toString().toUpperCase());
        queryValues.put("modelo", Modelo.getText().toString().toUpperCase());
        queryValues.put("cilindrada", Cilindrada.getText().toString());
        queryValues.put("motorizacion", opnSpinner.toString().toUpperCase());
        queryValues.put("fechaCompra",fecha.toString());
        controller.insertCoche(queryValues);
        this.callHomeActivity(v);
    }
    public void onClickCancelar(View v){
        finish();
    }
    public void onClickReset(View v){
        reset=(Button) findViewById(R.id.btnReset);
        contactImageImgView.setImageResource(R.drawable.car);
        Matricula.setText("");
        Marca.setText("");
        Modelo.setText("");
        Motorizacion.setSelection(0);
        Cilindrada.setText("");
        FechaCompra.updateDate(FechaCompra.getYear(),FechaCompra.getMonth(),FechaCompra.getDayOfMonth());
        Log.e("ValorFecha",FechaCompra.getYear()+"/"+FechaCompra.getMonth()+"/"+FechaCompra.getDayOfMonth());
    }
}