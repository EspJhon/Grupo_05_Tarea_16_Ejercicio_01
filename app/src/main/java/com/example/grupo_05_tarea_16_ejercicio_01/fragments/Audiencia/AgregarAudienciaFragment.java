package com.example.grupo_05_tarea_16_ejercicio_01.fragments.Audiencia;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Audiencia;
import com.google.android.gms.maps.OnMapReadyCallback;

public class AgregarAudienciaFragment extends Fragment {

    private EditText et_lugar, et_fecha, et_hora, et_codigo;
    private Button btn_agregarAudiencia;
    private DBHelper dbHelper;

    public AgregarAudienciaFragment() {
        // Required empty public constructor
    }

    public static AgregarAudienciaFragment newInstance(String param1, String param2) {
        AgregarAudienciaFragment fragment = new AgregarAudienciaFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agregar_audiencia, container, false);

        dbHelper = new DBHelper(getActivity());

        et_lugar = view.findViewById(R.id.et_lugar);
        et_fecha = view.findViewById(R.id.et_fecha);
        et_hora = view.findViewById(R.id.et_hora);
        et_codigo = view.findViewById(R.id.et_codigo);
        btn_agregarAudiencia = view.findViewById(R.id.btn_agregarAudiencia);

        btn_agregarAudiencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarAudiencia();
            }
        });

        return view;
    }

    public void AgregarAudiencia(){
        int codigo = Integer.parseInt(et_codigo.getText().toString().trim());
        String lugar = et_lugar.getText().toString().trim();
        String fecha = et_fecha.getText().toString().trim();
        String hora = et_hora.getText().toString().trim();

        Audiencia audiencia = new Audiencia(codigo,lugar,fecha,hora);
        dbHelper.Insertar_Audiencia(audiencia);

        requireActivity().getSupportFragmentManager().popBackStack();
    }

}