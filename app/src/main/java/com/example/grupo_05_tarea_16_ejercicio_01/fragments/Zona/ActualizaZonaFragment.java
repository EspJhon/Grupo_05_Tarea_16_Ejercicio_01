package com.example.grupo_05_tarea_16_ejercicio_01.fragments.Zona;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
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
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Zona;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ActualizaZonaFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener{

    private EditText edt_actualizar_titulo_zona, edt_actualizar_departamento_zona,
                    edt_actualizar_provincia_zona, edt_actualizar_distrito_zona,
                    edt_actualizar_latitud_zona, edt_actualizar_longitud_zona;
    private Button btn_eliminar_zona, btn_actualizar_zona;
    DBHelper dbHelper;
    GoogleMap mMap;
    String ctitulo;
    String clatitud;
    String clongitud;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ActualizaZonaFragment() {
        // Required empty public constructor
    }

    public static ActualizaZonaFragment newInstance(String param1, String param2) {
        ActualizaZonaFragment fragment = new ActualizaZonaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_actualiza_zona, container, false);
        edt_actualizar_titulo_zona = view.findViewById(R.id.edt_actualizar_titulo_zona);
        edt_actualizar_departamento_zona = view.findViewById(R.id.edt_actualizar_departamento_zona);
        edt_actualizar_provincia_zona = view.findViewById(R.id.edt_actualizar_provincia_zona);
        edt_actualizar_distrito_zona = view.findViewById(R.id.edt_actualizar_distrito_zona);
        edt_actualizar_latitud_zona = view.findViewById(R.id.edt_actualizar_latitud_zona);
        edt_actualizar_longitud_zona = view.findViewById(R.id.edt_actualizar_longitud_zona);
        btn_eliminar_zona = view.findViewById(R.id.btn_eliminar_zona);
        btn_actualizar_zona = view.findViewById(R.id.btn_actualizar_zona);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_zona);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DBHelper(getContext());
        ctitulo = getArguments().getString("ztitulo");
        clatitud = getArguments().getString("zlatitud");
        clongitud = getArguments().getString("zlongitud");
        Zona zona = dbHelper.get_Zona(clatitud,clongitud);
        edt_actualizar_titulo_zona.setText(String.valueOf(zona.getTitulo()));
        edt_actualizar_departamento_zona.setText(String.valueOf(zona.getDepartamento()));
        edt_actualizar_provincia_zona.setText(String.valueOf(zona.getProvincia()));
        edt_actualizar_distrito_zona.setText(String.valueOf(zona.getDistrito()));
        edt_actualizar_latitud_zona.setText(String.valueOf(zona.getLatitud()));
        edt_actualizar_longitud_zona.setText(String.valueOf(zona.getLongitud()));
        btn_actualizar_zona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarZona();
            }
        });
        btn_eliminar_zona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarZona();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        double latitude = Double.parseDouble(clatitud);
        double longitude = Double.parseDouble(clongitud);
        LatLng latLng = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(latLng).title(ctitulo));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(15);
        mMap.setMaxZoomPreference(17);
        LatLngBounds ADELAIDE = new LatLngBounds(
                new LatLng(-16.97447334874591, -80.99752107601148), // Sudoeste
                new LatLng(-0.9913482010088278, -71.86794607118557)  // Noreste
        );
        mMap.setLatLngBoundsForCameraTarget(ADELAIDE);
        this.mMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        edt_actualizar_latitud_zona.setText(String.valueOf(latLng.latitude));
        edt_actualizar_longitud_zona.setText(String.valueOf(latLng.longitude));
        mMap.clear();
        LatLng nlatLng = new LatLng(latLng.latitude, latLng.longitude);
        mMap.addMarker(new MarkerOptions().position(nlatLng).title(ctitulo));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(nlatLng));
    }
    private void actualizarZona() {
        String atitulo = edt_actualizar_titulo_zona.getText().toString();
        String adepartamento = edt_actualizar_departamento_zona.getText().toString();
        String aprovincia = edt_actualizar_provincia_zona.getText().toString();
        String adistrito = edt_actualizar_distrito_zona.getText().toString();
        String alatitud = edt_actualizar_latitud_zona.getText().toString();
        String alongitud = edt_actualizar_longitud_zona.getText().toString();
        if (TextUtils.isEmpty(atitulo) || TextUtils.isEmpty(adepartamento) || TextUtils.isEmpty(aprovincia) || TextUtils.isEmpty(adistrito) || TextUtils.isEmpty(alatitud) || TextUtils.isEmpty(alongitud)) {
            Toast.makeText(getContext(), "Por favor complete los campos", Toast.LENGTH_SHORT).show();
        } else {
            Zona zona = dbHelper.get_Zona(clatitud,clongitud);
            if (zona != null) {
                zona.setDepartamento(adepartamento);
                zona.setProvincia(aprovincia);
                zona.setDistrito(adistrito);
                zona.setLatitud(alatitud);
                zona.setLongitud(alongitud);
                zona.setTitulo(atitulo);
                dbHelper.Actualizar_Ubicacion(zona);
                zona = null;
                Toast.makeText(getContext(), "Zona Actualizada", Toast.LENGTH_SHORT).show();
                NavController navController = Navigation.findNavController(requireView());
                navController.popBackStack();
            } else {
                Toast.makeText(getContext(), "Zona no Actualizada", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void eliminarZona() {
        Zona zona = dbHelper.get_Zona(clatitud,clongitud);
        if (zona != null) {
            dbHelper.Eliminar_Zona(zona);
            Toast.makeText(getContext(), "Zona Eliminada", Toast.LENGTH_SHORT).show();
            NavController navController = Navigation.findNavController(requireView());
            navController.popBackStack();
        } else {
            Toast.makeText(getContext(), "Zona no Eliminada", Toast.LENGTH_SHORT).show();
        }
    }
}