package com.example.grupo_05_tarea_16_ejercicio_01.fragments.OficinaGob;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.AccidenteAdapter;
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.OficinaAdapter;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Accidente;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.OficinaGob;

import java.util.ArrayList;

public class OficinaGobFragment extends Fragment {

    private ListView lv_oficinas;
    private Button btn_nuevaOficina;
    private DBHelper dbHelper;

    public OficinaGobFragment() {
        // Required empty public constructor
    }

    public static OficinaGobFragment newInstance(String param1, String param2) {
        OficinaGobFragment fragment = new OficinaGobFragment();
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
        View view = inflater.inflate(R.layout.fragment_oficina_gob, container, false);

        dbHelper=new DBHelper(getActivity());

        lv_oficinas=view.findViewById(R.id.lv_oficinas);
        btn_nuevaOficina=view.findViewById(R.id.btn_nuevaOficina);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btn_nuevaOficina).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_oficinaGobFragment_to_agregarOficinaFragment);
            }
        });

        lv_oficinas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OficinaGob oficinaGob = (OficinaGob) parent.getItemAtPosition(position);
                OpcionesDialog(oficinaGob);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        ListarOficinas();
    }

    public void ListarOficinas() {
        ArrayList<OficinaGob> oficinas = dbHelper.get_all_Oficinas();
        OficinaAdapter adapter = new OficinaAdapter(getActivity(),oficinas);
        lv_oficinas.setAdapter(adapter);
    }

    public void OpcionesDialog(OficinaGob oficinaGob){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Seleccione una opción")
                .setItems(new String[]{"Editar", "Eliminar"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("id", oficinaGob.getIdOficina());
                                NavController navController = Navigation.findNavController(getView());
                                navController.navigate(R.id.action_oficinaGobFragment_to_actualizarOficinaFragment,bundle);
                                break;
                            case 1:
                                dbHelper.Eliminar_Oficina(oficinaGob);
                                ListarOficinas();
                                break;
                        }
                    }
                });
        builder.create().show();
    }

}