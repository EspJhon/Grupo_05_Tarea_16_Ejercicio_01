package com.example.grupo_05_tarea_16_ejercicio_01.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;

import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Accidente;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Propietario;

import java.util.ArrayList;

public class AccidenteAdapter extends ArrayAdapter<Accidente> {

    private Context context;
    private ArrayList<Accidente> accidentes;
    private DBHelper dbHelper;

    public AccidenteAdapter(@NonNull Context context, ArrayList<Accidente> accidentes) {
        super(context, R.layout.accidente_item, accidentes);
        this.context = context;
        this.accidentes = accidentes;
        dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.accidente_item, parent, false);
        }
        TextView tv_placa = convertView.findViewById(R.id.tv_placa);
        TextView tv_agente = convertView.findViewById(R.id.tv_agente);
        TextView tv_lugar = convertView.findViewById(R.id.tv_lugar);
        TextView tv_hora = convertView.findViewById(R.id.tv_hora);
        TextView tv_fecha = convertView.findViewById(R.id.tv_fecha);
        TextView tv_titulo = convertView.findViewById(R.id.tv_titulo);

        tv_placa.setText("Vehículo: " + accidentes.get(position).getIdVehiculo() + "");
        tv_agente.setText("Agente: " + accidentes.get(position).getIdagente() + "");
        tv_lugar.setText("Lugar: " + accidentes.get(position).getNombreLugar());
        tv_hora.setText("Hora: " + accidentes.get(position).getHora());
        tv_fecha.setText("Fecha: " + accidentes.get(position).getFecha());
        tv_titulo.setText("Titulo: " + accidentes.get(position).getTitulo());

        return convertView;
    }
}
