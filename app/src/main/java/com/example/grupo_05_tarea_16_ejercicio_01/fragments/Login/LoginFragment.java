package com.example.grupo_05_tarea_16_ejercicio_01.fragments.Login;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Usuario;

public class LoginFragment extends Fragment {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin, btnIrARegistro;
    private DBHelper dbHelper;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        editTextUsername = root.findViewById(R.id.et_username);
        editTextPassword = root.findViewById(R.id.et_password);
        buttonLogin = root.findViewById(R.id.btn_login);
        btnIrARegistro = root.findViewById(R.id.btn_IrARegistro);

        dbHelper = new DBHelper(getContext());

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        btnIrARegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.registerFragment);
            }
        });


        return root;
    }

    private void loginUser() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Usuario y Contraseña son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario usuario = new Usuario(username, password);

        boolean usuarioValido = dbHelper.validarUsuario(usuario);

        if (usuarioValido) {
            Toast.makeText(getContext(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(requireView()).navigate(R.id.propietarioFragment);
        } else {
            Toast.makeText(getContext(), "Usuario o Contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
    }
}
