package com.example.grupo_05_tarea_16_ejercicio_01.fragments.Vehiculo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.IPUtilizada;
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.Vehiculo_Adapter;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Accidente;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Infraccion;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.OficinaGob;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Propietario;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Usuario;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Vehiculo;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VehiculoFragment extends Fragment implements Vehiculo_Adapter.OnItemLongClickListener, Response.Listener<JSONObject>, Response.ErrorListener  {
    private EditText etNumPlaca, etMarca, etModelo, etMotor, etFecha;
    private Spinner spPropietarios;
    private ListView lvVehiculos;
    private Button btnRegistrarVehiculo;
    private DBHelper dbHelper;
    private AlertDialog dialog;
    private Vehiculo_Adapter vehiculoAdapter;
    private ArrayList<Vehiculo> vehiculos;
    private Vehiculo vehiculoSeleccionado;
    private ArrayList<Propietario> propietarios;

    private ProgressDialog progressDialog;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;


    StringRequest stringRequest;
    public VehiculoFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vehiculo, container, false);

        dbHelper = new DBHelper(requireActivity());
        lvVehiculos = view.findViewById(R.id.lv_vehiculos);
        btnRegistrarVehiculo = view.findViewById(R.id.btn_registrar_vehiculo);
        request = Volley.newRequestQueue(requireActivity());
        vehiculos = new ArrayList<>();
        propietarios = new ArrayList<>();




        cargarPropietarios();

        btnRegistrarVehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Propietario> prueba = dbHelper.get_all_Propietarios();
                if (prueba.isEmpty()) {
                    Toast.makeText(getContext(), "No Existe Propietario", Toast.LENGTH_SHORT).show();
                } else {
                    mostrarDialogoRegistrarVehiculo();
                }
            }
        });

        vehiculoAdapter = new Vehiculo_Adapter(getContext(), vehiculos);
        vehiculoAdapter.setOnItemLongClickListener(this);
        lvVehiculos.setAdapter(vehiculoAdapter);

        cargarVehiculos();

        // Implementar OnBackPressedCallback
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // No hacer nada para deshabilitar el botón de retroceso
            }
        });

        return view;
    }


    private void cargarPropietarios() {
        propietarios = dbHelper.get_all_Propietarios();
    }

    private void mostrarDialogoRegistrarVehiculo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_registro_vehiculo, null);
        builder.setView(dialogView);

        etNumPlaca = dialogView.findViewById(R.id.et_numPlaca);
        etMarca = dialogView.findViewById(R.id.et_marca);
        etModelo = dialogView.findViewById(R.id.et_modelo);
        etMotor = dialogView.findViewById(R.id.et_motor);
        etFecha = dialogView.findViewById(R.id.et_fecha);
        spPropietarios = dialogView.findViewById(R.id.sp_propietarios);
        etFecha.setOnClickListener(v -> showYearPickerDialog(etFecha));

        // Cargar los nombres de los clientes en el Spinner
        cargarPropietariosEnSpinner(spPropietarios);

        builder.setTitle("");
        builder.setPositiveButton("Registrar", (dialogInterface, which) -> {
            String numPlaca = etNumPlaca.getText().toString();
            String marca = etMarca.getText().toString();
            String modelo = etModelo.getText().toString();
            String motor = etMotor.getText().toString();
            String fecha = etFecha.getText().toString();
            int propietarioPosicion = spPropietarios.getSelectedItemPosition();

            if (numPlaca.isEmpty() || marca.isEmpty() || modelo.isEmpty() || motor.isEmpty() || fecha.isEmpty() || propietarioPosicion == -1) {
                Toast.makeText(getContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            int numeroPlacaInt;
            try {

                numeroPlacaInt = Integer.parseInt(numPlaca);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Número de Placa debe ser valor numérico", Toast.LENGTH_SHORT).show();
                return;
            }

            Propietario propietario = propietarios.get(propietarioPosicion);
            Vehiculo vehiculo = new Vehiculo(numeroPlacaInt, marca, modelo, motor, fecha, propietario.getIdPropietario());

            dbHelper.Insertar_Vehiculo(vehiculo);

            Runnable runnable = new Runnable() {
                @Override
                public void run() {}
            };

            cargaWebService(numeroPlacaInt, marca, modelo, motor, fecha, propietario.getIdPropietario(), runnable);
            Toast.makeText(getContext(), "Vehículo registrado", Toast.LENGTH_SHORT).show();
            cargarVehiculos();
        });
        builder.setNegativeButton("Cancelar", (dialogInterface, which) -> {
        });

        AlertDialog dialogRegistrar = builder.create();
        dialogRegistrar.show();
    }

    private void cargarPropietariosEnSpinner(Spinner spinner) {
        ArrayList<String> nombresPropietarios = new ArrayList<>();
        for (Propietario propietario : propietarios) {
            nombresPropietarios.add(propietario.getNombre());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, nombresPropietarios);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void cargarVehiculos() {
        vehiculos.clear();
        vehiculos.addAll(dbHelper.get_all_Vehiculos());
        vehiculoAdapter.notifyDataSetChanged();
        // Antes de notificar los cambios, obtén los nombres de los propietarios
        HashMap<Integer, String> propietariosMap = obtenerMapaPropietarios();
        vehiculoAdapter.setPropietariosMap(propietariosMap);
        vehiculoAdapter.notifyDataSetChanged();
    }

    private HashMap<Integer, String> obtenerMapaPropietarios() {
        HashMap<Integer, String> propietariosMap = new HashMap<>();
        for (Propietario propietario : propietarios) {
            propietariosMap.put(propietario.getIdPropietario(), propietario.getNombre());
        }
        return propietariosMap;
    }


    @Override
    public void onItemLongClick(Vehiculo vehiculo) {
        vehiculoSeleccionado = vehiculo;
        mostrarDialogoVehiculo(vehiculo);
    }

    private void mostrarDialogoVehiculo(Vehiculo vehiculo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.vehiculo_onitemlong, null);
        builder.setView(dialogView);

        TextView tvDialogNumPlaca = dialogView.findViewById(R.id.tv_dialog_numeroPlaca);
        TextView tvDialogMarca = dialogView.findViewById(R.id.tv_dialog_Marca);
        Button btnEliminar = dialogView.findViewById(R.id.btn_eliminar);
        Button btnActualizar = dialogView.findViewById(R.id.btn_actualizar);
        Button btnVolver = dialogView.findViewById(R.id.btn_volver);

        tvDialogNumPlaca.setText(String.valueOf(vehiculo.getNumplaca()));
        tvDialogMarca.setText(vehiculo.getMarca());

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoConfirmacionEliminar();
                dialog.dismiss();
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarVehiculo(vehiculo);
                dialog.dismiss();
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog = builder.create();
        dialog.show();
    }

    private void mostrarDialogoConfirmacionEliminar() {
        dialog.dismiss();

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Borrar Vehículo");
        builder.setMessage("¿Estás seguro de que deseas borrar este vehículo?");
        builder.setPositiveButton("Aceptar", (dialogInterface, which) -> {
            eliminarVehiculo(vehiculoSeleccionado);
            EliminarWebService(vehiculoSeleccionado.getIdVehiculo());
        });
        builder.setNegativeButton("Cancelar", (dialogInterface, which) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void eliminarVehiculo(Vehiculo vehiculo) {
        ArrayList<OficinaGob> comprobar01 = dbHelper.get_all_Oficinas();
        ArrayList<Accidente> comprobar02 = dbHelper.get_all_Accidentes();
        ArrayList<Infraccion> comprobar03 = dbHelper.get_all_Infracciones();

        boolean exists = doesIdOficinaExist(comprobar01, vehiculo.getIdVehiculo());
        boolean exists01 = doesIdAccidenteExist(comprobar02, vehiculo.getIdVehiculo());
        boolean exists02 = doesIdInfraccionExist(comprobar03, vehiculo.getIdVehiculo());


        if (exists || exists01 || exists02) {
            Toast.makeText(getContext(), "Existen Registros Dependientes", Toast.LENGTH_SHORT).show();
        } else {
            dbHelper.Eliminar_Vehiculo(vehiculo);

            Toast.makeText(getContext(), "Vehiculo eliminado", Toast.LENGTH_SHORT).show();
            cargarVehiculos();
        }
    }

    public static boolean doesIdOficinaExist(ArrayList<OficinaGob> oficinas, int idOficinaToCheck) {
        for (OficinaGob oficina : oficinas) {
            if (oficina.getIdVehiculo() == idOficinaToCheck) {
                return true;
            }
        }
        return false;
    }

    public static boolean doesIdAccidenteExist(ArrayList<Accidente> accidentes, int idAccidenteToCheck) {
        for (Accidente accidente : accidentes) {
            if (accidente.getIdVehiculo() == idAccidenteToCheck) {
                return true;
            }
        }
        return false;
    }

    public static boolean doesIdInfraccionExist(ArrayList<Infraccion> infracciones, int idInfraccionToCheck) {
        for (Infraccion infraccion : infracciones) {
            if (infraccion.getIdVehiculo() == idInfraccionToCheck) {
                return true;
            }
        }
        return false;
    }

    private void actualizarVehiculo(Vehiculo vehiculo) {
        dialog.dismiss();

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.vehiculo_actualizar, null);
        builder.setView(view);

        EditText etActualizarNumPlaca = view.findViewById(R.id.et_actualizar_numPlaca);
        EditText etActualizarMarca = view.findViewById(R.id.et_actualizar_marca);
        EditText etActualizarModelo = view.findViewById(R.id.et_actualizar_modelo);
        EditText etActualizarMotor = view.findViewById(R.id.et_actualizar_motor);
        EditText etActualizarFecha = view.findViewById(R.id.et_actualizar_fecha);
        Spinner sp_ActualizarPropietario = view.findViewById(R.id.sp_actualizar_propietario);

        etActualizarFecha.setOnClickListener(v -> showYearPickerDialog(etActualizarFecha));

        etActualizarNumPlaca.setText(String.valueOf(vehiculo.getNumplaca()));
        etActualizarMarca.setText(String.valueOf(vehiculo.getMarca()));
        etActualizarModelo.setText(vehiculo.getModelo());
        etActualizarMotor.setText(vehiculo.getMotor());
        etActualizarFecha.setText(vehiculo.getF_ano());

        ArrayList<String> nombresPropietarios = new ArrayList<>();
        for (Propietario propietario : propietarios) {
            nombresPropietarios.add(propietario.getNombre());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, nombresPropietarios);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_ActualizarPropietario.setAdapter(adapter);

        for (int i = 0; i < propietarios.size(); i++) {
            if (propietarios.get(i).getIdPropietario() == vehiculo.getIdPropietario()) {
                sp_ActualizarPropietario.setSelection(i);
                break;
            }
        }

        builder.setTitle("Editar Dirección");
        builder.setPositiveButton("Editar", (dialogInterface, which) -> {
            int nuevoNumPlaca = Integer.parseInt(etActualizarNumPlaca.getText().toString());
            String nuevaMarca = etActualizarMarca.getText().toString();
            String nuevoModelo = etActualizarModelo.getText().toString();
            String nuevoMotor = etActualizarMotor.getText().toString();
            String nuevaFecha = etActualizarFecha.getText().toString();
            int propietarioPosicion = sp_ActualizarPropietario.getSelectedItemPosition();

            if (String.valueOf(nuevoNumPlaca).isEmpty() || String.valueOf(nuevaMarca).isEmpty() ||
                    nuevoModelo.isEmpty() || nuevoMotor.isEmpty() || nuevaFecha.isEmpty() ||
                    propietarioPosicion == -1) {
                Toast.makeText(getContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Propietario propietarioSeleccionado = propietarios.get(propietarioPosicion);

            vehiculo.setNumplaca(nuevoNumPlaca);
            vehiculo.setMarca(nuevaMarca);
            vehiculo.setModelo(nuevoModelo);
            vehiculo.setMotor(nuevoMotor);
            vehiculo.setF_ano(nuevaFecha);
            vehiculo.setIdPropietario(propietarioSeleccionado.getIdPropietario());

            dbHelper.Actualizar_Vehiculo(vehiculo);

            ActualizarWebService(nuevoNumPlaca,nuevaMarca,nuevoModelo,nuevoMotor,nuevaFecha,propietarioSeleccionado.getIdPropietario(),vehiculoSeleccionado.getIdVehiculo());

            Toast.makeText(getContext(), "Dirección actualizada", Toast.LENGTH_SHORT).show();
            cargarVehiculos();
        });
        builder.setNegativeButton("Cancelar", (dialogInterface, which) -> {
        });

        AlertDialog dialogActualizar = builder.create();
        dialogActualizar.show();
    }

    private void cargaWebService(int numplaca, String marca, String modelo, String motor, String fecha, int idpropietario, Runnable runnable) {
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Registrando...");
        progressDialog.show();
        // Puedes añadir más IPs según sea necesario
        String selectedIp = "";

        ArrayList<Usuario> usuarios = dbHelper.get_all_Usuarios();
        for (Usuario usuario : usuarios) {
            selectedIp = IPUtilizada.getInstance().getSelectedIP(usuario.getUsername());
            if (selectedIp != null) {
                break;
            }
        }

        String url = "http://" + selectedIp + "/db_grupo_05_tarea_16_ejercicio_01/VehiculoRegistro.php?" +
                "numplaca=" + numplaca +
                "&marca=" + marca +
                "&modelo=" + modelo +
                "&motor=" + motor +
                "&f_ano=" + fecha +
                "&idpropietario=" + idpropietario;

        url = url.replace(" ", "%20");

        Log.d("URLWebService", "URL: " + url);

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.hide();
                Toast.makeText(requireActivity(), "Vehiculo registrado correctamente", Toast.LENGTH_SHORT).show();
                if (runnable != null) {
                    runnable.run();
                }
                cargarVehiculos();
            }
        }, error -> {
            progressDialog.hide();
            String errorMessage = error.toString();
            NetworkResponse networkResponse = error.networkResponse;
            if (networkResponse != null) {
                try {
                    String responseBody = new String(networkResponse.data, "utf-8");
                    Log.d("ServerResponse", responseBody);
                    errorMessage += "\nResponse: " + responseBody;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(requireActivity(), "No se puede conectar: " + errorMessage, Toast.LENGTH_LONG).show();
            Log.d("ERROR: ", errorMessage);
        });

        request.add(jsonObjectRequest);
    }

    private void ActualizarWebService(int numplaca, String marca, String modelo,String motor, String f_ano, int idpropietario, int idvehiculo) {
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Actualizando...");
        progressDialog.show();

        // Puedes añadir más IPs según sea necesario
        String selectedIp = "";

        ArrayList<Usuario> usuarios = dbHelper.get_all_Usuarios();
        for (Usuario usuario : usuarios) {
            selectedIp = IPUtilizada.getInstance().getSelectedIP(usuario.getUsername());
            if (selectedIp != null) {
                break;
            }
        }

        String urlWS = "http://" + selectedIp + "/db_grupo_05_tarea_16_ejercicio_01/VehiculoActualizar.php";

        stringRequest = new StringRequest(Request.Method.POST, urlWS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();
                if (response.trim().equalsIgnoreCase("actualiza")) {
                    Toast.makeText(requireActivity(), "Vehiculo actualizado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireActivity(), "Vehiculo no se pudo actualizar", Toast.LENGTH_SHORT).show();
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
                parametros.put("numplaca",String.valueOf(numplaca));
                parametros.put("marca", marca);
                parametros.put("modelo",modelo);
                parametros.put("motor",motor);
                parametros.put("f_ano",f_ano);
                parametros.put("idpropietario", String.valueOf(idpropietario));
                parametros.put("idvehiculo", String.valueOf(idvehiculo));

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

    private void showYearPickerDialog(final EditText et_fecha) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, (view, year1, month1, dayOfMonth) -> {
            String selectedDate = String.valueOf(year1);
            et_fecha.setText(selectedDate);
        }, year, 0, 1);

        datePickerDialog.getDatePicker().setCalendarViewShown(false);
        datePickerDialog.getDatePicker().setSpinnersShown(true);
        datePickerDialog.setTitle("Seleccione el año");
        datePickerDialog.show();
    }

    private void showYearPickerDialog2(final EditText et_actualizar_fecha) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, (view, year1, month1, dayOfMonth) -> {
            String selectedDate = String.valueOf(year1);
            et_actualizar_fecha.setText(selectedDate);
        }, year, 0, 1);

        datePickerDialog.getDatePicker().setCalendarViewShown(false);
        datePickerDialog.getDatePicker().setSpinnersShown(true);
        datePickerDialog.setTitle("Seleccione el año");
        datePickerDialog.show();
    }

    private void EliminarWebService(int idvehiculo){
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Eliminando...");
        progressDialog.show();

        // Puedes añadir más IPs según sea necesario
        String selectedIp = "";

        ArrayList<Usuario> usuarios = dbHelper.get_all_Usuarios();
        for (Usuario usuario : usuarios) {
            selectedIp = IPUtilizada.getInstance().getSelectedIP(usuario.getUsername());
            if (selectedIp != null) {
                break;
            }
        }

        String urlWS = "http://" + selectedIp + "/db_grupo_05_tarea_16_ejercicio_01/VehiculoEliminar.php?" + "idvehiculo="+idvehiculo;

        stringRequest = new StringRequest(Request.Method.GET, urlWS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();

                if (response.trim().equalsIgnoreCase("elimina")) {
                    Toast.makeText(requireActivity(), "Vehículo eliminado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    if (response.trim().equalsIgnoreCase("noExiste")) {
                        Toast.makeText(requireActivity(), "No se encuentra el vehículo", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireActivity(), "No se ha eliminado", Toast.LENGTH_SHORT).show();
                    }
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorM = error.getMessage();
                if (error.networkResponse != null) {
                    errorM += " Estado: " + error.networkResponse.statusCode;
                }
                Log.e("EliminarWebService", "Error: " + errorM);
                //Toast.makeText(requireActivity(), "No se ha podido conectar: " + errorM, Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        });
        request.add(stringRequest);
    }

}