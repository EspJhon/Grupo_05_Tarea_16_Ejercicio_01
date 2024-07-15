package com.example.grupo_05_tarea_16_ejercicio_01.fragments.Acta;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.ActaAdapter;
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.InfraccionAdapter;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Acta;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Infraccion;

import java.util.ArrayList;

public class ActaFragment extends Fragment {

    TextView prueba;
    private Button btnRegistrarActa;
    private RecyclerView lv_listas_actas;
    ActaAdapter adapter;
    DBHelper dbHelper;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ActaFragment() {
        // Required empty public constructor
    }

    public static ActaFragment newInstance(String param1, String param2) {
        ActaFragment fragment = new ActaFragment();
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
        View view = inflater.inflate(R.layout.fragment_acta, container, false);

        dbHelper = new DBHelper(getActivity());
        lv_listas_actas = view.findViewById(R.id.lv_listas_actas);
        btnRegistrarActa = view.findViewById(R.id.btn_RegistrarActa);
        lv_listas_actas.setLayoutManager(new LinearLayoutManager(getContext()));
        prueba = view.findViewById(R.id.tv_prueva);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // No hacer nada para deshabilitar el botón de retroceso
            }
        });
        Listar_Actas();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DBHelper(getContext());
        btnRegistrarActa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_actaFragment_to_actaRegisterFragment);
            }
        });
    }

    public void Listar_Actas() {
        ArrayList<Acta> actas = dbHelper.getAllActas();
        //prueba.setText(String.valueOf(actas.get(1).getFecha()));
        adapter = new ActaAdapter(getContext(), dbHelper, actas, new ActaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Acta acta) {
                Bundle bundle = new Bundle();
                bundle.putInt("IdInfraccion", acta.getIdActa());
                bundle.putBoolean("modo_edicion", true);
                NavController navController = Navigation.findNavController(getView());
                navController.navigate(R.id.action_actaFragment_to_actaRegisterFragment, bundle);
            }
        });
        lv_listas_actas.setAdapter(adapter);
    }
}