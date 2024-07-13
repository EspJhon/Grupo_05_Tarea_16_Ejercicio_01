package com.example.grupo_05_tarea_16_ejercicio_01.fragments.Zona;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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

import java.util.ArrayList;

public class MapsZonaFragment extends Fragment implements OnMapReadyCallback{

    DBHelper dbHelper;
    GoogleMap mMap;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public MapsZonaFragment() {
        // Required empty public constructor
    }

    public static MapsZonaFragment newInstance(String param1, String param2) {
        MapsZonaFragment fragment = new MapsZonaFragment();
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
        View view = inflater.inflate(R.layout.fragment_maps_zona, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_zona_general);
        mapFragment.getMapAsync(this);
        return view;
    }
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        dbHelper = new DBHelper(getContext());
        // Add a marker in Sydney and move the camera
        Listar_Marcadores();
        LatLng peru = new LatLng(-12.06735289431661, -77.02150953126328);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(peru));
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(7);
        mMap.setMaxZoomPreference(17);
        LatLngBounds ADELAIDE = new LatLngBounds(
                new LatLng(-16.97447334874591, -80.99752107601148), // Sudoeste
                new LatLng(-0.9913482010088278, -71.86794607118557)  // Noreste
        );
        mMap.setLatLngBoundsForCameraTarget(ADELAIDE);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                mMap.clear();
                Listar_Marcadores();
                LatLng zona = new LatLng(latLng.latitude, latLng.longitude);
                mMap.addMarker(new MarkerOptions().position(zona));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(zona));
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View dialogView = inflater.inflate(R.layout.dialog_zona, null);

                final EditText titulo = dialogView.findViewById(R.id.edt_titulo);
                final EditText latitud = dialogView.findViewById(R.id.edt_latitud);
                final EditText longitud = dialogView.findViewById(R.id.edt_longitud);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Enter Marker Title");
                builder.setView(dialogView);
                latitud.setText(String.valueOf(marker.getPosition().latitude));
                longitud.setText(String.valueOf(marker.getPosition().longitude));
                // Set up the buttons
                builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = titulo.getText().toString();
                        String slatitud = latitud.getText().toString();
                        String slongitud = longitud.getText().toString();
                        double mlatitud = Double.parseDouble(slatitud);
                        double mlongitud = Double.parseDouble(slongitud);
                        LatLng mUbi = new LatLng(mlatitud, mlongitud);
                        mMap.addMarker(new MarkerOptions().position(mUbi).title(title));
                        Zona zona = new Zona("", "", "", slatitud, slongitud, title);
                        dbHelper.Insertar_Zonas(zona);

                        // Navegar de regreso al fragmento anterior
                        try {
                            NavController navController = Navigation.findNavController(getView());
                            navController.navigateUp();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return false;
            }
        });
    }

    public void Listar_Marcadores(){
        ArrayList<Zona> zonas = dbHelper.get_all_Zonas();
        if (zonas != null) {
            for (Zona zona : zonas) {
                LatLng latLng = new LatLng(Double.parseDouble(zona.getLatitud()), Double.parseDouble(zona.getLongitud()));
                mMap.addMarker(new MarkerOptions().position(latLng).title(zona.getTitulo()));
            }
        }
    }
}