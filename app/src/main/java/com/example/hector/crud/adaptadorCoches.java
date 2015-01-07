package com.example.hector.crud;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Hector on 28/12/2014.
 */
public class adaptadorCoches extends BaseAdapter implements Filterable {

    public ArrayList<Coches> orig;

    Context context;
    ArrayList<Coches> co;


    adaptadorCoches(Activity context, ArrayList<Coches> c) {
       super();
        this.context = context;
       this.co = c;
    }

    public void addCoche(String Matricula, String Marca, String Modelo, String Motorizacion, String Cilindrada, String FechaCompra, Uri ImageUri) {
        co.add(new Coches(Matricula, Marca, Modelo, Motorizacion, Cilindrada, FechaCompra, ImageUri));
    }


    private class ViewHolder {
        ImageView fot;
        TextView mat;
        TextView mar;
        TextView mod;
        TextView cili;
        TextView mot;
        TextView fec;
    }


    public void editCoche(Coches c, int posicion) {
        co.set(posicion, c);
    }

    public void delCoches(ArrayList<Coches> c, int posi) {
        c.remove(posi);//Borra Coches selecionadas
    }

    public void deleteAll(ArrayList<Coches> c) {
        c.clear();
    }


    public View getView(int position, View convertView, ViewGroup parent) {


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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.fot.setImageURI(co.get(position).getImageURI());
        holder.mat.setText(co.get(position).getMatricula());
        holder.mar.setText(co.get(position).getMarca());
        holder.mod.setText(co.get(position).getModelo());
        holder.mot.setText(co.get(position).getMotorizacion());
        holder.cili.setText(co.get(position).getCilindrada());
        holder.fec.setText(co.get(position).getFechaCompra());


        return convertView;

      /* ImageView ivContactImage = (ImageView) item.findViewById(R.id.ivContactImage);
        ivContactImage.setImageURI(co.get(position).getImageURI());

        TextView mat = (TextView)item.findViewById(R.id.lblMatricula);
        mat.setText(co.get(position).getMatricula());

        TextView mar =(TextView)item.findViewById(R.id.lblMarca);
        mar.setText(co.get(position).getMarca());

        TextView model =(TextView)item.findViewById(R.id.lblModelo);
        model.setText(co.get(position).getModelo());

        TextView mo =(TextView)item.findViewById(R.id.lblMotorizacion);
        mo.setText(co.get(position).getMotorizacion());

        TextView ci =(TextView)item.findViewById(R.id.lblCilindrada);
        ci.setText(co.get(position).getCilindrada());

        TextView FechaCompra =(TextView)item.findViewById(R.id.lblFechaCompra);
        FechaCompra.setText(co.get(position).getFechaCompra());
        return(item);*/
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
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                co = (ArrayList<Coches>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
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
