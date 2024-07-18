package com.example.grupo_05_tarea_16_ejercicio_01.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Zona;

import java.util.ArrayList;

public class ZonaAdapter extends RecyclerView.Adapter<ZonaAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Zona> zonas;
    private OnItemClickListener onItemClickListener;

    public ZonaAdapter(Context context, ArrayList<Zona> zonas, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.zonas = zonas !=null? zonas : new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(Zona zona);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_zona, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Zona zona = zonas.get(position);
        holder.edt_titulo_zona.setText(String.valueOf(zona.getTitulo()));
        holder.edt_departamento_zona.setText(String.valueOf(zona.getDepartamento()));
        holder.edt_provincia_zona.setText(String.valueOf(zona.getProvincia()));
        holder.edt_distrito_zona.setText(String.valueOf(zona.getDistrito()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(zona);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return zonas !=null ? zonas.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
       TextView edt_titulo_zona, edt_departamento_zona, edt_provincia_zona, edt_distrito_zona;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edt_titulo_zona = itemView.findViewById(R.id.edt_titulo_zona);
            edt_departamento_zona = itemView.findViewById(R.id.edt_departamento_zona);
            edt_provincia_zona = itemView.findViewById(R.id.edt_provincia_zona);
            edt_distrito_zona = itemView.findViewById(R.id.edt_distrito_zona);;
        }

    }
}
