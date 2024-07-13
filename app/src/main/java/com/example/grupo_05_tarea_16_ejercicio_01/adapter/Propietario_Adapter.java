package com.example.grupo_05_tarea_16_ejercicio_01.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Propietario;

import java.util.ArrayList;

public class Propietario_Adapter extends ArrayAdapter<Propietario> {

    private Context context;
    private ArrayList<Propietario> datos;
    private DBHelper dbHelper;
    private OnItemLongClickListener longClickListener;


    public Propietario_Adapter(@NonNull Context context, ArrayList<Propietario> datos,OnItemLongClickListener longClickListener) {
        super(context, R.layout.propietario_item,datos);

        this.context = context;
        this.datos = datos;
        this.longClickListener = longClickListener;
        dbHelper = new DBHelper(context);


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.propietario_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tv_cedula = convertView.findViewById(R.id.tv_cedula);
            viewHolder.tv_nombre = convertView.findViewById(R.id.tv_nombre);
            viewHolder.tv_ciudad = convertView.findViewById(R.id.tv_ciudad);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Propietario propietario = datos.get(position);

        viewHolder.tv_cedula.setText(String.valueOf(propietario.getCedulap()));
        viewHolder.tv_nombre.setText(propietario.getNombre());
        viewHolder.tv_ciudad.setText(String.valueOf(propietario.getCiudad()));

        convertView.setOnLongClickListener(v -> {
            longClickListener.onItemLongClick(propietario);
            return true;
        });

        return convertView;

    }

    static class ViewHolder {
        TextView tv_cedula;
        TextView tv_nombre;
        TextView tv_ciudad;
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(Propietario propietario);
    }
}
