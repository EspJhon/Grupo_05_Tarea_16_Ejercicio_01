package com.example.grupo_05_tarea_16_ejercicio_01.fragments.Zona;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.ZonaAdapter;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Zona;

import java.util.ArrayList;


public class ZonaFragment extends Fragment {

    private RecyclerView lvl_lista_zona;
    private ZonaAdapter adapter;
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
        dbHelper = new DBHelper(getActivity());
        lvl_lista_zona = view.findViewById(R.id.lvl_lista_zona);
        lvl_lista_zona.setLayoutManager(new LinearLayoutManager(getContext()));
        Listar_Zonas();
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // No hacer nada para deshabilitar el botón de retroceso
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DBHelper(getActivity());
        view.findViewById(R.id.btn_agregar_zona).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_zonaFragment_to_mapsZonaFragment);
            }
        });
    }
    public void Listar_Zonas() {
        ArrayList<Zona> zonas = dbHelper.get_all_Zonas();
        adapter = new ZonaAdapter(getActivity(), zonas, new ZonaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Zona zona) {
                Bundle bundle = new Bundle();
                bundle.putString("ztitulo", zona.getTitulo());
                bundle.putString("zlatitud", zona.getLatitud());
                bundle.putString("zlongitud", zona.getLongitud());
                NavController navController = Navigation.findNavController(getView());
                navController.navigate(R.id.action_zonaFragment_to_actualizarZonaFragment, bundle);
            }
        });
        lvl_lista_zona.setAdapter(adapter);
    }
}