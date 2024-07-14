package com.example.grupo_05_tarea_16_ejercicio_01.fragments.Audiencia;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.AudienciaAdapter;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Audiencia;

import java.util.ArrayList;

public class AudienciaFragment extends Fragment {

    private ListView lv_audiencias;
    private Button btn_nuevaAudiencia;
    private DBHelper dbHelper;

    public AudienciaFragment() {
        // Required empty public constructor
    }


    public static AudienciaFragment newInstance(String param1, String param2) {
        AudienciaFragment fragment = new AudienciaFragment();
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

        dbHelper = new DBHelper(getActivity());

        View view = inflater.inflate(R.layout.fragment_audiencia, container, false);

        lv_audiencias=view.findViewById(R.id.lv_audiencias);
        btn_nuevaAudiencia=view.findViewById(R.id.btn_nuevaAudiencia);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btn_nuevaAudiencia).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_audienciaFragment_to_agregarAudienciaFragment);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        ListarAudiencias();
    }

    public void ListarAudiencias() {
        ArrayList<Audiencia> audiencias = dbHelper.get_all_Audiencias();
        AudienciaAdapter adapter = new AudienciaAdapter(getActivity(),audiencias);
        lv_audiencias.setAdapter(adapter);
    }

}