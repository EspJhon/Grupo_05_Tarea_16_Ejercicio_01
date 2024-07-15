package com.example.grupo_05_tarea_16_ejercicio_01.fragments.NormaDet;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.NormaDetalleAdapter;
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.ZonaAdapter;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Agente;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.NormasDet;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Zona;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class NormaDetFragment extends Fragment {

    DBHelper dbHelper;
    private Button btn_anadir_norma;
    private RecyclerView lvl_lista_norma;
    private NormaDetalleAdapter adapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public NormaDetFragment() {
        // Required empty public constructor
    }

    public static NormaDetFragment newInstance(String param1, String param2) {
        NormaDetFragment fragment = new NormaDetFragment();
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
        View view = inflater.inflate(R.layout.fragment_norma_det, container, false);
        dbHelper = new DBHelper(getActivity());
        btn_anadir_norma = view.findViewById(R.id.btn_anadir_norma);
        lvl_lista_norma = view.findViewById(R.id.lvl_lista_norma);
        lvl_lista_norma.setLayoutManager(new LinearLayoutManager(getContext()));
        Listar_Normas();
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
        btn_anadir_norma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_Norma(1, null);
            }
        });
    }

    public void Listar_Normas() {
        ArrayList<NormasDet> normasDets = dbHelper.get_all_Normas_Detalle();
        adapter = new NormaDetalleAdapter(getActivity(), normasDets, new NormaDetalleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NormasDet normasDet) {
                Dialog_Norma(2, normasDet);
            }
        });
        lvl_lista_norma.setAdapter(adapter);
    }

    public void Dialog_Norma(int tipo, NormasDet normasDet) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_norma, null);

        final EditText n_norma = dialogView.findViewById(R.id.edt_register_numero_norma);
        final EditText descripcion = dialogView.findViewById(R.id.edt_register_descripcion_norma);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        if (tipo == 1 && normasDet == null) {
            builder.setTitle("Añadir Norma");
            builder.setView(dialogView);
            // Set up the buttons
            builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String cnnorma = n_norma.getText().toString();
                    String cdescripcion = descripcion.getText().toString();
                    NormasDet normasDet = new NormasDet(cnnorma, cdescripcion);
                    dbHelper.Insertar_Normas_Detalle(normasDet);
                    Listar_Normas();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        } else if (tipo == 2 && normasDet != null) {
            builder.setTitle("Actualizar Norma");
            builder.setView(dialogView);
            n_norma.setText(String.valueOf(normasDet.getNumnorma()));
            descripcion.setText(String.valueOf(normasDet.getDescripcion()));
            int IdNorma = normasDet.getIdnomra();
            builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String cnnorma = n_norma.getText().toString();
                    String cdescripcion = descripcion.getText().toString();
                    NormasDet normasDet = dbHelper.get_Norma_Detalle(IdNorma);
                    if (normasDet != null) {
                        normasDet.setNumnorma(cnnorma);
                        normasDet.setDescripcion(cdescripcion);
                        dbHelper.Actualizar_Norma_Detalle(normasDet);
                        normasDet = null;
                        Toast.makeText(getContext(), "Norma Actualizada", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Norma no Actualizada", Toast.LENGTH_SHORT).show();
                    }
                    Listar_Normas();
                }
            });
            builder.setNegativeButton("Eliminar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setTitle("Confirmar Eliminación")
                            .setMessage("¿Estás seguro de eliminar este Norma?")
                            .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    NormasDet normasDet = dbHelper.get_Norma_Detalle(IdNorma);
                                    dbHelper.Eliminar_Norma_Detalle(normasDet);
                                    Listar_Normas();

                                    Toast.makeText(requireContext(), "Norma eliminado correctamente", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Cancelar", null)
                            .create().show();
                }
            });
        }

        builder.show();
    }
}