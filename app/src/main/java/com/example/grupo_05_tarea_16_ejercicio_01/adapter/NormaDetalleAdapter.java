package com.example.grupo_05_tarea_16_ejercicio_01.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.NormasDet;

import java.util.ArrayList;

public class NormaDetalleAdapter extends RecyclerView.Adapter<NormaDetalleAdapter.ViewHolder>{
    private Context context;
    private ArrayList<NormasDet> normasDets;
    private OnItemClickListener onItemClickListener;

    public NormaDetalleAdapter(Context context, ArrayList<NormasDet> normasDets, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.normasDets = normasDets;
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(NormasDet normasDet);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_norma, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NormasDet normasDet = normasDets.get(position);
        holder.edt_numero_norma.setText(String.valueOf(normasDet.getNumnorma()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(normasDet);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return normasDets  !=null ? normasDets .size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        EditText edt_numero_norma;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edt_numero_norma = itemView.findViewById(R.id.edt_numero_norma);
        }

    }
}
