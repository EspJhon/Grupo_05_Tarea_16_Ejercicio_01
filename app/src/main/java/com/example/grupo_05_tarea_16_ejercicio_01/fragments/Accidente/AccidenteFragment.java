package com.example.grupo_05_tarea_16_ejercicio_01.fragments.Accidente;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.grupo_05_tarea_16_ejercicio_01.MainActivity;
import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.AccidenteAdapter;
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

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btn_nuevoAccidente).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_accidenteFragment_to_agregarAccidenteFragment);
            }
        });

        lv_accidentes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Accidente accidente = (Accidente) parent.getItemAtPosition(position);
                OpcionesDialog(accidente);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        ListarAccidentes();
    }

    public void ListarAccidentes() {
        ArrayList<Accidente> accidentes = dbHelper.get_all_Accidentes();
        AccidenteAdapter adapter = new AccidenteAdapter(getActivity(),accidentes);
        lv_accidentes.setAdapter(adapter);
    }

    public void OpcionesDialog(Accidente accidente){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Seleccione una opción")
                .setItems(new String[]{"Editar", "Eliminar"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("id", accidente.getIdaccidente());
                                NavController navController = Navigation.findNavController(getView());
                                navController.navigate(R.id.action_accidenteFragment_to_actualizarAccidenteFragment,bundle);
                                break;
                            case 1:
                                dbHelper.Eliminar_Accidente(accidente);
                                ListarAccidentes();
                                break;
                        }
                    }
                });
        builder.create().show();
    }

}