package com.example.grupo_05_tarea_16_ejercicio_01.fragments.Infraccion;

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

import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.InfraccionAdapter;
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.NormaDetalleAdapter;
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.PuestoControlAdapter;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.fragments.PuestoControl.PuestoControlGrupo;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Infraccion;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.PuestoControl;

import java.util.ArrayList;

public class InfraccionFragment extends Fragment {

    DBHelper dbHelper;
    private Button btn_anadir_infraccion;
    private RecyclerView lvl_lista_infraccion;
    InfraccionAdapter adapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public InfraccionFragment() {
        // Required empty public constructor
    }

    public static InfraccionFragment newInstance(String param1, String param2) {
        InfraccionFragment fragment = new InfraccionFragment();
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
        View view = inflater.inflate(R.layout.fragment_infraccion, container, false);
        dbHelper = new DBHelper(getActivity());
        btn_anadir_infraccion = view.findViewById(R.id.btn_anadir_infraccion);
        lvl_lista_infraccion = view.findViewById(R.id.lvl_lista_infraccion);
        lvl_lista_infraccion.setLayoutManager(new LinearLayoutManager(getContext()));

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // No hacer nada para deshabilitar el botón de retroceso
            }
        });
        Listar_Infraciones();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_anadir_infraccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_infraccionFragment_to_infraccionRegisterFragment);
            }
        });
    }
    public void Listar_Infraciones() {
        ArrayList<Infraccion> infraccions = dbHelper.get_all_Infracciones();
        adapter = new InfraccionAdapter(getContext(), dbHelper, infraccions, new InfraccionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Infraccion infraccion) {
                Bundle bundle = new Bundle();
                bundle.putInt("IdInfraccion", infraccion.getIdInfraccion());
                bundle.putBoolean("modo_edicion", true);
                NavController navController = Navigation.findNavController(getView());
                navController.navigate(R.id.action_infraccionFragment_to_infraccionRegisterFragment, bundle);
            }
        });
        lvl_lista_infraccion.setAdapter(adapter);
    }
}