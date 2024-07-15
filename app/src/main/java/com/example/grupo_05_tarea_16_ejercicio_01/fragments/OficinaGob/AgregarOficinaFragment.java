package com.example.grupo_05_tarea_16_ejercicio_01.fragments.OficinaGob;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.android.gms.maps.model.MarkerOptions;

public class AgregarOficinaFragment extends Fragment implements OnMapReadyCallback {

    private EditText et_valorVehiculo, et_numPoliza, et_numPlaca, et_ubicacion;
    private double latitud=0,longitud=0;
    private DBHelper dbHelper;
    private GoogleMap mMap;

    public AgregarOficinaFragment() {
        // Required empty public constructor
    }

    public static AgregarOficinaFragment newInstance(String param1, String param2) {
        AgregarOficinaFragment fragment = new AgregarOficinaFragment();
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
        View view = inflater.inflate(R.layout.fragment_agregar_oficina, container, false);

        dbHelper = new DBHelper(getActivity());

        et_valorVehiculo = view.findViewById(R.id.et_valorVehiculo);
        et_numPoliza = view.findViewById(R.id.et_numPoliza);
        et_numPlaca = view.findViewById(R.id.et_numPlaca);
        et_ubicacion = view.findViewById(R.id.et_ubicacion);

        view.findViewById(R.id.btn_agregarOficina).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarOficina();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fr_ubicacionOficina);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(inicial, 10));
    }

    public void AgregarOficina(){

        if (et_valorVehiculo.getText().toString().trim().isEmpty() ||
                et_numPoliza.getText().toString().trim().isEmpty() ||
                et_numPlaca.getText().toString().trim().isEmpty() ||
                et_ubicacion.getText().toString().trim().isEmpty()) {

            Toast.makeText(getActivity(), "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (latitud==0 && longitud==0){
            Toast.makeText(getActivity(), "Debe seleccionar una ubicación", Toast.LENGTH_SHORT).show();
            return;
        }

        String valorVehiculo = et_valorVehiculo.getText().toString().trim();
        int numPoliza = Integer.parseInt(et_numPoliza.getText().toString().trim());
        int numPlaca = Integer.parseInt(et_numPlaca.getText().toString().trim());
        String ubicacion = et_ubicacion.getText().toString().trim();

        OficinaGob oficinaGob = new OficinaGob(valorVehiculo,numPoliza,numPlaca,ubicacion,latitud,longitud);
        dbHelper.Insertar_Oficina(oficinaGob);

        requireActivity().getSupportFragmentManager().popBackStack();
    }
}