package com.example.grupo_05_tarea_16_ejercicio_01.fragments.Audiencia;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
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
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.AudienciaAdapter;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Audiencia;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Usuario;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AudienciaFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    private ListView lv_audiencias;
    private Button btn_nuevaAudiencia;
    private DBHelper dbHelper;

    private ProgressDialog progressDialog;
    StringRequest stringRequest;

    ProgressDialog progreso;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

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
        request = Volley.newRequestQueue(requireActivity());
        View view = inflater.inflate(R.layout.fragment_audiencia, container, false);

        lv_audiencias = view.findViewById(R.id.lv_audiencias);
        btn_nuevaAudiencia = view.findViewById(R.id.btn_nuevaAudiencia);
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
        view.findViewById(R.id.btn_nuevaAudiencia).setOnClickListener(v -> Dialog_Audiencia(1, null));

        lv_audiencias.setOnItemClickListener((parent, view1, position, id) -> {
            Audiencia paudiencia = (Audiencia) parent.getItemAtPosition(position);
            Dialog_Audiencia(2, paudiencia);
        });
    }

    public void Dialog_Audiencia(int tipo, @Nullable Audiencia paudiencia) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_audiencia, null);

        final EditText et_lugar = dialogView.findViewById(R.id.et_lugar);
        final EditText et_fecha = dialogView.findViewById(R.id.et_fecha);
        final EditText et_hora = dialogView.findViewById(R.id.et_hora);
        final EditText et_codigo = dialogView.findViewById(R.id.et_codigo);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);

        if (tipo == 1) { // Insertar
            builder.setTitle("Añadir Audiencia");
            builder.setPositiveButton("Guardar", (dialog, which) -> {
                int codigo = Integer.parseInt(et_codigo.getText().toString().trim());
                String lugar = et_lugar.getText().toString().trim();
                String fecha = et_fecha.getText().toString().trim();
                String hora = et_hora.getText().toString().trim();

                Audiencia audiencia = new Audiencia(codigo, lugar, fecha, hora);
                dbHelper.Insertar_Audiencia(audiencia);
                cargaWebService(codigo, lugar, fecha, hora);
                ListarAudiencias();
            });
            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        } else { // Actualizar o Eliminar
            builder.setTitle("Actualizar Audiencia");

            et_lugar.setText(paudiencia.getLugar());
            et_fecha.setText(paudiencia.getFecha());
            et_hora.setText(paudiencia.getHora());
            et_codigo.setText(String.valueOf(paudiencia.getCodigo()));

            builder.setPositiveButton("Guardar", (dialog, which) -> {
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

                    ActualizarWebService(codigo,lugar,fecha,hora,audiencia.getIdAudiencia());
                    Toast.makeText(getContext(), "Audiencia Actualizada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Audiencia no Actualizada", Toast.LENGTH_SHORT).show();
                }
                ListarAudiencias();
            });

            builder.setNegativeButton("Eliminar", (dialog, which) -> {
                AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(requireContext());
                deleteBuilder.setTitle("Confirmar Eliminación")
                        .setMessage("¿Estás seguro de eliminar este Audiencia?")
                        .setPositiveButton("Eliminar", (dialog1, which1) -> {
                            dbHelper.Eliminar_Audiencia(paudiencia);
                            ListarAudiencias();
                            Toast.makeText(requireContext(), "Audiencia eliminada correctamente", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancelar", null)
                        .create().show();
            });
        }

        builder.show();

        et_fecha.setOnClickListener(v -> showDatePickerDialog(et_fecha));
        et_hora.setOnClickListener(v -> showTimePickerDialog(et_hora));
    }

    private void showDatePickerDialog(final EditText et_fecha) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, year1, month1, dayOfMonth) -> {
            String selectedDate = year1 + "-" + String.format("%02d", (month1 + 1)) + "-" + String.format("%02d", dayOfMonth);
            et_fecha.setText(selectedDate);
        }, year, month, day);

        datePickerDialog.show();
    }

    private void showTimePickerDialog(final EditText et_hora) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), (view, hourOfDay, minuteOfDay) -> {
            String selectedTime = String.format("%02d:%02d:%02d", hourOfDay, minuteOfDay, second);
            et_hora.setText(selectedTime);
        }, hour, minute, true);

        timePickerDialog.show();
    }


    public void ListarAudiencias() {
        ArrayList<Audiencia> audiencias = dbHelper.get_all_Audiencias();
        AudienciaAdapter adapter = new AudienciaAdapter(getActivity(), audiencias);
        lv_audiencias.setAdapter(adapter);
    }

    //WEB SERVICE
    private void cargaWebService(int codigo, String lugar, String fecha, String hora) {
        progreso = new ProgressDialog(requireActivity());
        progreso.setMessage("Cargando...");
        progreso.show();

        List<String> ips = Arrays.asList("192.168.100.15", "192.168.10.106", "192.168.1.6");
        // Puedes añadir más IPs según sea necesario
        String selectedIp = "";
        Map<String, String> userIpMap = new HashMap<>();
        userIpMap.put("jhon", ips.get(0));
        userIpMap.put("chagua", ips.get(1));
        userIpMap.put("matias", ips.get(2)); // Assuming all three get IP1 for now

        ArrayList<Usuario> usuarios = dbHelper.get_all_Usuarios();
        for (Usuario usuario : usuarios) {
            selectedIp = userIpMap.get(usuario.getUsername());
            if (selectedIp != null) {
                break; // Exit loop after finding a match
            }
        }

        String url = "http://" + selectedIp +"/db_grupo_05_tarea_16_ejercicio_01/AudienciaRegistro.php?codigo=" + codigo + "&lugar=" + lugar + "&fecha=" + fecha + "&hora=" + hora;
        url = url.replace(" ", "%20");

        Log.d("URLWebService", "URL: " + url);

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }


    private void ActualizarWebService(int codigo, String lugar, String fecha, String hora, int idaudiencia) {
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Actualizando...");
        progressDialog.show();

        List<String> ips = Arrays.asList("192.168.100.15", "192.168.10.106", "192.168.1.6");
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

        if (selectedIp.isEmpty()) {
            progressDialog.hide();
            Toast.makeText(requireActivity(), "No se pudo determinar la IP", Toast.LENGTH_SHORT).show();
            return;
        }

        String urlWS = "http://" + selectedIp + "/db_grupo_05_tarea_16_ejercicio_01/AudienciaActualizar.php";
        Log.d("URLWebService", "URL: " + urlWS);

        stringRequest = new StringRequest(Request.Method.POST, urlWS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();
                Toast.makeText(requireActivity(), "Audiencia actualizada con éxito", Toast.LENGTH_SHORT).show();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    String errorResponse = new String(error.networkResponse.data);
                    Log.e("VolleyError", errorResponse);
                } else {
                    Log.e("VolleyError", error.toString());
                }
                Toast.makeText(requireActivity(), "No se ha podido conectar", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<>();
                parametros.put("codigo", String.valueOf(codigo));
                parametros.put("lugar", lugar);
                parametros.put("fecha", fecha);
                parametros.put("hora", hora);
                parametros.put("idaudiencia", String.valueOf(idaudiencia));

                Log.d("Params", "codigo: " + codigo + ", lugar: " + lugar + ", fecha: " + fecha + ", hora: " + hora);
                return parametros;
            }
        };
        request.add(stringRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        Toast.makeText(requireActivity(), "No se puede conectar: " + error.toString(), Toast.LENGTH_LONG).show();
        Log.d("ERROR: ", error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        progreso.hide();
        Toast.makeText(requireActivity(), "Mensaje: " + response.toString(), Toast.LENGTH_SHORT).show();
    }
}
