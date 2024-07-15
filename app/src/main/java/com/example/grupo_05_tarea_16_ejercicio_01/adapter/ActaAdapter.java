package com.example.grupo_05_tarea_16_ejercicio_01.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Accidente;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Acta;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Agente;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Infraccion;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Vehiculo;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Zona;

import java.util.ArrayList;

public class ActaAdapter extends RecyclerView.Adapter<ActaAdapter.ViewHolder>{
    private Context context;
    private DBHelper dbHelper;
    private ArrayList<Acta> actas;
    private OnItemClickListener onItemClickListener;

    public ActaAdapter(Context context, DBHelper dbHelper, ArrayList<Acta> actas, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.dbHelper = dbHelper;
        this.actas = actas;
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(Acta acta);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_acta, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Acta acta = actas.get(position);
        Agente agente = dbHelper.get_Agente(acta.getIdagente());
        Accidente accidente = dbHelper.get_Accidente(acta.getIdaccidente());
        Zona zona = dbHelper.get_Zona_Puesto(String.valueOf(acta.getIdZona()));
        holder.edt_accidente_acta.setText(String.valueOf(accidente.getTitulo()));
        holder.edt_fecha_acta.setText(acta.getFecha());
        holder.edt_agente_acta.setText(agente.getNombre());
        holder.edt_zona_acta.setText(zona.getTitulo());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(acta);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return actas  !=null ? actas .size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView edt_agente_acta, edt_accidente_acta, edt_fecha_acta, edt_zona_acta;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edt_agente_acta = itemView.findViewById(R.id.tv_agente_acta);
            edt_accidente_acta = itemView.findViewById(R.id.tv_accidente_acta);
            edt_fecha_acta = itemView.findViewById(R.id.tv_fecha_acta);
            edt_zona_acta = itemView.findViewById(R.id.tv_zona_acta);
        }

    }

}
