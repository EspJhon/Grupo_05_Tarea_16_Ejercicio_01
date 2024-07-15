package com.example.grupo_05_tarea_16_ejercicio_01.fragments.Accidente;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.MapMoveFragment;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Accidente;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ActualizarAccidenteFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback {

    private EditText et_placaA, et_agenteA, et_horaA, et_fechaA, et_descripcionA_accidente, et_tituloA_accidente,
            et_nombreLugarA;
    private double latitud=0,longitud=0;
    private ImageView iv_imagenAccidenteA;
    private DBHelper dbHelper;
    private String URL;
    private GoogleMap mMap;
    private Accidente accidente;
    private Marker marker;
    private ScrollView scrollView;

    public ActualizarAccidenteFragment() {
        // Required empty public constructor
    }

    public static ActualizarAccidenteFragment newInstance(String param1, String param2) {
        ActualizarAccidenteFragment fragment = new ActualizarAccidenteFragment();
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

        View view =inflater.inflate(R.layout.fragment_actualizar_accidente, container, false);

        scrollView = view.findViewById(R.id.sv_accidenteA);

        et_placaA = view.findViewById(R.id.et_placaA);
        et_agenteA = view.findViewById(R.id.et_agenteA);
        et_horaA = view.findViewById(R.id.et_horaA);
        et_fechaA = view.findViewById(R.id.et_fechaA);
        et_tituloA_accidente = view.findViewById(R.id.et_tituloA_accidente);
        et_descripcionA_accidente = view.findViewById(R.id.et_descripcionA_accidente);
        et_nombreLugarA = view.findViewById(R.id.et_nombreLugarA);
        iv_imagenAccidenteA = view.findViewById(R.id.iv_imagenAccidenteA);

        view.findViewById(R.id.btn_tomarFotoA).setOnClickListener(this::onClick);
        view.findViewById(R.id.btn_subirFotoA).setOnClickListener(this::onClick);
        view.findViewById(R.id.btn_actualizarAccidente).setOnClickListener(this::onClick);

        MapMoveFragment mapMoveFragment = (MapMoveFragment) getChildFragmentManager().findFragmentById(R.id.fr_ubicacionAccidenteA);
        if (mapMoveFragment != null){
            mapMoveFragment.getMapAsync(this);
            mapMoveFragment.setListener(new MapMoveFragment.OnTouchListener() {
                @Override
                public void onTouch() {
                    scrollView.requestDisallowInterceptTouchEvent(true);
                }
            });
        }

        if (getArguments() != null && getArguments().getSerializable("id") != null) {
            int id = getArguments().getInt("id");
            accidente = dbHelper.get_Accidente(id);
            CargarDatosRegistrados(accidente);
        }

        return view;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_tomarFotoA) {
            ActivarCam();
        } else if (v.getId() == R.id.btn_actualizarAccidente) {
            ActualizarAccidente();
        }else if (v.getId() == R.id.btn_subirFotoA) {
            SubirFoto();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111111 && resultCode == requireActivity().RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            URL = GuardarURL(imageBitmap);
            iv_imagenAccidenteA.setImageBitmap(imageBitmap);
        } else if (requestCode == 222222 && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                URL = GuardarURL(selectedImage);
                iv_imagenAccidenteA.setImageBitmap(selectedImage);
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if (accidente != null) {
            LatLng lugarAccidente = new LatLng(accidente.getLatitud(), accidente.getLongitud());
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(lugarAccidente).title("Ubicación del accidente"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lugarAccidente, 15));
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                latitud = latLng.latitude;
                longitud = latLng.longitude;
                if (marker != null) {
                    marker.setPosition(latLng);
                } else {
                    marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Nueva ubicación del accidente"));
                }
            }
        });
    }

    private void ActualizarAccidente() {

        if (et_placaA.getText().toString().trim().isEmpty() || et_agenteA.getText().toString().trim().isEmpty() ||
                et_horaA.getText().toString().trim().isEmpty() || et_fechaA.getText().toString().trim().isEmpty() ||
                et_descripcionA_accidente.getText().toString().trim().isEmpty() ||
                et_tituloA_accidente.getText().toString().trim().isEmpty() ||
                et_nombreLugarA.getText().toString().trim().isEmpty()) {

            Toast.makeText(getActivity(), "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int placaA = Integer.parseInt(et_placaA.getText().toString().trim());
        int agenteA = Integer.parseInt(et_agenteA.getText().toString().trim());
        String horaA = et_horaA.getText().toString().trim();
        String fechaA = et_fechaA.getText().toString().trim();
        String tituloA = et_tituloA_accidente.getText().toString().trim();
        String descripcionA = et_descripcionA_accidente.getText().toString().trim();
        String lugarA = et_nombreLugarA.getText().toString().trim();

        accidente.setIdVehiculo(placaA);
        accidente.setIdagente(agenteA);
        accidente.setHora(horaA);
        accidente.setFecha(fechaA);
        accidente.setTitulo(tituloA);
        accidente.setDescripcion(descripcionA);
        accidente.setURLimagen(URL);
        accidente.setNombreLugar(lugarA);
        if (latitud != 0 || longitud != 0) {
            accidente.setLatitud(latitud);
            accidente.setLongitud(longitud);
        }

        dbHelper.Actualizar_Accidente(accidente);

        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void ActivarCam() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(intent,111111);
        }
    }

    private void SubirFoto(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(intent, 222222);
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

    private void CargarDatosRegistrados(Accidente accidente) {
        et_placaA.setText(String.valueOf(accidente.getIdVehiculo()));
        et_agenteA.setText(String.valueOf(accidente.getIdagente()));
        et_horaA.setText(accidente.getHora());
        et_fechaA.setText(accidente.getFecha());
        et_tituloA_accidente.setText(accidente.getTitulo());
        et_descripcionA_accidente.setText(accidente.getDescripcion());
        et_nombreLugarA.setText(accidente.getNombreLugar());
        latitud = accidente.getLatitud();
        longitud = accidente.getLongitud();
        URL = accidente.getURLimagen();

        Bitmap bitmap = BitmapFactory.decodeFile(URL);
        iv_imagenAccidenteA.setImageBitmap(bitmap);

        if (mMap != null) {
            LatLng lugarAccidente = new LatLng(latitud, longitud);
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(lugarAccidente).title("Ubicación del accidente"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lugarAccidente, 15));
        }
    }


}
