package com.example.grupo_05_tarea_16_ejercicio_01.fragments.Infraccion;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Agente;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Infraccion;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.NormasDet;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Usuario;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Vehiculo;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Zona;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfraccionRegisterFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    private EditText edt_register_valor_multa, edt_register_fecha_infraccion,
                    edt_register_hora_infraccion;
    private Button btn_registrar_infraccion, btn_eliminar_infraccion, btn_actualizar_infraccion;
    DBHelper dbHelper;
    private Spinner sp_agente, sp_placa, sp_norma;
    private LinearLayout layout_btn_registrar_infraccion, layout_btn_actualizar_infracion;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ProgressDialog progressDialog;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;


    private String mParam1;
    private String mParam2;

    public InfraccionRegisterFragment() {
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

        request = Volley.newRequestQueue(requireActivity());  // Inicializar RequestQueue

        edt_register_fecha_infraccion.setOnClickListener(v -> showDatePickerDialog());
        edt_register_hora_infraccion.setOnClickListener(v -> showTimePickerDialog());
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

                    CargarWebService(Id_Agente, Id_vehiculo, multa, fecha, Id_Norma, hora);

                    if (isAdded()) {
                        try {
                            NavController navController = Navigation.findNavController(v);
                            navController.navigateUp();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
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
                        ActualizarWebService(AId_Agente,AId_vehiculo,amulta,afecha,AId_Norma,ahora,ainfraccion.getIdInfraccion());
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

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, year1, month1, dayOfMonth) -> {
            String selectedDate = year1 + "-" + String.format("%02d", (month1 + 1)) + "-" + String.format("%02d", dayOfMonth);
            edt_register_fecha_infraccion.setText(selectedDate);
        }, year, month, day);

        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), (view, hourOfDay, minuteOfDay) -> {
            String selectedTime = String.format("%02d:%02d", hourOfDay, minuteOfDay);
            edt_register_hora_infraccion.setText(selectedTime);
        }, hour, minute, true);

        timePickerDialog.show();
    }

    private void CargarWebService(int idagente, int idvehiculo, String valormulta, String fecha, int idnorma, String hora) {
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Registrando...");
        progressDialog.show();

        List<String> ips = Arrays.asList("192.168.100.15", "192.168.10.106", "192.168.1.6", "192.168.1.2");
        // Puedes añadir más IPs según sea necesario
        String selectedIp = "";
        Map<String, String> userIpMap = new HashMap<>();
        userIpMap.put("jhon", ips.get(0));
        userIpMap.put("chagua", ips.get(1));
        userIpMap.put("matias", ips.get(2));
        userIpMap.put("calixto", ips.get(3));

        ArrayList<Usuario> usuarios = dbHelper.get_all_Usuarios();
        for (Usuario usuario : usuarios) {
            selectedIp = userIpMap.get(usuario.getUsername());
            if (selectedIp != null) {
                break;
            }
        }

        String urlWS = "http://" + selectedIp + "/db_grupo_05_tarea_16_ejercicio_01/InfraccionRegistro.php?" +
                "idagente=" + idagente +
                "&idvehiculo=" + idvehiculo +
                "&valormulta=" + valormulta +
                "&fecha=" + fecha +
                "&idnorma=" + idnorma +
                "&hora=" + hora;

        urlWS = urlWS.replace(" ", "%20");

        //Log.d("URLWebService", "URL: " + urlWS);

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlWS, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (isAdded()) {  // Verifica si el fragmento está adjunto antes de interactuar con la UI

                    Toast.makeText(requireActivity(), "Zona registrado correctamente", Toast.LENGTH_SHORT).show();
                    // preguntarle a chagua del runable
                }
                progressDialog.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (isAdded()) {  // Verifica si el fragmento está adjunto antes de interactuar con la UI

                    Toast.makeText(requireActivity(), "No se puede conectar: " + error.toString(), Toast.LENGTH_LONG).show();
                }
                progressDialog.hide();
            }
        });

        request.add(jsonObjectRequest);
    }

    private void ActualizarWebService(int idagente, int idvehiculo, String valormulta,String fecha, int idnorma, String hora, int idinfraccion) {
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Actualizando...");
        progressDialog.show();

        List<String> ips = Arrays.asList("192.168.100.15", "192.168.10.106", "192.168.1.6");
        // Puedes añadir más IPs según sea necesario
        String selectedIp = "";
        Map<String, String> userIpMap = new HashMap<>();
        userIpMap.put("jhon", ips.get(0));
        userIpMap.put("chagua", ips.get(1));
        userIpMap.put("matias", ips.get(2));

        ArrayList<Usuario> usuarios = dbHelper.get_all_Usuarios();
        for (Usuario usuario : usuarios) {
            selectedIp = userIpMap.get(usuario.getUsername());
            if (selectedIp != null) {
                break;
            }
        }

        String urlWS = "http://" + selectedIp + "/db_grupo_05_tarea_16_ejercicio_01/InfraccionActualizar.php";

        stringRequest = new StringRequest(Request.Method.POST, urlWS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();
                if (response.trim().equalsIgnoreCase("actualiza")) {
                    Toast.makeText(requireActivity(), "Accidente actualizado correctamente", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(requireActivity(), "Accidente no se pudo actualizar", Toast.LENGTH_SHORT).show();
                    Log.i("RESPUESTA: ", "" + response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(requireActivity(), "No se ha podido conectar", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<>();
                parametros.put("idagente", String.valueOf(idagente));
                parametros.put("idvehiculo", String.valueOf(idvehiculo));
                parametros.put("valormulta",valormulta);
                parametros.put("fecha",fecha);
                parametros.put("idnorma",String.valueOf(idnorma));
                parametros.put("hora", hora);
                parametros.put("idinfraccion", String.valueOf(idinfraccion));

                return parametros;
            }
        };
        request.add(stringRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {

    }
}