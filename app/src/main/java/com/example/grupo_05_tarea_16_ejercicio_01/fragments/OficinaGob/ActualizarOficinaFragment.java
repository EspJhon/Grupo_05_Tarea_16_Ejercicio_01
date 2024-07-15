package com.example.grupo_05_tarea_16_ejercicio_01.fragments.OficinaGob;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Accidente;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.OficinaGob;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ActualizarOficinaFragment extends Fragment implements OnMapReadyCallback {

    private EditText et_valorVehiculoA, et_numPolizaA, et_numPlacaA, et_ubicacionA;
    private double latitud=0,longitud=0;
    private DBHelper dbHelper;
    private GoogleMap mMap;
    private OficinaGob oficinaGob;
    private Marker marker;


    public ActualizarOficinaFragment() {
        // Required empty public constructor
    }

    public static ActualizarOficinaFragment newInstance(String param1, String param2) {
        ActualizarOficinaFragment fragment = new ActualizarOficinaFragment();
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

        View view =inflater.inflate(R.layout.fragment_actualizar_oficina, container, false);

        et_valorVehiculoA = view.findViewById(R.id.et_valorVehiculoA);
        et_numPolizaA = view.findViewById(R.id.et_numPolizaA);
        et_numPlacaA = view.findViewById(R.id.et_numPlacaA);
        et_ubicacionA = view.findViewById(R.id.et_ubicacionA);

        view.findViewById(R.id.btn_actualizarOficina).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActualizarOficina();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fr_ubicacionOficinaA);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        if (getArguments() != null && getArguments().getSerializable("id") != null) {
            int id = getArguments().getInt("id");
            oficinaGob = dbHelper.get_Oficina(id);
            CargarDatosRegistrados(oficinaGob);
        }

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if (oficinaGob != null) {
            LatLng lugarOficina = new LatLng(oficinaGob.getLatitud(), oficinaGob.getLongitud());
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(lugarOficina).title("Ubicación de la oficina"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lugarOficina, 15));
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                latitud = latLng.latitude;
                longitud = latLng.longitude;
                if (marker != null) {
                    marker.setPosition(latLng);
                } else {
                    marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Nueva ubicación de la oficina"));
                }
            }
        });
    }

    private void ActualizarOficina() {

        if (et_valorVehiculoA.getText().toString().trim().isEmpty() ||
                et_numPolizaA.getText().toString().trim().isEmpty() ||
                et_numPlacaA.getText().toString().trim().isEmpty() ||
                et_ubicacionA.getText().toString().trim().isEmpty()) {

            Toast.makeText(getActivity(), "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        String valorVehiculoA = et_valorVehiculoA.getText().toString().trim();
        int numPolizaA = Integer.parseInt(et_numPolizaA.getText().toString().trim());
        int numPlacaA = Integer.parseInt(et_numPlacaA.getText().toString().trim());
        String ubicacionA = et_ubicacionA.getText().toString().trim();

        oficinaGob.setValor_vehiculo(valorVehiculoA);
        oficinaGob.setNpoliza(numPolizaA);
        oficinaGob.setIdVehiculo(numPlacaA);
        oficinaGob.setUbicacion(ubicacionA);

        if (latitud != 0 || longitud != 0) {
            oficinaGob.setLatitud(latitud);
            oficinaGob.setLongitud(longitud);
        }

        dbHelper.Actualizar_Oficina(oficinaGob);

        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void CargarDatosRegistrados(OficinaGob oficinaGob) {
        et_valorVehiculoA.setText(oficinaGob.getValor_vehiculo());
        et_numPolizaA.setText(String.valueOf(oficinaGob.getNpoliza()));
        et_numPlacaA.setText(String.valueOf(oficinaGob.getIdVehiculo()));
        et_ubicacionA.setText(oficinaGob.getUbicacion());
        latitud = oficinaGob.getLatitud();
        longitud = oficinaGob.getLongitud();

        if (mMap != null) {
            LatLng lugarOficina = new LatLng(latitud, longitud);
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(lugarOficina).title("Ubicación de la oficina"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lugarOficina, 15));
        }
    }

}