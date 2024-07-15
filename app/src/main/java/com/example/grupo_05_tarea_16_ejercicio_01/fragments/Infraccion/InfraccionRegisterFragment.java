package com.example.grupo_05_tarea_16_ejercicio_01.fragments.Infraccion;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Agente;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Infraccion;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.NormasDet;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Vehiculo;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Zona;

import java.util.ArrayList;

public class InfraccionRegisterFragment extends Fragment {

    private EditText edt_register_valor_multa, edt_register_fecha_infraccion,
                    edt_register_hora_infraccion;
    private Button btn_registrar_infraccion, btn_eliminar_infraccion, btn_actualizar_infraccion;
    DBHelper dbHelper;
    private Spinner sp_agente, sp_placa, sp_norma;
    private LinearLayout layout_btn_registrar_infraccion, layout_btn_actualizar_infracion;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public InfraccionRegisterFragment() {
        // Required empty public constructor
    }

    public static InfraccionRegisterFragment newInstance(String param1, String param2) {
        InfraccionRegisterFragment fragment = new InfraccionRegisterFragment();
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
        View view = inflater.inflate(R.layout.fragment_infraccion_register, container, false);
        edt_register_valor_multa = view.findViewById(R.id.edt_register_valor_multa);
        edt_register_fecha_infraccion = view.findViewById(R.id.edt_register_fecha_infraccion);
        edt_register_hora_infraccion = view.findViewById(R.id.edt_register_hora_infraccion);
        btn_registrar_infraccion = view.findViewById(R.id.btn_registrar_infraccion);
        btn_actualizar_infraccion = view.findViewById(R.id.btn_actualizar_infraccion);
        btn_eliminar_infraccion = view.findViewById(R.id.btn_eliminar_infraccion);
        layout_btn_registrar_infraccion = view.findViewById(R.id.layout_btn_registrar_infraccion);
        layout_btn_actualizar_infracion = view.findViewById(R.id.layout_btn_actualizar_infracion);
        sp_agente = view.findViewById(R.id.sp_agente);
        sp_placa = view.findViewById(R.id.sp_placa);
        sp_norma = view.findViewById(R.id.sp_norma);
        return view;
    }
    private int IdAgente, IdVehiculo, IdNorma;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DBHelper(getContext());
        ArrayList<Agente> agentes = dbHelper.getAllAgentes();
        ArrayList<String> tituloAgentes = new ArrayList<>();
        if (agentes != null) {
            for (Agente agente : agentes){
                String item = agente.getCedulaa() + " - " + agente.getNombre(); // Concatenar ID y nombre
                tituloAgentes.add(item);
            }
        }
        ArrayAdapter<String> adapter_agente = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tituloAgentes);
        adapter_agente.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_agente.setAdapter(adapter_agente);

        ArrayList<Vehiculo> vehiculos = dbHelper.get_all_Vehiculos();
        ArrayList<String> tituloVehiculos= new ArrayList<>();
        if (vehiculos != null) {
            for (Vehiculo vehiculo : vehiculos){
                tituloVehiculos.add(String.valueOf(vehiculo.getNumplaca()));
            }
        }
        ArrayAdapter<String> adapter_placa = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tituloVehiculos);
        adapter_placa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_placa.setAdapter(adapter_placa);

        ArrayList<NormasDet> normasDets = dbHelper.get_all_Normas_Detalle();
        ArrayList<String> tituloNormasDets= new ArrayList<>();
        if (normasDets != null) {
            for (NormasDet normasDet : normasDets){
                tituloNormasDets.add(normasDet.getNumnorma());
            }
        }
        ArrayAdapter<String> adapter_norma = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tituloNormasDets);
        adapter_norma.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_norma.setAdapter(adapter_norma);

        sp_agente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Agente agente = agentes.get(position);
                IdAgente = agente.getIdagente();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_placa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Vehiculo vehiculo = vehiculos.get(position);
                IdVehiculo = vehiculo.getIdVehiculo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_norma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                NormasDet normasDet = normasDets.get(position);
                IdNorma = normasDet.getIdnomra();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_registrar_infraccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Id_Agente = IdAgente;
                int Id_vehiculo = IdVehiculo;
                int Id_Norma = IdNorma;
                String multa = edt_register_valor_multa.getText().toString();
                String fecha = edt_register_fecha_infraccion.getText().toString();
                String hora = edt_register_hora_infraccion.getText().toString();
                if (TextUtils.isEmpty(multa) || TextUtils.isEmpty(fecha) || TextUtils.isEmpty(hora)) {
                    Toast.makeText(getContext(), "Por favor complete los campos", Toast.LENGTH_SHORT).show();
                } else {
                    Infraccion infraccion = new Infraccion(Id_Agente,Id_vehiculo,multa,fecha,Id_Norma,hora);
                    dbHelper.Insertar_Infraccion(infraccion);
                    NavController navController = Navigation.findNavController(v);
                    navController.navigateUp();
                    Toast.makeText(getContext(), "Infraccion Registrada", Toast.LENGTH_SHORT).show();
                }
            }
        });

        boolean modoEdicion = getArguments() != null && getArguments().getBoolean("modo_edicion", false);
        if (modoEdicion) {
            layout_btn_actualizar_infracion.setVisibility(View.VISIBLE);
            layout_btn_registrar_infraccion.setVisibility(View.GONE);
            int IdInfraccion = getArguments().getInt("IdInfraccion");
            Infraccion infraccion = dbHelper.get_Infraccion(IdInfraccion);
            Agente agente = dbHelper.get_Agente(infraccion.getIdagente());
            Vehiculo vehiculo = dbHelper.get_Vehiculo(infraccion.getIdVehiculo());
            NormasDet normasDet = dbHelper.get_Norma_Detalle(infraccion.getIdnomra());

            String agenteItem = agente.getCedulaa() + " - " + agente.getNombre();
            int spinnerPosition_agente = adapter_agente.getPosition(agenteItem);
            sp_agente.setSelection(spinnerPosition_agente);
            sp_agente.setPadding(16,8,16,8);

            int spinnerPosition_vehiculo = adapter_placa.getPosition(String.valueOf(vehiculo.getNumplaca()));
            sp_placa.setSelection(spinnerPosition_vehiculo);
            sp_placa.setPadding(16,8,16,8);

            int spinnerPosition_norma = adapter_norma.getPosition(normasDet.getNumnorma());
            sp_norma.setSelection(spinnerPosition_norma);
            sp_norma.setPadding(16,8,16,8);

            edt_register_valor_multa.setText(infraccion.getValormulta());
            edt_register_fecha_infraccion.setText(infraccion.getFecha());
            edt_register_hora_infraccion.setText(infraccion.getHora());

            btn_actualizar_infraccion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int AId_Agente = IdAgente;
                    int AId_vehiculo = IdVehiculo;
                    int AId_Norma = IdNorma;
                    String amulta = edt_register_valor_multa.getText().toString();
                    String afecha = edt_register_fecha_infraccion.getText().toString();
                    String ahora = edt_register_hora_infraccion.getText().toString();
                    Infraccion ainfraccion = dbHelper.get_Infraccion(IdInfraccion);
                    if (ainfraccion != null) {
                        ainfraccion.setIdagente(AId_Agente);
                        ainfraccion.setIdVehiculo(AId_vehiculo);
                        ainfraccion.setValormulta(amulta);
                        ainfraccion.setFecha(afecha);
                        ainfraccion.setIdnomra(AId_Norma);
                        ainfraccion.setHora(ahora);
                        dbHelper.Actualizar_Infraccion(ainfraccion);
                        ainfraccion = null;
                        NavController navController = Navigation.findNavController(v);
                        navController.navigateUp();
                        Toast.makeText(getContext(), "Infraccion Actualizada", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Infraccion no Actualizada", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            btn_eliminar_infraccion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setTitle("Confirmar Eliminación")
                            .setMessage("¿Estás seguro de eliminar esta Infraccion?")
                            .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Infraccion ainfraccion = dbHelper.get_Infraccion(IdInfraccion);
                                    dbHelper.Eliminar_Infraccion(ainfraccion);
                                    NavController navController = Navigation.findNavController(v);
                                    navController.navigateUp();
                                    Toast.makeText(requireContext(), "Infraccion eliminado correctamente", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Cancelar", null)
                            .create().show();
                }
            });
        }
    }
}