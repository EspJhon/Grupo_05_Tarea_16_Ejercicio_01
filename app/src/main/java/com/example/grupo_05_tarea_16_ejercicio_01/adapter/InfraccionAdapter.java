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
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Agente;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Infraccion;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Vehiculo;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Zona;

import java.util.ArrayList;

public class InfraccionAdapter extends RecyclerView.Adapter<InfraccionAdapter.ViewHolder>{
    private Context context;
    private DBHelper dbHelper;
    private ArrayList<Infraccion> infraccions;
    private OnItemClickListener onItemClickListener;

    public InfraccionAdapter(Context context, DBHelper dbHelper, ArrayList<Infraccion> infraccions, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.dbHelper = dbHelper;
        this.infraccions = infraccions;
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(Infraccion infraccion);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_infraccion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Infraccion infraccion = infraccions.get(position);
        Agente agente = dbHelper.get_Agente(infraccion.getIdagente());
        Vehiculo vehiculo = dbHelper.get_Vehiculo(infraccion.getIdVehiculo());
        holder.edt_numero_placa_infraccion.setText(String.valueOf(vehiculo.getNumplaca()));
        holder.edt_multa_infraccion.setText(infraccion.getValormulta());
        holder.edt_fecha_infraccion.setText(infraccion.getFecha());
        holder.edt_agente_infraccion.setText(agente.getNombre());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(infraccion);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return infraccions  !=null ? infraccions .size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView edt_agente_infraccion, edt_numero_placa_infraccion, edt_multa_infraccion, edt_fecha_infraccion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edt_agente_infraccion = itemView.findViewById(R.id.edt_agente_infraccion);
            edt_numero_placa_infraccion = itemView.findViewById(R.id.edt_numero_placa_infraccion);
            edt_multa_infraccion = itemView.findViewById(R.id.edt_multa_infraccion);
            edt_fecha_infraccion = itemView.findViewById(R.id.edt_fecha_infraccion);
        }

    }
}
