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
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Agente;

import java.util.ArrayList;

public class AgenteAdapter extends ArrayAdapter<Agente> {
    private Context context;
    private ArrayList<Agente> lista_agente;

    public AgenteAdapter(@NonNull Context context, ArrayList<Agente> lista_agente) {
        super(context, R.layout.item_agente, lista_agente);
        this.context = context;
        this.lista_agente = lista_agente;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_agente, parent, false);
        }

        TextView tv_Cedula = convertView.findViewById(R.id.tv_Cedula);
        TextView tv_NombreAgente = convertView.findViewById(R.id.tv_NombreAgente);
        TextView tv_IdPuestoControl = convertView.findViewById(R.id.tv_IdPuestoControl);
        TextView tv_Rango = convertView.findViewById(R.id.tv_Rango);

        Agente agente = lista_agente.get(position);

        // Asegúrate de convertir los valores a cadenas
        tv_Cedula.setText(String.valueOf(agente.getCedulaa()));
        tv_NombreAgente.setText(agente.getNombre());
        tv_IdPuestoControl.setText(String.valueOf(agente.getIdPuestoControl()));
        tv_Rango.setText(agente.getRango());

        return convertView;
    }
}
