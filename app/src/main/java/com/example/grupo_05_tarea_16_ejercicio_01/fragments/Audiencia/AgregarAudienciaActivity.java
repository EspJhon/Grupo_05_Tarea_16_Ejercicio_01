package com.example.grupo_05_tarea_16_ejercicio_01.fragments.Audiencia;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Accidente;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Audiencia;

public class AgregarAudienciaActivity extends AppCompatActivity {

    private EditText et_lugar, et_fecha, et_hora, et_codigo;
    private Button btn_agregarAudiencia;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_audiencia);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DBHelper(getBaseContext());

        et_lugar = findViewById(R.id.et_lugar);
        et_fecha = findViewById(R.id.et_fecha);
        et_hora = findViewById(R.id.et_hora);
        et_codigo = findViewById(R.id.et_codigo);
        btn_agregarAudiencia = findViewById(R.id.btn_agregarAudiencia);

        btn_agregarAudiencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarAudiencia();
                finish();
            }
        });

    }

    public void AgregarAudiencia(){
        int codigo = Integer.parseInt(et_codigo.getText().toString().trim());
        String lugar = et_lugar.getText().toString().trim();
        String fecha = et_fecha.getText().toString().trim();
        String hora = et_hora.getText().toString().trim();

        Audiencia audiencia = new Audiencia(codigo,lugar,fecha,hora);
        dbHelper.Insertar_Audiencia(audiencia);

    }

}