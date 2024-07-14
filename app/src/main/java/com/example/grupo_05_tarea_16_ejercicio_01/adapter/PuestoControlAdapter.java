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
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.fragments.PuestoControl.PuestoControlGrupo;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.PuestoControl;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Zona;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PuestoControlAdapter extends RecyclerView.Adapter<PuestoControlAdapter.ViewHolder>{
    private Context context;
    private DBHelper dbHelper;
    private ArrayList<PuestoControlGrupo> puestoControlGroupedList;
    private OnItemClickListener onItemClickListener;

    public PuestoControlAdapter(Context context, DBHelper dbHelper, ArrayList<PuestoControlGrupo> puestoControlGroupedList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.dbHelper = dbHelper;
        this.puestoControlGroupedList = puestoControlGroupedList;
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(PuestoControlGrupo puestoControlGrouped);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_puesto_control, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PuestoControlGrupo puestoControlGrouped = puestoControlGroupedList.get(position);
        Zona zona = dbHelper.get_Zona_Puesto(puestoControlGrouped.getIdZona());

        holder.edt_zona_puesto_control.setText(zona.getTitulo());
        holder.txt_total_puestos.setText(String.valueOf(puestoControlGrouped.getTotalPuestos()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(puestoControlGrouped);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return puestoControlGroupedList  !=null ? puestoControlGroupedList .size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        EditText edt_zona_puesto_control;
        TextView txt_total_puestos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edt_zona_puesto_control = itemView.findViewById(R.id.edt_zona_puesto_control);
            txt_total_puestos = itemView.findViewById(R.id.txt_total_puestos);
        }

    }

}
