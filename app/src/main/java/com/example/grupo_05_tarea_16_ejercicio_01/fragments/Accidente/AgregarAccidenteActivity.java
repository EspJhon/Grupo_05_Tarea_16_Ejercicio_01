package com.example.grupo_05_tarea_16_ejercicio_01.fragments.Accidente;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Accidente;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class AgregarAccidenteActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_placa, et_agente, et_hora, et_fecha, et_descripcion,
            et_nombreLugar, et_latitud, et_longitud;
    private ImageView iv_imagenAccidente;
    private DBHelper dbHelper;
    private String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_accidente);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DBHelper(getBaseContext());

        et_placa = findViewById(R.id.et_placa);
        et_agente = findViewById(R.id.et_agente);
        et_hora = findViewById(R.id.et_hora);
        et_fecha = findViewById(R.id.et_fecha);
        et_descripcion = findViewById(R.id.et_descripcion);
        et_nombreLugar = findViewById(R.id.et_nombreLugar);
        et_latitud = findViewById(R.id.et_latitud);
        et_longitud = findViewById(R.id.et_longitud);
        iv_imagenAccidente = findViewById(R.id.iv_imagenAccidente);

        findViewById(R.id.btn_tomarFoto).setOnClickListener(this::onClick);
        findViewById(R.id.btn_agregarAccidente).setOnClickListener(this::onClick);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_tomarFoto) {
            ActivarCam();
        } else if (v.getId() == R.id.btn_agregarAccidente) {
            AgregarAccidente();
            finish();
        }
    }

    public void AgregarAccidente(){
        int placa = Integer.parseInt(et_placa.getText().toString().trim());
        int agente = Integer.parseInt(et_agente.getText().toString().trim());
        String hora = et_hora.getText().toString().trim();
        String fecha = et_fecha.getText().toString().trim();
        String descripcion = et_descripcion.getText().toString().trim();

        String lugar = et_nombreLugar.getText().toString().trim();
        double latitud = Double.parseDouble(et_latitud.getText().toString().trim());
        double longitud = Double.parseDouble(et_longitud.getText().toString().trim());

        Accidente accidente = new Accidente(placa,agente,hora,fecha,descripcion,URL,lugar,latitud,longitud);
        dbHelper.Insertar_Accidente(accidente);

    }

    private void ActivarCam() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent,99999999);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99999999 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap foto = (Bitmap) extras.get("data");

            URL = GuardarURL(foto);

            iv_imagenAccidente.setImageBitmap(foto);
        }
    }

    private String GuardarURL(Bitmap bitmap) {
        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());

        File directory = wrapper.getDir("images", Context.MODE_PRIVATE);
        String fileName = "imagen_" + System.currentTimeMillis() + ".jpg";
        File file = new File(directory, fileName);
        try (OutputStream stream = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
        } catch (Exception e) {
            Log.e("IMPORTANTE", "Error al guardar la imagen", e);
        }

        return file.getAbsolutePath();
    }



}