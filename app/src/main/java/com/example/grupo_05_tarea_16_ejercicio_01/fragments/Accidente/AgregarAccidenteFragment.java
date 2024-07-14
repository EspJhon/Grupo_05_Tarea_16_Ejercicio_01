package com.example.grupo_05_tarea_16_ejercicio_01.fragments.Accidente;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Accidente;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class AgregarAccidenteFragment extends Fragment implements View.OnClickListener,OnMapReadyCallback{

    private EditText et_placa, et_agente, et_hora, et_fecha, et_descripcion,
            et_nombreLugar;
    private double latitud=0,longitud=0;
    private ImageView iv_imagenAccidente;
    private DBHelper dbHelper;
    private String URL;
    private GoogleMap mMap;

    public AgregarAccidenteFragment() {
        // Required empty public constructor
    }

    public static AgregarAccidenteFragment newInstance(String param1, String param2) {
        AgregarAccidenteFragment fragment = new AgregarAccidenteFragment();
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

        View view = inflater.inflate(R.layout.fragment_agregar_accidente, container, false);

        dbHelper = new DBHelper(getActivity());

        et_placa = view.findViewById(R.id.et_placa);
        et_agente = view.findViewById(R.id.et_agente);
        et_hora = view.findViewById(R.id.et_hora);
        et_fecha = view.findViewById(R.id.et_fecha);
        et_descripcion = view.findViewById(R.id.et_descripcion);
        et_nombreLugar = view.findViewById(R.id.et_nombreLugar);
        iv_imagenAccidente = view.findViewById(R.id.iv_imagenAccidente);

        view.findViewById(R.id.btn_tomarFoto).setOnClickListener(this::onClick);
        view.findViewById(R.id.btn_agregarAccidente).setOnClickListener(this::onClick);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fr_ubicacionAccidente);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_tomarFoto) {
            ActivarCam();
        } else if (v.getId() == R.id.btn_agregarAccidente) {
            AgregarAccidente();
        }
    }

    public void AgregarAccidente(){
        int placa = Integer.parseInt(et_placa.getText().toString().trim());
        int agente = Integer.parseInt(et_agente.getText().toString().trim());
        String hora = et_hora.getText().toString().trim();
        String fecha = et_fecha.getText().toString().trim();
        String descripcion = et_descripcion.getText().toString().trim();

        String lugar = et_nombreLugar.getText().toString().trim();

        Accidente accidente = new Accidente(placa,agente,hora,fecha,descripcion,URL,lugar,latitud,longitud);
        dbHelper.Insertar_Accidente(accidente);

        requireActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99999999 && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap foto = (Bitmap) extras.get("data");

            URL = GuardarURL(foto);

            iv_imagenAccidente.setImageBitmap(foto);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Ubicación seleccionada"));
                latitud=latLng.latitude;
                longitud=latLng.longitude;
            }
        });

        LatLng inicial = new LatLng(-12.037553, -77.044837);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(inicial, 5));
    }

    private void ActivarCam() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(intent,99999999);
        }
    }

    private String GuardarURL(Bitmap bitmap) {
        ContextWrapper wrapper = new ContextWrapper(getActivity());

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