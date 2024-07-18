package com.example.grupo_05_tarea_16_ejercicio_01.fragments.Register;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Usuario;

import org.json.JSONObject;

public class RegisterFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    private EditText etUsername, etPassword, etConfirmPassword;
    private Button btnRegister;
    private DBHelper dbHelper;

    ProgressDialog progreso;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    public RegisterFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        etUsername = view.findViewById(R.id.et_username);
        etPassword = view.findViewById(R.id.et_password);
        etConfirmPassword = view.findViewById(R.id.et_conf_password);
        btnRegister = view.findViewById(R.id.btn_register);
        dbHelper = new DBHelper(getContext());
        request = Volley.newRequestQueue(requireActivity());

        btnRegister.setOnClickListener(v -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(getContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(getContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            Usuario usuario = new Usuario(username, password);
            dbHelper.insertarUsuario(usuario);
            cargaWebService(username,password);
            Toast.makeText(getContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();

            Navigation.findNavController(requireView()).navigate(R.id.loginFragment);
        });

        return view;
    }

    //WEB SERVICE
    private void cargaWebService(String usuario, String contrasena) {
        progreso = new ProgressDialog(requireActivity());
        progreso.setMessage("Cargando...");
        progreso.show();

        String url = "http://192.168.10.106/db_grupo_05_tarea_16_ejercicio_01/UsuarioRegistro.php?usuario=" + usuario + "&contrasena=" + contrasena;

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
