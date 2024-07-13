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
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Audiencia;

import java.util.ArrayList;

public class AudienciaAdapter extends ArrayAdapter<Audiencia> {

    private Context context;
    private ArrayList<Audiencia> audiencias;
    private DBHelper dbHelper;

    public AudienciaAdapter(@NonNull Context context, ArrayList<Audiencia> audiencias) {
        super(context, R.layout.audiencia_item, audiencias);
        this.context = context;
        this.audiencias = audiencias;
        dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.audiencia_item, parent, false);
        }
        TextView tv_codigo = convertView.findViewById(R.id.tv_codigo);
        TextView tv_lugar = convertView.findViewById(R.id.tv_lugar);
        TextView tv_fecha = convertView.findViewById(R.id.tv_fecha);
        TextView tv_hora = convertView.findViewById(R.id.tv_hora);


        tv_codigo.setText("Código: " + audiencias.get(position).getCodigo()+"");
        tv_lugar.setText("Lugar: " + audiencias.get(position).getLugar());
        tv_fecha.setText("Fecha: " + audiencias.get(position).getFecha());
        tv_hora.setText("Hora: " + audiencias.get(position).getHora());


        return convertView;
    }

}
