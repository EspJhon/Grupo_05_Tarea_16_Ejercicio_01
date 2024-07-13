package com.example.grupo_05_tarea_16_ejercicio_01.fragments.Zona;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.PuestoControl;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Zona;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.grupo_05_tarea_16_ejercicio_01.databinding.ActivityMapsZonaBinding;

import java.util.ArrayList;

public class MapsZona extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DBHelper dbHelper;
    private ActivityMapsZonaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsZonaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        dbHelper = new DBHelper(this);
        // Add a marker in Sydney and move the camera
        ArrayList<Zona> zonas = dbHelper.get_all_Zonas();
        if (zonas != null) {
            for (Zona zona : zonas) {
                LatLng latLng = new LatLng(Double.parseDouble(zona.getLatitud()), Double.parseDouble(zona.getLongitud()));
                mMap.addMarker(new MarkerOptions().position(latLng).title(zona.getTitulo()));
            }
        }
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
                LatLng zona = new LatLng(latLng.latitude, latLng.longitude);
                mMap.addMarker(new MarkerOptions().position(zona));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(zona));
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                LayoutInflater inflater = LayoutInflater.from(MapsZona.this);
                View dialogView = inflater.inflate(R.layout.dialog_zona, null);

                final EditText titulo = dialogView.findViewById(R.id.edt_titulo);
                final EditText latitud = dialogView.findViewById(R.id.edt_latitud);
                final EditText longitud = dialogView.findViewById(R.id.edt_longitud);

                AlertDialog.Builder builder = new AlertDialog.Builder(MapsZona.this);
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
                        Zona zona = new Zona("","","",slatitud, slongitud, title);
                        dbHelper.Insertar_Zonas(zona);
                        NavController navController = Navigation.findNavController(MapsZona.this, R.id.zonaFragment);
                        navController.navigateUp();
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
}