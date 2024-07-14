package com.example.grupo_05_tarea_16_ejercicio_01.fragments.Agente;

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
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.AgenteAdapter;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Acta;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Agente;

import java.util.ArrayList;


public class AgenteFragment extends Fragment {

    private Button btnRegistrarAgente;
    private ListView lvAgentes;
    private ArrayList<Agente> agenteList;
    private AgenteAdapter adapter;
    private DBHelper dbHelper;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public AgenteFragment() {
        // Required empty public constructor
    }

    public static AgenteFragment newInstance(String param1, String param2) {
        AgenteFragment fragment = new AgenteFragment();
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
        View view = inflater.inflate(R.layout.fragment_agente, container, false);

        dbHelper = new DBHelper(requireContext());

        lvAgentes = view.findViewById(R.id.lv_agentes);
        btnRegistrarAgente = view.findViewById(R.id.btn_RegistrarAgente);

        // Inicializar la lista de agentes desde la base de datos
        agenteList = dbHelper.getAllAgentes();

        // Inicializar el adaptador con la lista de agentes
        adapter = new AgenteAdapter(requireContext(), agenteList);

        // Establecer el adaptador en el ListView
        lvAgentes.setAdapter(adapter);

        // Configurar el botón para registrar un nuevo agente
        btnRegistrarAgente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoRegistrarAgente();
            }
        });

        // Configurar la acción al hacer clic en un elemento del ListView
        lvAgentes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
    private void mostrarDialogoRegistrarAgente() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_agente, null);
        builder.setView(dialogView);

        final EditText etCedula = dialogView.findViewById(R.id.et_Cedula);
        final EditText etNombre = dialogView.findViewById(R.id.et_NombreAgente);
        final EditText etIdPuestoControl = dialogView.findViewById(R.id.et_IdPuestoControl);
        final EditText etRango = dialogView.findViewById(R.id.et_Rango);

        builder.setPositiveButton("Registrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    // Obtener valores de los campos
                    int cedula = Integer.parseInt(etCedula.getText().toString());
                    String nombre = etNombre.getText().toString();
                    int idPuestoControl = Integer.parseInt(etIdPuestoControl.getText().toString());
                    String rango = etRango.getText().toString();

                    // Validar que los campos requeridos no estén vacíos
                    if (nombre.isEmpty()) {
                        Toast.makeText(requireContext(), "Nombre del agente es requerido", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Crear objeto Agente y agregarlo a la base de datos
                    Agente agente = new Agente(cedula, nombre, idPuestoControl, rango);
                    dbHelper.insertarAgente(agente);

                    // Actualizar la lista de agentes
                    actualizarListaAgentes();

                    // Cerrar el diálogo después de insertar el agente
                    Toast.makeText(requireContext(), "Agente registrado correctamente", Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(requireContext(), "Error: Ingresa valores numéricos válidos", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(requireContext(), "Error al registrar el Agente", Toast.LENGTH_SHORT).show();
                    e.printStackTrace(); // Añade esto para ver el error detallado en LogCat
                }
            }
        });

        builder.setNegativeButton("Cancelar", null);

        builder.create().show();
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
                                mostrarDialogoEditarAgente(position);
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

    private void mostrarDialogoEditarAgente(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_agente, null);
        builder.setView(dialogView);

        final EditText etCedula = dialogView.findViewById(R.id.et_Cedula);
        final EditText etNombre = dialogView.findViewById(R.id.et_NombreAgente);
        final EditText etIdPuestoControl = dialogView.findViewById(R.id.et_IdPuestoControl);
        final EditText etRango = dialogView.findViewById(R.id.et_Rango);

        final Agente agenteSeleccionado = agenteList.get(position);

        // Llenar los campos con los datos actuales del agente seleccionado
        etCedula.setText(String.valueOf(agenteSeleccionado.getCedulaa()));
        etNombre.setText(agenteSeleccionado.getNombre());
        etIdPuestoControl.setText(String.valueOf(agenteSeleccionado.getIdPuestoControl()));
        etRango.setText(agenteSeleccionado.getRango());

        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    // Obtener valores actualizados de los campos
                    int cedula = Integer.parseInt(etCedula.getText().toString());
                    String nombre = etNombre.getText().toString();
                    int idPuestoControl = Integer.parseInt(etIdPuestoControl.getText().toString());
                    String rango = etRango.getText().toString();

                    // Actualizar el objeto Agente
                    agenteSeleccionado.setCedulaa(cedula);
                    agenteSeleccionado.setNombre(nombre);
                    agenteSeleccionado.setIdPuestoControl(idPuestoControl);
                    agenteSeleccionado.setRango(rango);

                    // Actualizar en la base de datos
                    dbHelper.actualizarAgente(agenteSeleccionado);

                    // Actualizar la lista de agentes
                    actualizarListaAgentes();

                    // Cerrar el diálogo después de actualizar el agente
                    Toast.makeText(requireContext(), "Agente actualizado correctamente", Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(requireContext(), "Error: Ingresa valores numéricos válidos", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(requireContext(), "Error al actualizar el Agente", Toast.LENGTH_SHORT).show();
                    e.printStackTrace(); // Añade esto para ver el error detallado en LogCat
                }
            }
        });

        builder.setNegativeButton("Cancelar", null);

        builder.create().show();
    }

    private void mostrarConfirmacionEliminar(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de eliminar este agente?")
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Eliminar el agente
                        Agente agenteSeleccionado = agenteList.get(position);
                        dbHelper.eliminarAgente(agenteSeleccionado.getIdagente());

                        // Actualizar la lista de agentes
                        actualizarListaAgentes();

                        Toast.makeText(requireContext(), "Agente eliminado correctamente", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .create().show();
    }

    private void actualizarListaAgentes() {
        // Actualizar la lista de agentes desde la base de datos
        agenteList.clear();
        agenteList.addAll(dbHelper.getAllAgentes());
        adapter.notifyDataSetChanged();
    }
}