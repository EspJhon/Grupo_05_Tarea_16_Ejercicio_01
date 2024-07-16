package com.example.grupo_05_tarea_16_ejercicio_01.fragments.Propietario;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.Propietario_Adapter;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Propietario;

import org.json.JSONObject;

import java.util.ArrayList;

public class PropietarioFragment extends Fragment implements Propietario_Adapter.OnItemLongClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

    private ListView lv_propietarios;
    private EditText et_cedula, et_nombre, et_ciudad;
    private DBHelper dbHelper;
    private ArrayList<Propietario> datos;
    private Dialog dialog;
    private Propietario propietarioSeleccionado;
    private Button btnLogout;

    ProgressDialog progreso;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    public PropietarioFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_propietario, container, false);

        dbHelper = new DBHelper(requireActivity());
        request = Volley.newRequestQueue(requireActivity());  // Inicializar RequestQueue

        //LLAMADA AL DIALOG REGISTRAR
        view.findViewById(R.id.btn_registrar_propietario).setOnClickListener(this::onClick);
        lv_propietarios = view.findViewById(R.id.lv_propietarios);
        btnLogout = view.findViewById(R.id.btn_logout);

        Listar();

        dialog = new Dialog(requireActivity());
        dialog.setContentView(R.layout.propietario_onitemlong);

        //LLAMADA AL DIALOG ELIMINAR
        Button btnEliminar = dialog.findViewById(R.id.btn_eliminar);
        btnEliminar.setOnClickListener(v -> mostrarDialogoConfirmacionEliminar());
        //LLAMADA AL DIALOG ACTUALIZAR
        Button btnActualizar = dialog.findViewById(R.id.btn_actualizar);
        btnActualizar.setOnClickListener(v -> mostrarDialogoActualizar());
        Button btnVolver = dialog.findViewById(R.id.btn_volver);
        btnVolver.setOnClickListener(v -> dialog.dismiss());

        btnLogout.setOnClickListener(v -> logoutUser());

        // Implementar OnBackPressedCallback
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // No hacer nada para deshabilitar el botón de retroceso
            }
        });

        return view;
    }

    private void logoutUser() {
        SharedPreferences sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();

        Navigation.findNavController(requireView()).navigate(R.id.loginFragment);
    }

    private void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_registro_propietario, null);
        builder.setView(dialogView);

        et_cedula = dialogView.findViewById(R.id.et_cedula);
        et_nombre = dialogView.findViewById(R.id.et_nombre);
        et_ciudad = dialogView.findViewById(R.id.et_ciudad);

        builder.setTitle("Registrar Propietario");
        builder.setPositiveButton("Registrar", (dialogInterface, which) -> {
            String cedula = et_cedula.getText().toString();
            String nombre = et_nombre.getText().toString();
            String ciudad = et_ciudad.getText().toString();

            if (cedula.isEmpty() || nombre.isEmpty() || ciudad.isEmpty()) {
                Toast.makeText(getContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            int cedulaInt;
            try {
                cedulaInt = Integer.parseInt(cedula);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Campo CÉDULA debe ser valor numérico", Toast.LENGTH_SHORT).show();
                return;
            }

            Propietario propietario = new Propietario(cedulaInt, nombre, ciudad);
            Registrar(propietario);
            Log.d("RegistroPropietario", "Cedula: " + cedula + ", Nombre: " + nombre + ", Ciudad: " + ciudad);
            cargaWebService(cedula, nombre, ciudad);
            Toast.makeText(getContext(), "Cliente registrado", Toast.LENGTH_SHORT).show();
            Listar();
        });
        builder.setNegativeButton("Cancelar", (dialogInterface, which) -> {
        });

        AlertDialog dialogCliente = builder.create();
        dialogCliente.show();
    }

    //LISTAR EN EL LISTVIEW
    private void Listar() {
        datos = dbHelper.get_all_Propietarios();
        Propietario_Adapter adapter = new Propietario_Adapter(requireActivity(), datos, this);
        lv_propietarios.setAdapter(adapter);
    }

    //MÉTODO REGISTRAR QUE ES LLAMADO EN EL ONCLICK
    private void Registrar(Propietario propietario) {
        dbHelper.Insertar_Propietario(propietario);
        Listar();
        et_cedula.setText("");
        et_nombre.setText("");
        et_ciudad.setText("");
    }

    //MÉTODO LONG CLICK
    @Override
    public void onItemLongClick(Propietario propietario) {
        propietarioSeleccionado = propietario;
        TextView tvDialogCedula = dialog.findViewById(R.id.tv_dialog_cedula);
        TextView tvDialogNombre = dialog.findViewById(R.id.tv_dialog_nombre);

        tvDialogCedula.setText(String.valueOf(propietario.getCedulap()));
        tvDialogNombre.setText(propietario.getNombre());

        dialog.show();
    }

    //MÉTODOS DE ELIMINACIÓN

    private void mostrarDialogoConfirmacionEliminar() {
        dialog.dismiss();

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Borrar Cliente");
        builder.setMessage("¿Estás seguro de que deseas borrar este cliente?");
        builder.setPositiveButton("Aceptar", (dialogInterface, which) -> {
            eliminarPropietario(propietarioSeleccionado);
        });
        builder.setNegativeButton("Cancelar", (dialogInterface, which) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void eliminarPropietario(Propietario propietario) {
        dbHelper.Eliminar_Propietario(propietario);
        Listar();
        Toast.makeText(requireActivity(), "Propietario borrado: " + propietario.getNombre(), Toast.LENGTH_SHORT).show();
    }

    //MÉTODOS DE ACTUALIZACIÓN

    private void mostrarDialogoActualizar() {
        dialog.dismiss();

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.propietario_actualizar, null);
        builder.setView(view);

        EditText etActualizarCedula = view.findViewById(R.id.et_actualizar_cedula);
        EditText etActualizarNombre = view.findViewById(R.id.et_actualizar_nombre);
        EditText etActualizarCiudad = view.findViewById(R.id.et_actualizar_ciudad);

        etActualizarCedula.setText(String.valueOf(propietarioSeleccionado.getCedulap()));
        etActualizarNombre.setText(propietarioSeleccionado.getNombre());
        etActualizarCiudad.setText(propietarioSeleccionado.getCiudad());

        builder.setTitle("Editar Propietario");
        builder.setPositiveButton("Editar", (dialogInterface, which) -> {
            int nuevaCedula = Integer.parseInt(etActualizarCedula.getText().toString());
            String nuevoNombre = etActualizarNombre.getText().toString();
            String nuevaCiudad = etActualizarCiudad.getText().toString();

            propietarioSeleccionado.setCedulap(nuevaCedula);
            propietarioSeleccionado.setNombre(nuevoNombre);
            propietarioSeleccionado.setCiudad(nuevaCiudad);

            actualizarPropietario(propietarioSeleccionado);
        });
        builder.setNegativeButton("Cancelar", (dialogInterface, which) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void actualizarPropietario(Propietario propietario) {
        dbHelper.Actualizar_Propietario(propietario);
        Listar();
        Toast.makeText(requireActivity(), "Propietario actualizado: " + propietario.getNombre(), Toast.LENGTH_SHORT).show();
    }

    //WEB SERVICE
    private void cargaWebService(String cedula, String nombre, String ciudad) {
        progreso = new ProgressDialog(requireActivity());
        progreso.setMessage("Cargando...");
        progreso.show();

        String url = "http://192.168.10.106/db_grupo_05_tarea_16_ejercicio_01/PropietarioRegistro.php?cedulap=" + cedula + "&nombre=" + nombre + "&ciudad=" + ciudad;

        url = url.replace(" ", "%20");

        Log.d("URLWebService", "URL: " + url);

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
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
