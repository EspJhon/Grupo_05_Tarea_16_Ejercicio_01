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
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Accidente;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.OficinaGob;

import java.util.ArrayList;

public class OficinaAdapter extends ArrayAdapter<OficinaGob> {

    private Context context;
    private ArrayList<OficinaGob> oficinas;
    private DBHelper dbHelper;

    public OficinaAdapter(@NonNull Context context, ArrayList<OficinaGob> oficinas) {
        super(context, R.layout.oficina_item,oficinas);
        this.context=context;
        this.oficinas=oficinas;
        dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.oficina_item, parent, false);
        }
        TextView tv_valorVehiculo = convertView.findViewById(R.id.tv_valorVehiculo);
        TextView tv_numPoliza = convertView.findViewById(R.id.tv_numPoliza);
        TextView tv_numPlaca = convertView.findViewById(R.id.tv_numPlaca);
        TextView tv_ubicacion = convertView.findViewById(R.id.tv_ubicacion);

        tv_valorVehiculo.setText("Valor del vehículo: " + oficinas.get(position).getValor_vehiculo());
        tv_numPoliza.setText("Número de poliza: " + oficinas.get(position).getNpoliza() + "");
        tv_numPlaca.setText("Número de placa: " + oficinas.get(position).getIdVehiculo()+"");
        tv_ubicacion.setText("Ubicación: " + oficinas.get(position).getUbicacion());

        return convertView;
    }
}
