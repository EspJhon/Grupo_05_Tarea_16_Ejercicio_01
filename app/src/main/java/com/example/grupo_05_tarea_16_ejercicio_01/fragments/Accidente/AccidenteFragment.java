package com.example.grupo_05_tarea_16_ejercicio_01.fragments.Accidente;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.grupo_05_tarea_16_ejercicio_01.MainActivity;
import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Accidente;

import java.util.ArrayList;

public class AccidenteFragment extends Fragment {

    private ListView lv_accidentes;
    private Button btn_nuevoAccidente;
    private DBHelper dbHelper;

    public AccidenteFragment() {
        // Required empty public constructor
    }

    public static AccidenteFragment newInstance(String param1, String param2) {
        AccidenteFragment fragment = new AccidenteFragment();
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

        View view = inflater.inflate(R.layout.fragment_accidente, container, false);

        lv_accidentes=view.findViewById(R.id.lv_accidentes);
        btn_nuevoAccidente=view.findViewById(R.id.btn_nuevoAccidente);

        btn_nuevoAccidente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AgregarAccidenteActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ListarAccidentes();
    }

    public void ListarAccidentes() {
        ArrayList<Accidente> accidentes = dbHelper.get_all_Accidentes();
        ArrayAdapter<Accidente> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, accidentes);
        lv_accidentes.setAdapter(adapter);
    }

}