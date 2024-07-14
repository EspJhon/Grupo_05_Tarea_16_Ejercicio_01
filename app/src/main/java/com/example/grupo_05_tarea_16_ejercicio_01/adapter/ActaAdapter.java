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
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Acta;

import java.util.ArrayList;

public class ActaAdapter extends ArrayAdapter<Acta> {
    private Context context;
    private ArrayList<Acta> lista_actas;

    public ActaAdapter(@NonNull Context context, ArrayList<Acta> lista_actas) {
        super(context, R.layout.item_acta, lista_actas);
        this.context = context;
        this.lista_actas = lista_actas;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_acta, parent, false);
        }

        TextView tv_IdActa = convertView.findViewById(R.id.tv_IdActa);
        TextView tv_Codigo = convertView.findViewById(R.id.tv_Codigo);
        TextView tv_Fecha = convertView.findViewById(R.id.tv_Fecha);

        Acta acta = lista_actas.get(position);

        // Asegúrate de convertir los valores a cadenas
        tv_IdActa.setText(String.valueOf(acta.getIdActa()));
        tv_Codigo.setText(String.valueOf(acta.getCodigo()));
        tv_Fecha.setText(acta.getFecha());

        return convertView;
    }
}
