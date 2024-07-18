package com.example.grupo_05_tarea_16_ejercicio_01.fragments.OficinaGob;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Accidente;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.OficinaGob;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Usuario;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Vehiculo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActualizarOficinaFragment extends Fragment implements OnMapReadyCallback{

    private EditText et_valorVehiculoA, et_numPolizaA, et_ubicacionA;
    private Spinner sp_numPlacaA_oficina;
    private String latitud = null, longitud = null;
    private DBHelper dbHelper;
    private GoogleMap mMap;
    private OficinaGob oficinaGob;
    private Marker marker;
    ArrayAdapter<String> adapter_placa;

    private ProgressDialog progressDialog;
    RequestQueue request;
    StringRequest stringRequest;


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

        View view = inflater.inflate(R.layout.fragment_actualizar_oficina, container, false);

        et_valorVehiculoA = view.findViewById(R.id.et_valorVehiculoA);
        et_numPolizaA = view.findViewById(R.id.et_numPolizaA);
        sp_numPlacaA_oficina = view.findViewById(R.id.sp_numPlacaA_oficina);
        et_ubicacionA = view.findViewById(R.id.et_ubicacionA);

        request = Volley.newRequestQueue(getContext());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fr_ubicacionOficinaA);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }


        return view;
    }

    private int AIdVehiculo;

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
        adapter_placa = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tituloVehiculos);
        adapter_placa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_numPlacaA_oficina.setAdapter(adapter_placa);
        sp_numPlacaA_oficina.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Vehiculo vehiculo = vehiculos.get(position);
                AIdVehiculo = vehiculo.getIdVehiculo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (getArguments() != null && getArguments().getSerializable("id") != null) {
            int id = getArguments().getInt("id");
            oficinaGob = dbHelper.get_Oficina(id);
            CargarDatosRegistrados(oficinaGob);
        }
        view.findViewById(R.id.btn_actualizarOficina).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActualizarOficina();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if (oficinaGob != null) {
            double latitudObtenida = Double.parseDouble(oficinaGob.getLatitud());
            double longitudObtenida = Double.parseDouble(oficinaGob.getLongitud());
            LatLng lugarOficina = new LatLng(latitudObtenida, longitudObtenida);
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(lugarOficina).title("Ubicación de la oficina"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lugarOficina, 15));
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                latitud = String.valueOf(latLng.latitude);
                longitud = String.valueOf(latLng.longitude);
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
                et_ubicacionA.getText().toString().trim().isEmpty()) {

            Toast.makeText(getActivity(), "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        String valorVehiculoA = et_valorVehiculoA.getText().toString().trim();
        int numPolizaA = Integer.parseInt(et_numPolizaA.getText().toString().trim());
        int numPlacaA = AIdVehiculo;
        String ubicacionA = et_ubicacionA.getText().toString().trim();

        oficinaGob.setValor_vehiculo(valorVehiculoA);
        oficinaGob.setNpoliza(numPolizaA);
        oficinaGob.setIdVehiculo(numPlacaA);
        oficinaGob.setUbicacion(ubicacionA);

        if (latitud != null || longitud != null) {
            oficinaGob.setLatitud(latitud);
            oficinaGob.setLongitud(longitud);
        }

        dbHelper.Actualizar_Oficina(oficinaGob);

        ActualizarWebService(valorVehiculoA,numPolizaA,numPlacaA,ubicacionA,latitud,longitud,oficinaGob.getIdOficina());
    }

    private void CargarDatosRegistrados(OficinaGob oficinaGob) {
        Vehiculo vehiculo = dbHelper.get_Vehiculo(oficinaGob.getIdVehiculo());
        int spinnerPosition_vehiculo = adapter_placa.getPosition(String.valueOf(vehiculo.getNumplaca()));
        sp_numPlacaA_oficina.setSelection(spinnerPosition_vehiculo);
        sp_numPlacaA_oficina.setPadding(16, 8, 16, 8);
        et_valorVehiculoA.setText(oficinaGob.getValor_vehiculo());
        et_numPolizaA.setText(String.valueOf(oficinaGob.getNpoliza()));
        et_ubicacionA.setText(oficinaGob.getUbicacion());
        latitud = oficinaGob.getLatitud();
        longitud = oficinaGob.getLongitud();

        if (mMap != null) {
            double latitudObtenida = Double.parseDouble(latitud);
            double longitudObtenida = Double.parseDouble(longitud);
            LatLng lugarOficina = new LatLng(latitudObtenida, longitudObtenida);
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(lugarOficina).title("Ubicación de la oficina"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lugarOficina, 15));
        }
    }

    private void ActualizarWebService(String valorvehiculoA, int npolizaA, int idvehiculoA, String ubicacionA, String latitudA, String longitudA, int idoficinagobA) {
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Actualizando...");
        progressDialog.show();

        List<String> ips = Arrays.asList("192.168.100.15", "192.168.10.106", "192.168.1.6", "192.168.1.2");
        // Puedes añadir más IPs según sea necesario
        String selectedIp = "";
        Map<String, String> userIpMap = new HashMap<>();
        userIpMap.put("jhon", ips.get(0));
        userIpMap.put("chagua", ips.get(1));
        userIpMap.put("matias", ips.get(2));
        userIpMap.put("calixto", ips.get(3));

        ArrayList<Usuario> usuarios = dbHelper.get_all_Usuarios();
        for (Usuario usuario : usuarios) {
            selectedIp = userIpMap.get(usuario.getUsername());
            if (selectedIp != null) {
                break;
            }
        }

        String urlWS = "http://" + selectedIp + "/db_grupo_05_tarea_16_ejercicio_01/OficinaActualizar.php";

        stringRequest = new StringRequest(Request.Method.POST, urlWS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();
                if (response.trim().equalsIgnoreCase("actualiza")) {
                    Toast.makeText(requireActivity(), "Oficina actualizada correctamente", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(requireActivity(), "Oficina no se pudo actualizar", Toast.LENGTH_SHORT).show();
                    Log.i("RESPUESTA: ", "" + response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(requireActivity(), "No se ha podido conectar", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String valorvehiculo = valorvehiculoA;
                int npoliza = npolizaA;
                int idvehiculo = idvehiculoA;
                String ubicacion = ubicacionA;
                String latitud = latitudA;
                String longitud = longitudA;
                int idoficinagob = idoficinagobA;

                Map<String,String> parametros = new HashMap<>();
                parametros.put("valorvehiculo",valorvehiculo);
                parametros.put("npoliza", String.valueOf(npoliza));
                parametros.put("idvehiculo", String.valueOf(idvehiculo));
                parametros.put("ubicacion",ubicacion);
                parametros.put("latitud",latitud);
                parametros.put("longitud",longitud);
                parametros.put("idoficinagob", String.valueOf(idoficinagob));

                return parametros;
            }
        };
        request.add(stringRequest);
    }

}