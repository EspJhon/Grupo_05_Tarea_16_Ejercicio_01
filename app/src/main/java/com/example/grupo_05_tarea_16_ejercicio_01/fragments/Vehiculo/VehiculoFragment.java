package com.example.grupo_05_tarea_16_ejercicio_01.fragments.Vehiculo;

import android.app.AlertDialog;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.Vehiculo_Adapter;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Propietario;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Vehiculo;

import org.json.JSONObject;

import java.util.ArrayList;


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

    ProgressDialog progreso;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;


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
                mostrarDialogoRegistrarVehiculo();
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

        // Cargar los nombres de los clientes en el Spinner
        cargarPropietariosEnSpinner(spPropietarios);

        builder.setTitle("Registrar Dirección");
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

            cargaWebService(numeroPlacaInt, marca, modelo, motor, fecha, propietario.getIdPropietario());
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
        });
        builder.setNegativeButton("Cancelar", (dialogInterface, which) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void eliminarVehiculo(Vehiculo vehiculo) {
        dbHelper.Eliminar_Vehiculo(vehiculo);

        Toast.makeText(getContext(), "Vehiculo eliminado", Toast.LENGTH_SHORT).show();
        cargarVehiculos();
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

            Toast.makeText(getContext(), "Dirección actualizada", Toast.LENGTH_SHORT).show();
            cargarVehiculos();
        });
        builder.setNegativeButton("Cancelar", (dialogInterface, which) -> {
        });

        AlertDialog dialogActualizar = builder.create();
        dialogActualizar.show();
    }

    private void cargaWebService(int numplaca, String marca, String modelo, String motor, String fecha, int idpropietario) {
        progreso = new ProgressDialog(requireActivity());
        progreso.setMessage("Registrando...");
        progreso.show();

        String url = "http://192.168.1.6/db_grupo_05_tarea_16_ejercicio_01/VehiculoRegistro.php?" +
                "numplaca=" + numplaca +
                "&marca=" + marca +
                "&modelo=" + modelo +
                "&motor=" + motor +
                "&f_ano=" + fecha +
                "&idpropietario=" + idpropietario;

        url = url.replace(" ", "%20");

        Log.d("URLWebService", "URL: " + url);

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        String errorMessage = "No se puede conectar: " + error.toString();
        Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_LONG).show();
        Log.d("ERROR: ", errorMessage);
    }

    @Override
    public void onResponse(JSONObject response) {
        progreso.hide();
        Toast.makeText(requireActivity(), "Vehículo registrado correctamente", Toast.LENGTH_SHORT).show();
        cargarVehiculos();
    }
}