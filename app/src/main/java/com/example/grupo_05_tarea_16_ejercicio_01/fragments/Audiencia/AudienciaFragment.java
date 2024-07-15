package com.example.grupo_05_tarea_16_ejercicio_01.fragments.Audiencia;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.AudienciaAdapter;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Audiencia;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.NormasDet;

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
        ListarAudiencias();
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
        dbHelper = new DBHelper(getContext());
        view.findViewById(R.id.btn_nuevaAudiencia).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_Audiencia(1,null);
            }
        });
        lv_audiencias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Audiencia paudiencia = (Audiencia) parent.getItemAtPosition(position);
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View dialogView = inflater.inflate(R.layout.dialog_audiencia, null);

                final EditText et_lugar = dialogView.findViewById(R.id.et_lugar);
                final EditText et_fecha = dialogView.findViewById(R.id.et_fecha);
                final EditText et_hora = dialogView.findViewById(R.id.et_hora);
                final EditText et_codigo = dialogView.findViewById(R.id.et_codigo);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Actualizar Norma");
                builder.setView(dialogView);

                et_lugar.setText(paudiencia.getLugar());
                et_fecha.setText(paudiencia.getFecha());
                et_hora.setText(paudiencia.getHora());
                et_codigo.setText(String.valueOf(paudiencia.getCodigo()));
                builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int codigo = Integer.parseInt(et_codigo.getText().toString().trim());
                        String lugar = et_lugar.getText().toString().trim();
                        String fecha = et_fecha.getText().toString().trim();
                        String hora = et_hora.getText().toString().trim();

                        Audiencia audiencia = dbHelper.get_Audiencia(paudiencia.getIdAudiencia());
                        if (audiencia != null) {
                            audiencia.setCodigo(codigo);
                            audiencia.setLugar(lugar);
                            audiencia.setFecha(fecha);
                            audiencia.setHora(hora);
                            dbHelper.Actualizar_Audiencia(audiencia);
                            audiencia = null;
                            Toast.makeText(getContext(), "Audiencia Actualizada", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Audiencia no Actualizada", Toast.LENGTH_SHORT).show();
                        }
                        ListarAudiencias();
                    }
                });
                builder.setNegativeButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                        builder.setTitle("Confirmar Eliminación")
                                .setMessage("¿Estás seguro de eliminar este Audiencia?")
                                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Audiencia audiencia = dbHelper.get_Audiencia(paudiencia.getIdAudiencia());
                                        dbHelper.Eliminar_Audiencia(audiencia);
                                        ListarAudiencias();

                                        Toast.makeText(requireContext(), "Audiencia eliminado correctamente", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton("Cancelar", null)
                                .create().show();
                    }
                });
                builder.show();
            }
        });
    }

    public void Dialog_Audiencia(int tipo, Audiencia audiencia) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_audiencia, null);

        final EditText et_lugar = dialogView.findViewById(R.id.et_lugar);
        final EditText et_fecha = dialogView.findViewById(R.id.et_fecha);
        final EditText et_hora = dialogView.findViewById(R.id.et_hora);
        final EditText et_codigo = dialogView.findViewById(R.id.et_codigo);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            builder.setTitle("Añadir Norma");
            builder.setView(dialogView);
            // Set up the buttons
            builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int codigo = Integer.parseInt(et_codigo.getText().toString().trim());
                    String lugar = et_lugar.getText().toString().trim();
                    String fecha = et_fecha.getText().toString().trim();
                    String hora = et_hora.getText().toString().trim();

                    Audiencia audiencia = new Audiencia(codigo,lugar,fecha,hora);
                    dbHelper.Insertar_Audiencia(audiencia);
                    ListarAudiencias();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });



        builder.show();
    }

    public void ListarAudiencias() {
        ArrayList<Audiencia> audiencias = dbHelper.get_all_Audiencias();
        AudienciaAdapter adapter = new AudienciaAdapter(getActivity(),audiencias);
        lv_audiencias.setAdapter(adapter);
    }

}