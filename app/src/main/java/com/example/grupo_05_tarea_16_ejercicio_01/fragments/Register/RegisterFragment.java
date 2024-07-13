package com.example.grupo_05_tarea_16_ejercicio_01.fragments.Register;

import android.os.Bundle;
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

public class RegisterFragment extends Fragment {

    private EditText etUsername, etPassword, etConfirmPassword;
    private Button btnRegister;
    private DBHelper dbHelper;

    public RegisterFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        etUsername = view.findViewById(R.id.et_username);
        etPassword = view.findViewById(R.id.et_password);
        etConfirmPassword = view.findViewById(R.id.et_conf_password);
        btnRegister = view.findViewById(R.id.btn_register);
        dbHelper = new DBHelper(getContext());

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

            Toast.makeText(getContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();

            Navigation.findNavController(requireView()).navigate(R.id.loginFragment);
        });

        return view;
    }
}
