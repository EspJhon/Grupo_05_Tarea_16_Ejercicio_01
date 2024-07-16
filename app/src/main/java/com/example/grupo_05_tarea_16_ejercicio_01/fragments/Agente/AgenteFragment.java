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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.AgenteAdapter;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Acta;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Agente;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.NormasDet;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.PuestoControl;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Vehiculo;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Zona;

import java.util.ArrayList;


public class AgenteFragment extends Fragment {

    private Button btnRegistrarAgente;
    private ListView lvAgentes;
    private ArrayList<Agente> agenteList;
    private AgenteAdapter adapter;
    private DBHelper dbHelper;
    private int IdZona, IdPuestoControl;
    ArrayAdapter<String> adapter_zona;
    ArrayAdapter<String> adapter_puesto_control;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
                ArrayList<PuestoControl> prueba = dbHelper.get_all_Puesto_Controls();
                if (prueba.isEmpty()) {
                    Toast.makeText(getContext(), "No Existe Puesto de Control", Toast.LENGTH_SHORT).show();
                } else {
                    mostrarDialogoRegistrarEditarAgente(null);
                }
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

    private void mostrarDialogoRegistrarEditarAgente(Agente agente) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_agente, null);
        builder.setView(dialogView);
        dbHelper = new DBHelper(requireContext());

        final EditText etCedula = dialogView.findViewById(R.id.et_Cedula);
        final EditText etNombre = dialogView.findViewById(R.id.et_NombreAgente);
        final Spinner sp_IdPuestoControl_agente = dialogView.findViewById(R.id.sp_IdPuestoControl_agente);
        final Spinner sp_Zona_agente = dialogView.findViewById(R.id.sp_Zona_agente);
        final EditText etRango = dialogView.findViewById(R.id.et_Rango);

        ArrayList<Zona> zonas = dbHelper.get_all_Zonas();
        ArrayList<String> tituloZonas = new ArrayList<>();
        if (zonas != null) {
            for (Zona zona : zonas) {
                tituloZonas.add(zona.getTitulo());
            }
        }
        adapter_zona = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tituloZonas);
        adapter_zona.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_Zona_agente.setAdapter(adapter_zona);

        ArrayList<PuestoControl> puestoControls = dbHelper.get_all_Puesto_Controls();
        adapter_puesto_control = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new ArrayList<>());
        adapter_puesto_control.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_IdPuestoControl_agente.setAdapter(adapter_puesto_control);

        sp_Zona_agente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Zona zona = zonas.get(position);
                IdZona = zona.getIdZona();

                ArrayList<String> filteredPuestoControlTitles = new ArrayList<>();
                for (PuestoControl puestoControl : puestoControls) {
                    if (puestoControl.getIdZona() == IdZona) {
                        filteredPuestoControlTitles.add(puestoControl.getTitulo());
                    }
                }
                adapter_puesto_control.clear();
                adapter_puesto_control.addAll(filteredPuestoControlTitles);
                adapter_puesto_control.notifyDataSetChanged();
                if (agente != null) {
                    PuestoControl puestoControl = dbHelper.get_Puesto_Control(agente.getIdPuestoControl());
                    int spinnerPositionPuestoControl = adapter_puesto_control.getPosition(puestoControl.getTitulo());
                    sp_IdPuestoControl_agente.setSelection(spinnerPositionPuestoControl);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_IdPuestoControl_agente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPuestoControlTitle = (String) sp_IdPuestoControl_agente.getSelectedItem();
                for (PuestoControl puestoControl : puestoControls) {
                    if (puestoControl.getTitulo().equals(selectedPuestoControlTitle)) {
                        IdPuestoControl = puestoControl.getIdPuestoControl();
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (agente != null) {
            etCedula.setText(String.valueOf(agente.getCedulaa()));
            etNombre.setText(agente.getNombre());
            etRango.setText(agente.getRango());

            PuestoControl puestoControl = dbHelper.get_Puesto_Control(agente.getIdPuestoControl());
            Zona zona = dbHelper.get_Zona_Puesto(String.valueOf(puestoControl.getIdZona()));

            int spinnerPositionZona = adapter_zona.getPosition(zona.getTitulo());
            sp_Zona_agente.setSelection(spinnerPositionZona);

            int spinnerPositionPuestoControl = adapter_puesto_control.getPosition(puestoControl.getTitulo());
            sp_IdPuestoControl_agente.setSelection(spinnerPositionPuestoControl);

            builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        // Obtener valores actualizados de los campos
                        int cedula = Integer.parseInt(etCedula.getText().toString());
                        String nombre = etNombre.getText().toString();
                        int idPuestoControl = IdPuestoControl;
                        String rango = etRango.getText().toString();

                        // Actualizar el objeto Agente
                        agente.setCedulaa(cedula);
                        agente.setNombre(nombre);
                        agente.setIdPuestoControl(idPuestoControl);
                        agente.setRango(rango);

                        // Actualizar en la base de datos
                        dbHelper.actualizarAgente(agente);

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
        } else {
            builder.setPositiveButton("Registrar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        // Obtener valores de los campos
                        int cedula = Integer.parseInt(etCedula.getText().toString());
                        String nombre = etNombre.getText().toString();
                        int idPuestoControl = IdPuestoControl;
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
        }

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
                                mostrarDialogoRegistrarEditarAgente(agenteList.get(position));
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

    private void actualizarListaAgentes() {
        agenteList = dbHelper.getAllAgentes();
        adapter.clear();
        adapter.addAll(agenteList);
        adapter.notifyDataSetChanged();
    }

    private void mostrarConfirmacionEliminar(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("¿Estás seguro de que deseas eliminar este agente?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Agente agente = agenteList.get(position);
                        dbHelper.eliminarAgente(agente.getCedulaa());
                        actualizarListaAgentes();
                        Toast.makeText(requireContext(), "Agente eliminado correctamente", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null);
        builder.create().show();
    }
}