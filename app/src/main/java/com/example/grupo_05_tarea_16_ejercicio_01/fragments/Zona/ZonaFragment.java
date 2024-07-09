package com.example.grupo_05_tarea_16_ejercicio_01.fragments.Zona;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Zona;


public class ZonaFragment extends Fragment {

    private EditText edt_ubicacion_zona;
    private Button btn_registrar_zona;
    DBHelper dbHelper;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ZonaFragment() {
        // Required empty public constructor
    }

    public static ZonaFragment newInstance(String param1, String param2) {
        ZonaFragment fragment = new ZonaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_zona, container, false);
        edt_ubicacion_zona = view.findViewById(R.id.edt_ubicacion_zon);
        btn_registrar_zona = view.findViewById(R.id.btn_registrar_zona);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DBHelper(getActivity());
        btn_registrar_zona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ubi = edt_ubicacion_zona.getText().toString();
                Zona zona = new Zona(ubi);
                dbHelper.Insertar_Zonas(zona);
            }
        });
    }
}