package com.example.grupo_05_tarea_16_ejercicio_01.fragments.OficinaGob;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.IPUtilizada;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.OficinaGob;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Usuario;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Vehiculo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgregarOficinaFragment extends Fragment implements OnMapReadyCallback, Response.Listener<JSONObject>, Response.ErrorListener {

    private EditText et_valorVehiculo, et_numPoliza, et_ubicacion;
    private Spinner sp_numPlaca_oficina;
    private String latitud = null, longitud = null;
    private DBHelper dbHelper;
    private GoogleMap mMap;
    private ProgressDialog progressDialog;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

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
        sp_numPlaca_oficina = view.findViewById(R.id.sp_numPlaca_oficina);
        et_ubicacion = view.findViewById(R.id.et_ubicacion);

        request = Volley.newRequestQueue(getContext());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fr_ubicacionOficina);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    private int IdVehiculo;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DBHelper(getContext());
        ArrayList<Vehiculo> vehiculos = dbHelper.get_all_Vehiculos();
        ArrayList<String> tituloVehiculos = new ArrayList<>();
        if (vehiculos != null) {
            for (Vehiculo vehiculo : vehiculos) {
                tituloVehiculos.add(String.valueOf(vehiculo.getNumplaca()));
            }
        }
        ArrayAdapter<String> adapter_placa = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tituloVehiculos);
        adapter_placa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_numPlaca_oficina.setAdapter(adapter_placa);
        sp_numPlaca_oficina.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Vehiculo vehiculo = vehiculos.get(position);
                IdVehiculo = vehiculo.getIdVehiculo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        view.findViewById(R.id.btn_agregarOficina).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarOficina();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Ubicación seleccionada"));
                latitud = String.valueOf(latLng.latitude);
                longitud = String.valueOf(latLng.longitude);
            }
        });

        LatLng inicial = new LatLng(-12.037553, -77.044837);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(inicial, 10));
    }

    public void AgregarOficina() {

        if (et_valorVehiculo.getText().toString().trim().isEmpty() ||
                et_numPoliza.getText().toString().trim().isEmpty() ||
                et_ubicacion.getText().toString().trim().isEmpty()) {

            Toast.makeText(getActivity(), "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (latitud == null && longitud == null) {
            Toast.makeText(getActivity(), "Debe seleccionar una ubicación", Toast.LENGTH_SHORT).show();
            return;
        }

        String valorVehiculo = et_valorVehiculo.getText().toString().trim();
        int numPoliza = Integer.parseInt(et_numPoliza.getText().toString().trim());
        int numPlaca = IdVehiculo;
        String ubicacion = et_ubicacion.getText().toString().trim();

        OficinaGob oficinaGob = new OficinaGob(valorVehiculo, numPoliza, numPlaca, ubicacion, latitud, longitud);
        dbHelper.Insertar_Oficina(oficinaGob);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (isAdded()) { // Check if the fragment is attached
                    requireActivity().getSupportFragmentManager().popBackStack();
                }
            }
        };

        CargarWebService(valorVehiculo, numPoliza, numPlaca, ubicacion, latitud, longitud, runnable);
    }

    private void CargarWebService(String valorvehiculo, int npoliza, int idvehiculo, String ubicacion,
                                  String latitud, String longitud, Runnable runnable) {
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Registrando...");
        progressDialog.show();

        String selectedIp = "";

        ArrayList<Usuario> usuarios = dbHelper.get_all_Usuarios();
        for (Usuario usuario : usuarios) {
            selectedIp = IPUtilizada.getInstance().getSelectedIP(usuario.getUsername());
            if (selectedIp != null) {
                break;
            }
        }

        String urlWS = "http://" + selectedIp + "/db_grupo_05_tarea_16_ejercicio_01/OficinaRegistro.php?" +
                "valorvehiculo=" + valorvehiculo +
                "&npoliza=" + npoliza +
                "&idvehiculo=" + idvehiculo +
                "&ubicacion=" + ubicacion +
                "&latitud=" + latitud +
                "&longitud=" + longitud;

        urlWS = urlWS.replace(" ", "%20");

        //Log.d("URLWebService", "URL: " + urlWS);

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlWS, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.hide();
                Toast.makeText(requireActivity(), "Oficina registrada correctamente", Toast.LENGTH_SHORT).show();
                if (runnable != null) {
                    runnable.run();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(requireActivity(), "No se puede conectar: " + error.toString(), Toast.LENGTH_LONG).show();
                Log.d("ERROR: ", error.toString());
                if (runnable != null) {
                    runnable.run();
                }
            }
        });

        request.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {

    }
}