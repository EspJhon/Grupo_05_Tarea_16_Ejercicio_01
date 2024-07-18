package com.example.grupo_05_tarea_16_ejercicio_01.fragments.Zona;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.IPUtilizada;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Acta;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.PuestoControl;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Usuario;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Zona;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ActualizarZonaFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener{

    private EditText edt_actualizar_titulo_zona, edt_actualizar_departamento_zona,
            edt_actualizar_provincia_zona, edt_actualizar_distrito_zona,
            edt_actualizar_latitud_zona, edt_actualizar_longitud_zona;
    private Button btn_eliminar_zona, btn_actualizar_zona;
    DBHelper dbHelper;
    GoogleMap mMap;
    String ctitulo;
    String clatitud;
    String clongitud;
    private ProgressDialog progressDialog;
    RequestQueue request;
    StringRequest stringRequest;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ActualizarZonaFragment() {
        // Required empty public constructor
    }

    public static ActualizarZonaFragment newInstance(String param1, String param2) {
        ActualizarZonaFragment fragment = new ActualizarZonaFragment();
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
        View view = inflater.inflate(R.layout.fragment_actualizar_zona, container, false);
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
        request = Volley.newRequestQueue(getContext());
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
                ActualizarWebService(adepartamento, aprovincia, adistrito, alatitud, alongitud, atitulo, zona.getIdZona());
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
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirmar Eliminación")
                .setMessage("¿Estás seguro de eliminar este Puesto?")
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Zona zona = dbHelper.get_Zona(clatitud,clongitud);
                        if (zona != null) {
                            ArrayList<PuestoControl> comprobars01 = dbHelper.get_all_Puesto_Controls();
                            ArrayList<Acta> comprobar02 = dbHelper.getAllActas();
                            boolean exists = doesIdZonaExist(comprobar02, zona.getIdZona());
                            boolean exists1 = doesIdPuestoExist(comprobars01, zona.getIdZona());
                            if (exists1 || exists) {
                                Toast.makeText(getContext(), "Existen Registros Dependientes", Toast.LENGTH_SHORT).show();
                            } else {
                                dbHelper.Eliminar_Zona(zona);
                                EliminarWebService(zona.getIdZona());

                                if (isAdded()) {
                                    try {
                                        NavController navController = Navigation.findNavController(requireView());
                                        navController.popBackStack();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                                Toast.makeText(getContext(), "Zona Eliminada", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Zona no Eliminada", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancelar", null)
                .create().show();
    }

    public static boolean doesIdZonaExist(ArrayList<Acta> actas, int idZonaToCheck) {
        for (Acta acta : actas) {
            if (acta.getIdZona() == idZonaToCheck) {
                return true;
            }
        }
        return false;
    }
    public static boolean doesIdPuestoExist(ArrayList<PuestoControl> puestoControls, int idZonaToCheck) {
        for (PuestoControl puestoControl : puestoControls) {
            if (puestoControl.getIdZona() == idZonaToCheck) {
                return true;
            }
        }
        return false;
    }

    private void ActualizarWebService(String adepartamento, String aprovincia, String adistrito, String alatitud, String alongitud, String atitulo, int AId_Zona) {
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Actualizando...");
        progressDialog.show();

        // Puedes añadir más IPs según sea necesario
        String selectedIp = "";

        ArrayList<Usuario> usuarios = dbHelper.get_all_Usuarios();
        for (Usuario usuario : usuarios) {
            selectedIp = IPUtilizada.getInstance().getSelectedIP(usuario.getUsername());
            if (selectedIp != null) {
                break;
            }
        }

        String urlWS = "http://" + selectedIp + "/db_grupo_05_tarea_16_ejercicio_01/ZonaActualizar.php";

        stringRequest = new StringRequest(Request.Method.POST, urlWS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();
                if (isAdded()) {
                    if (response.trim().equalsIgnoreCase("actualiza")) {
                        Toast.makeText(requireActivity(), "Zona actualizada correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireActivity(), "Zona no se pudo actualizar", Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ", "" + response);
                    }
                    progressDialog.hide();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (isAdded()) {
                    Toast.makeText(requireActivity(), "No se ha podido conectar", Toast.LENGTH_SHORT).show();
                    progressDialog.hide();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String departamento = adepartamento;
                String provincia = aprovincia;
                String distrito = adistrito;
                String latitud = alatitud;
                String longitud = alongitud;
                String titulo = atitulo;
                int idzona = AId_Zona;

                Map<String,String> parametros = new HashMap<>();
                parametros.put("departamento",departamento);
                parametros.put("provincia", provincia);
                parametros.put("distrito", distrito);
                parametros.put("latitud",latitud);
                parametros.put("longitud",longitud);
                parametros.put("titulo",titulo);
                parametros.put("idzona",String.valueOf(idzona));

                Log.d("Params", "departamento: " + departamento + ", provincia: " + provincia + ", " +
                        "distrito: " + distrito + ", latitud: " + latitud+ ", longitud: " + longitud+ ", " +
                        "titulo: " + titulo + "idzona: " + idzona);
                return parametros;
            }
        };
        request.add(stringRequest);
    }

    private void EliminarWebService(int idZona){
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Eliminando...");
        progressDialog.show();

        // Puedes añadir más IPs según sea necesario
        String selectedIp = "";

        ArrayList<Usuario> usuarios = dbHelper.get_all_Usuarios();
        for (Usuario usuario : usuarios) {
            selectedIp = IPUtilizada.getInstance().getSelectedIP(usuario.getUsername());
            if (selectedIp != null) {
                break;
            }
        }

        String urlWS = "http://" + selectedIp + "/db_grupo_05_tarea_16_ejercicio_01/ZonaEliminar.php?" +
                "idzona="+idZona;

        stringRequest = new StringRequest(Request.Method.GET, urlWS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();
                if (isAdded()) {
                    try {
                        if (response.trim().equalsIgnoreCase("elimina")) {
                            Toast.makeText(requireActivity(), "Zona eliminada correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            if (response.trim().equalsIgnoreCase("noExiste")) {
                                Toast.makeText(requireActivity(), "No se encuentra la Zona", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(requireActivity(), "No se ha eliminado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (isAdded()) {
                    try {
                        String errorMessage = error.getMessage();
                        if (error.networkResponse != null) {
                            errorMessage += " Status Code: " + error.networkResponse.statusCode;
                        }
                        Log.e("EliminarWebService", "Error: " + errorMessage);
                        Toast.makeText(requireActivity(), "No se ha podido conectar: " + errorMessage, Toast.LENGTH_SHORT).show();
                        progressDialog.hide();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        });
        request.add(stringRequest);
    }
}