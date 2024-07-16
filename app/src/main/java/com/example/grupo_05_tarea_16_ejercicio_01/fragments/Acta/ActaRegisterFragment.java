package com.example.grupo_05_tarea_16_ejercicio_01.fragments.Acta;

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
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Accidente;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Acta;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Agente;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Audiencia;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Infraccion;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Vehiculo;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Zona;

import java.util.ArrayList;


public class ActaRegisterFragment extends Fragment {
    private EditText et_CodigoActa_Acta, et_Hora_Acta, et_FechaActa_Acta;
    private Spinner sp_IdAccidente, sp_IdAudiencia, sp_IdZona, sp_IdAgente;
    private Button  btn_RegistrarActa, btn_eliminar_acta, btn_actualizar_acta;
    DBHelper dbHelper;
    private LinearLayout layout_btn_registrar_acta, layout_btn_actualizar_acta;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ActaRegisterFragment() {
        // Required empty public constructor
    }

    public static ActaRegisterFragment newInstance(String param1, String param2) {
        ActaRegisterFragment fragment = new ActaRegisterFragment();
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
        View view = inflater.inflate(R.layout.fragment_acta_register, container, false);
        et_CodigoActa_Acta = view.findViewById(R.id.et_CodigoActa_Acta);
        et_Hora_Acta = view.findViewById(R.id.et_Hora_Acta);
        et_FechaActa_Acta = view.findViewById(R.id.et_FechaActa_Acta);
        sp_IdAccidente = view.findViewById(R.id.sp_IdAccidente);
        sp_IdAudiencia = view.findViewById(R.id.sp_IdAudiencia);
        sp_IdAgente = view.findViewById(R.id.sp_IdAgente);
        sp_IdZona = view.findViewById(R.id.sp_IdZona);
        btn_RegistrarActa = view.findViewById(R.id.btn_RegistrarActa);
        btn_eliminar_acta = view.findViewById(R.id.btn_eliminar_acta);
        btn_actualizar_acta = view.findViewById(R.id.btn_actualizar_acta);
        layout_btn_registrar_acta = view.findViewById(R.id.layout_btn_registrar_acta);
        layout_btn_actualizar_acta = view.findViewById(R.id.layout_btn_actualizar_acta);
        return view;
    }
    private int IdAccidente, IdAudiencia, IdZona, IdAgente;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DBHelper(getContext());
        ArrayList<Accidente> accidentes = dbHelper.get_all_Accidentes();
        ArrayList<String> tituloAccidentes= new ArrayList<>();
        if (accidentes != null) {
            for (Accidente accidente : accidentes){
                tituloAccidentes.add(String.valueOf(accidente.getTitulo()));
            }
        }
        ArrayAdapter<String> adapter_accidente = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tituloAccidentes);
        adapter_accidente.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_IdAccidente.setAdapter(adapter_accidente);

        ArrayList<Audiencia> audiencias = dbHelper.get_all_Audiencias();
        ArrayList<String> tituloAudiecnias = new ArrayList<>();
        if (audiencias != null) {
            for (Audiencia agAudiencia : audiencias){
                String item = agAudiencia.getCodigo() + " - " + agAudiencia.getLugar(); // Concatenar ID y nombre
                tituloAudiecnias.add(item);
            }
        }
        ArrayAdapter<String> adapter_audiencia = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tituloAudiecnias);
        adapter_audiencia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_IdAudiencia.setAdapter(adapter_audiencia);

        ArrayList<Zona> zonas = dbHelper.get_all_Zonas();
        ArrayList<String> tituloZonas= new ArrayList<>();
        if (zonas != null) {
            for (Zona zona : zonas){
                tituloZonas.add(String.valueOf(zona.getTitulo()));
            }
        }
        ArrayAdapter<String> adapter_zona = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tituloZonas);
        adapter_zona.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_IdZona.setAdapter(adapter_zona);

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
        sp_IdAgente.setAdapter(adapter_agente);

        sp_IdAccidente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Accidente accidente = accidentes.get(position);
                IdAccidente = accidente.getIdaccidente();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_IdAudiencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Audiencia audiencia = audiencias.get(position);
                IdAudiencia = audiencia.getIdAudiencia();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_IdAgente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Agente agente = agentes.get(position);
                IdAgente = agente.getIdagente();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_IdZona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Zona zona = zonas.get(position);
                IdZona = zona.getIdZona();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_RegistrarActa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Id_Agente = IdAgente;
                int Id_Accidente = IdAccidente;
                int Id_Audiencia = IdAudiencia;
                int Id_Zona = IdZona;
                int  codigo = Integer.parseInt(et_CodigoActa_Acta.getText().toString());
                String fecha = et_FechaActa_Acta.getText().toString();
                String hora = et_Hora_Acta.getText().toString();
                if (TextUtils.isEmpty(String.valueOf(codigo)) || TextUtils.isEmpty(fecha) || TextUtils.isEmpty(hora)) {
                    Toast.makeText(getContext(), "Por favor complete los campos", Toast.LENGTH_SHORT).show();
                } else {
                    Acta acta = new Acta(codigo, Id_Accidente, Id_Audiencia, hora, Id_Zona, Id_Agente, fecha);
                    dbHelper.insertarActa(acta);
                    NavController navController = Navigation.findNavController(v);
                    navController.navigateUp();
                    Toast.makeText(getContext(), "Infraccion Registrada", Toast.LENGTH_SHORT).show();
                }
            }
        });

        boolean modoEdicion = getArguments() != null && getArguments().getBoolean("modo_edicion", false);
        if (modoEdicion) {
            layout_btn_actualizar_acta.setVisibility(View.VISIBLE);
            layout_btn_registrar_acta.setVisibility(View.GONE);

            int IdActa = getArguments().getInt("IdActa");
            Acta acta = dbHelper.get_Acta(IdActa);
            Accidente accidente = dbHelper.get_Accidente(acta.getIdaccidente());
            Audiencia audiencia = dbHelper.get_Audiencia(acta.getIdAudiencia());
            Agente agente = dbHelper.get_Agente(acta.getIdagente());
            Zona zona = dbHelper.get_Zona_Puesto(String.valueOf(acta.getIdZona()));

            String agenteItem = agente.getCedulaa() + " - " + agente.getNombre();
            int spinnerPosition_agente = adapter_agente.getPosition(agenteItem);
            sp_IdAgente.setSelection(spinnerPosition_agente);
            sp_IdAgente.setPadding(16,8,16,8);

            String audienciaItem = audiencia.getCodigo() + " - " + audiencia.getLugar();
            int spinnerPosition_audiencia = adapter_agente.getPosition(audienciaItem);
            sp_IdAudiencia.setSelection(spinnerPosition_audiencia);
            sp_IdAudiencia.setPadding(16,8,16,8);

            int spinnerPosition_accidente = adapter_accidente.getPosition(String.valueOf(accidente.getTitulo()));
            sp_IdAgente.setSelection(spinnerPosition_accidente);
            sp_IdAgente.setPadding(16,8,16,8);

            int spinnerPosition_zona = adapter_zona.getPosition(zona.getTitulo());
            sp_IdZona.setSelection(spinnerPosition_zona);
            sp_IdZona.setPadding(16,8,16,8);

            et_CodigoActa_Acta.setText(String.valueOf(acta.getCodigo()));
            et_Hora_Acta.setText(acta.getHora());
            et_FechaActa_Acta.setText(acta.getFecha());

            btn_actualizar_acta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int AId_Agente = IdAgente;
                    int AId_Accidente = IdAccidente;
                    int AId_Audiencia = IdAudiencia;
                    int AId_Zona = IdZona;
                    int Acodigo = Integer.parseInt(et_CodigoActa_Acta.getText().toString());
                    String Afecha = et_FechaActa_Acta.getText().toString();
                    String Ahora = et_Hora_Acta.getText().toString();
                    Acta aacta = dbHelper.get_Acta(IdActa);
                    if (aacta != null) {
                        aacta.setCodigo(Acodigo);
                        aacta.setIdaccidente(AId_Accidente);
                        aacta.setIdAudiencia(AId_Audiencia);
                        aacta.setHora(Ahora);
                        aacta.setIdZona(AId_Zona);
                        aacta.setIdagente(AId_Agente);
                        aacta.setFecha(Afecha);
                        dbHelper.actualizarActa(aacta);
                        aacta = null;
                        NavController navController = Navigation.findNavController(v);
                        navController.navigateUp();
                        Toast.makeText(getContext(), "Acta Actualizada", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Acta no Actualizada", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            btn_eliminar_acta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setTitle("Confirmar Eliminación")
                            .setMessage("¿Estás seguro de eliminar esta Acta?")
                            .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Acta aacta = dbHelper.get_Acta(IdActa);
                                    dbHelper.eliminarActa(aacta);
                                    NavController navController = Navigation.findNavController(v);
                                    navController.navigateUp();
                                    Toast.makeText(requireContext(), "Acta eliminado correctamente", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Cancelar", null)
                            .create().show();
                }
            });
        }
    }

}