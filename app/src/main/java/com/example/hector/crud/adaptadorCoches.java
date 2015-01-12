package com.example.hector.crud;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Hector on 28/12/2014.
 */
public class adaptadorCoches extends BaseAdapter implements Filterable {

    public ArrayList<Coches> orig; //ArrayList de coches para la busqueda.
    Context context; //Variable global context (desde la que se crea el adaptador)
    ArrayList<Coches> co; //ArrayList de coches original

    adaptadorCoches(Activity context, ArrayList<Coches> c) {//Constructor
       super();
       this.context = context;
       this.co = c;
    }
    public void UpdateAdaptador(ArrayList<Coches> a){
        this.co=a;
        this.co=new ArrayList<Coches>();
        this.co.addAll(a);
    }
    public void addCoche(String Matricula, String Marca, String Modelo, String Motorizacion, String Cilindrada, String FechaCompra, Uri ImageUri,ArrayList<Coches> c) {
        c.add(new Coches(Matricula, Marca, Modelo, Motorizacion, Cilindrada, FechaCompra, ImageUri));
        UpdateAdaptador(c);
    }
    /*Optimización de listas
        aporta agilidad y menor consumo de bateria.
        /////////////////////////////////////////////////////////////////////////////////
        La clase ViewHolder tan sólo va a contener una
        referencia a cada uno de los controles que tengamos que manipular de nuestro layout
     */
    private class ViewHolder { //Metodo para la optimizacion
        ImageView fot;
        TextView mat;
        TextView mar;
        TextView mod;
        TextView cili;
        TextView mot;
        TextView fec;
    }

    public void editCoche(Coches c, int posicion,ArrayList<Coches> d) {
        d.set(posicion, c);
        UpdateAdaptador(d);
    }

    public void delCoches(ArrayList<Coches> c, int posi) {
        c.remove(posi);//Borra Coches selecionada
        UpdateAdaptador(c);
    }

    public void deleteAll(ArrayList<Coches> c) {
        c.clear();
    }
/*
ahorrarnos el trabajo de inflar el layout definido cada vez que se muestra un nuevo elemento
 */
    public View getView(int position, View convertView, ViewGroup parent) {
        /*
        convertView --> recibe layout que puedan ser reutilizados.
         */
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.mi_layout, parent, false);
            holder = new ViewHolder();
            holder.fot = (ImageView) convertView.findViewById(R.id.ivContactImage);
            holder.mat = (TextView) convertView.findViewById(R.id.lblMatricula);
            holder.mar = (TextView) convertView.findViewById(R.id.lblMarca);
            holder.mod = (TextView) convertView.findViewById(R.id.lblModelo);
            holder.cili = (TextView) convertView.findViewById(R.id.lblCilindrada);
            holder.mot = (TextView) convertView.findViewById(R.id.lblMotorizacion);
            holder.fec = (TextView) convertView.findViewById(R.id.lblFechaCompra);
            /*
            propiedad  Tag (podemos
            asignarla y recuperarla mediante los métodos setTag() y getTag() respectivamente) que puede
            contener cualquier tipo de objeto
             */
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(co.get(position).getImageURI().toString().equals("android.resource://com.example.hector.crud/drawable/car.png")){
            holder.fot.setImageResource(R.drawable.car);
        }
        else{
            holder.fot.setImageURI(co.get(position).getImageURI());
        }
        holder.mat.setText(co.get(position).getMatricula());
        holder.mar.setText(co.get(position).getMarca());
        holder.mod.setText(co.get(position).getModelo());
        holder.mot.setText(co.get(position).getMotorizacion());
        holder.cili.setText(co.get(position).getCilindrada());
        holder.fec.setText(co.get(position).getFechaCompra());
        return convertView;
    }
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected Filter.FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Coches> results = new ArrayList<Coches>();
                if (orig == null)
                    orig = co;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Coches g : orig) {
                            if (g.getMatricula().toLowerCase()
                                    .contains(constraint.toString().toLowerCase()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {
                co = (ArrayList<Coches>) results.values;
                Log.e("numeroElementos",String.valueOf(co.size()));
                UpdateAdaptador(co);
                notifyDataSetChanged();

            }
        };
    }

    @Override
    public int getCount() {
        return co.size();
    }

    @Override
    public Object getItem(int position) {
        return co.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}