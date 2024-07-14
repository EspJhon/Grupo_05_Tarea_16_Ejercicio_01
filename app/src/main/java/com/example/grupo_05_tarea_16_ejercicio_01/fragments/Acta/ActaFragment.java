package com.example.grupo_05_tarea_16_ejercicio_01.fragments.Acta;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.ActaAdapter;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Acta;

import java.util.ArrayList;

public class ActaFragment extends Fragment {

    private Button btnRegistrarActa;
    private ListView lv_actas;
    private ArrayList<Acta> actaList;
    private ActaAdapter adapter;
    private DBHelper dbHelper;
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

        lv_actas = view.findViewById(R.id.lv_actas);
        btnRegistrarActa = view.findViewById(R.id.btn_RegistrarActa);

        actaList = dbHelper.getAllActas();
        adapter = new ActaAdapter(getActivity(), actaList);
        lv_actas.setAdapter(adapter);

        btnRegistrarActa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoRegistrarActa();
            }
        });

        lv_actas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mostrarOpcionesEditarEliminar(position);
            }
        });
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // No hacer nada para deshabilitar el botón de retroceso
            }
        });
        return view;
    }

    private void mostrarDialogoRegistrarActa() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_acta, null);
        builder.setView(dialogView);

        final EditText etCodigoActa = dialogView.findViewById(R.id.et_CodigoActa);
        final EditText etIdAccidente = dialogView.findViewById(R.id.et_IdAccidente);
        final EditText etIdAudiencia = dialogView.findViewById(R.id.et_IdAudiencia);
        final EditText etHora = dialogView.findViewById(R.id.et_Hora);
        final EditText etIdZona = dialogView.findViewById(R.id.et_IdZona);
        final EditText etIdAgente = dialogView.findViewById(R.id.et_IdAgente);
        final EditText etFechaActa = dialogView.findViewById(R.id.et_FechaActa);
        Button btnRegistrar = dialogView.findViewById(R.id.btn_RegistrarActa);

        final AlertDialog dialog = builder.create();

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Obtener valores de los campos
                    int codigo = Integer.parseInt(etCodigoActa.getText().toString());
                    int idAccidente = Integer.parseInt(etIdAccidente.getText().toString());
                    int idAudiencia = Integer.parseInt(etIdAudiencia.getText().toString());
                    String hora = etHora.getText().toString();
                    int idZona = Integer.parseInt(etIdZona.getText().toString());
                    int idAgente = Integer.parseInt(etIdAgente.getText().toString());
                    String fecha = etFechaActa.getText().toString();

                    // Crear objeto Acta y agregarlo a la base de datos
                    Acta acta = new Acta(codigo, idAccidente, idAudiencia, hora, idZona, idAgente, fecha);
                    dbHelper.insertarActa(acta);

                    actualizarListaActas();

                    // Cerrar el diálogo después de insertar la acta
                    Toast.makeText(requireContext(), "Acta registrada correctamente", Toast.LENGTH_SHORT).show();
                    dialog.dismiss(); // Cerrar el diálogo
                } catch (Exception e) {
                    Toast.makeText(requireContext(), "Error al registrar el Acta", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private void mostrarOpcionesEditarEliminar(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Selecciona una opción")
                .setItems(new CharSequence[]{"Editar", "Eliminar"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // Editar
                                mostrarDialogoEditarActa(position);
                                break;
                            case 1:
                                // Eliminar
                                mostrarConfirmacionEliminar(position);
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    private void mostrarDialogoEditarActa(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_acta, null);
        builder.setView(dialogView);

        final EditText etCodigoActa = dialogView.findViewById(R.id.et_CodigoActa);
        final EditText etIdAccidente = dialogView.findViewById(R.id.et_IdAccidente);
        final EditText etIdAudiencia = dialogView.findViewById(R.id.et_IdAudiencia);
        final EditText etHora = dialogView.findViewById(R.id.et_Hora);
        final EditText etIdZona = dialogView.findViewById(R.id.et_IdZona);
        final EditText etIdAgente = dialogView.findViewById(R.id.et_IdAgente);
        final EditText etFechaActa = dialogView.findViewById(R.id.et_FechaActa);
        Button btnRegistrar = dialogView.findViewById(R.id.btn_RegistrarActa);

        final Acta actaSeleccionada = actaList.get(position);

        // Llenar los campos con los datos actuales del acta seleccionada
        etCodigoActa.setText(String.valueOf(actaSeleccionada.getCodigo()));
        etIdAccidente.setText(String.valueOf(actaSeleccionada.getIdaccidente()));
        etIdAudiencia.setText(String.valueOf(actaSeleccionada.getIdAudiencia()));
        etHora.setText(actaSeleccionada.getHora());
        etIdZona.setText(String.valueOf(actaSeleccionada.getIdZona()));
        etIdAgente.setText(String.valueOf(actaSeleccionada.getIdagente()));
        etFechaActa.setText(actaSeleccionada.getFecha());

        final AlertDialog dialog = builder.create();

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Obtener valores actualizados de los campos
                    int codigo = Integer.parseInt(etCodigoActa.getText().toString());
                    int idAccidente = Integer.parseInt(etIdAccidente.getText().toString());
                    int idAudiencia = Integer.parseInt(etIdAudiencia.getText().toString());
                    String hora = etHora.getText().toString();
                    int idZona = Integer.parseInt(etIdZona.getText().toString());
                    int idAgente = Integer.parseInt(etIdAgente.getText().toString());
                    String fecha = etFechaActa.getText().toString();

                    // Actualizar el objeto Acta
                    actaSeleccionada.setCodigo(codigo);
                    actaSeleccionada.setIdaccidente(idAccidente);
                    actaSeleccionada.setIdAudiencia(idAudiencia);
                    actaSeleccionada.setHora(hora);
                    actaSeleccionada.setIdZona(idZona);
                    actaSeleccionada.setIdagente(idAgente);
                    actaSeleccionada.setFecha(fecha);

                    // Actualizar en la base de datos
                    dbHelper.actualizarActa(actaSeleccionada);

                    actualizarListaActas();

                    // Cerrar el diálogo después de actualizar el acta
                    Toast.makeText(requireContext(), "Acta actualizada correctamente", Toast.LENGTH_SHORT).show();
                    dialog.dismiss(); // Cerrar el diálogo
                } catch (Exception e) {
                    Toast.makeText(requireContext(), "Error al actualizar el Acta", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private void mostrarConfirmacionEliminar(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de eliminar este acta?")
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Eliminar el acta
                        Acta actaSeleccionada = actaList.get(position);
                        dbHelper.eliminarActa(actaSeleccionada.getIdActa());
                        actualizarListaActas();
                        Toast.makeText(requireContext(), "Acta eliminada correctamente", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .create().show();
    }

    private void actualizarListaActas() {
        actaList.clear();
        actaList.addAll(dbHelper.getAllActas());
        adapter.notifyDataSetChanged();
    }
}