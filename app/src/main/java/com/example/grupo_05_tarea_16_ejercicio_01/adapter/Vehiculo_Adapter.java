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
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Vehiculo;

import java.util.ArrayList;
import java.util.HashMap;

public class Vehiculo_Adapter extends ArrayAdapter<Vehiculo> {

    private ArrayList<Vehiculo> vehiculos;
    private Context context;
    private OnItemLongClickListener onItemLongClickListener;
    private HashMap<Integer, String> propietariosMap; // HashMap para almacenar ID de propietario y nombre

    public Vehiculo_Adapter(@NonNull Context context, ArrayList<Vehiculo> vehiculos) {
        super(context, 0, vehiculos);
        this.context = context;
        this.vehiculos = vehiculos;
        this.propietariosMap = new HashMap<>();
    }

    public void setPropietariosMap(HashMap<Integer, String> propietariosMap) {
        this.propietariosMap = propietariosMap;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Vehiculo vehiculo = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.vehiculo_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvNumPlaca = convertView.findViewById(R.id.tv_numPlaca);
            viewHolder.tvMarca = convertView.findViewById(R.id.tv_marca);
            viewHolder.tvModelo = convertView.findViewById(R.id.tv_modelo);
            viewHolder.tvMotor = convertView.findViewById(R.id.tv_motor);
            viewHolder.tvFecha = convertView.findViewById(R.id.tv_fecha);
            viewHolder.tvPropietario = convertView.findViewById(R.id.tv_propietario);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvNumPlaca.setText(String.valueOf(vehiculo.getNumplaca()));
        viewHolder.tvMarca.setText(String.valueOf(vehiculo.getMarca()));
        viewHolder.tvModelo.setText(vehiculo.getModelo());
        viewHolder.tvMotor.setText(vehiculo.getMotor());
        viewHolder.tvFecha.setText(vehiculo.getF_ano());
        viewHolder.tvPropietario.setText(String.valueOf(vehiculo.getIdPropietario()));


        // Obtener el nombre del propietario a partir del HashMap
        String nombrePropietario = propietariosMap.get(vehiculo.getIdPropietario());
        viewHolder.tvPropietario.setText(nombrePropietario != null ? nombrePropietario : String.valueOf(vehiculo.getIdPropietario()));


        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClick(vehiculo);
                    return true;
                }
                return false;
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView tvNumPlaca;
        TextView tvMarca;
        TextView tvModelo;
        TextView tvMotor;
        TextView tvFecha;
        TextView tvPropietario;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Vehiculo vehiculo);
    }
}
