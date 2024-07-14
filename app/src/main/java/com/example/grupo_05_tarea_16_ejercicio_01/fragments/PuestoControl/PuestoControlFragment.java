package com.example.grupo_05_tarea_16_ejercicio_01.fragments.PuestoControl;

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

import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.PuestoControlAdapter;
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.ZonaAdapter;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.PuestoControl;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Zona;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PuestoControlFragment extends Fragment {

    private RecyclerView lvl_lista_puesto_control;
    DBHelper dbHelper;
    PuestoControlAdapter adapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public PuestoControlFragment() {
        // Required empty public constructor
    }

    public static PuestoControlFragment newInstance(String param1, String param2) {
        PuestoControlFragment fragment = new PuestoControlFragment();
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
        View view = inflater.inflate(R.layout.fragment_puesto_control, container, false);
        dbHelper = new DBHelper(getActivity());
        lvl_lista_puesto_control = view.findViewById(R.id.lvl_lista_puesto_control);
        lvl_lista_puesto_control.setLayoutManager(new LinearLayoutManager(getContext()));
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // No hacer nada para deshabilitar el botón de retroceso
            }
        });
        Listar_Puesto_Controls();
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_agregar_puesto_control).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_puestoControlFragment_to_registerPuestoControlFragment);
            }
        });
    }

    public void Listar_Puesto_Controls() {
        ArrayList<PuestoControl> puestoControls = dbHelper.get_all_Puesto_Controls();
        ArrayList<PuestoControlGrupo> groupedList = groupPuestoControls(puestoControls);
        adapter = new PuestoControlAdapter(getActivity(), dbHelper, groupedList, new PuestoControlAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PuestoControlGrupo puestoControlGrouped) {
                Bundle bundle = new Bundle();
                bundle.putString("IdZona", puestoControlGrouped.getIdZona());
                bundle.putBoolean("modo_edicion", true);
                NavController navController = Navigation.findNavController(getView());
                navController.navigate(R.id.action_puestoControlFragment_to_registerPuestoControlFragment, bundle);
            }
        });
        lvl_lista_puesto_control.setAdapter(adapter);
    }

    public ArrayList<PuestoControlGrupo> groupPuestoControls(ArrayList<PuestoControl> puestoControls) {
        Map<String, Integer> groupedMap = new HashMap<>();

        for (PuestoControl puestoControl : puestoControls) {
            String idZona = String.valueOf(puestoControl.getIdZona());
            groupedMap.put(idZona, groupedMap.getOrDefault(idZona, 0) + 1);
        }

        ArrayList<PuestoControlGrupo> groupedList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : groupedMap.entrySet()) {
            groupedList.add(new PuestoControlGrupo(entry.getKey(), entry.getValue()));
        }

        return groupedList;
    }
}