package com.example.grupo_05_tarea_16_ejercicio_01.fragments.PuestoControl;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.IPUtilizada;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Infraccion;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.PuestoControl;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Usuario;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Zona;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterPuestoControlFragment extends Fragment implements OnMapReadyCallback{

    private EditText edt_register_titulo_puesto_control, edt_register_latitud_puesto_control,
            edt_register_longitud_puesto_control, edt_register_referencia_puesto_control;
    private Button btn_registrar_puesto_control, btn_finalizar_puesto_control;
    DBHelper dbHelper;
    Spinner sp_zona;
    GoogleMap mMap;
    Marker currentMarker;
    ProgressDialog progreso;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;
    private int idPuestoControlSeleccionado = 0;
    boolean modoEdicion;
    private LinearLayout layout_edt_registrar, layout_btn_registrar, layout_btn_actualizar;
    private Button btn_eliminar_puesto_control, btn_actualizar_puesto_control, btn_finalizar_puesto_control_2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public RegisterPuestoControlFragment() {
        // Required empty public constructor
    }

    public static RegisterPuestoControlFragment newInstance(String param1, String param2) {
        RegisterPuestoControlFragment fragment = new RegisterPuestoControlFragment();
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
        View view =inflater.inflate(R.layout.fragment_register_puesto_control, container, false);
        layout_edt_registrar = view.findViewById(R.id.layout_edt_registrar);
        layout_btn_registrar = view.findViewById(R.id.layout_btn_registrar);
        layout_btn_actualizar = view.findViewById(R.id.layout_btn_actualizar);
        btn_actualizar_puesto_control = view.findViewById(R.id.btn_actualizar_puesto_control);
        btn_eliminar_puesto_control = view.findViewById(R.id.btn_eliminar_zona_puesto_control);
        btn_finalizar_puesto_control_2 = view.findViewById(R.id.btn_finalizar_puesto_control_2);
        edt_register_titulo_puesto_control = view.findViewById(R.id.edt_register_titulo_puesto_control);
        edt_register_latitud_puesto_control = view.findViewById(R.id.edt_register_latitud_puesto_control);
        edt_register_longitud_puesto_control = view.findViewById(R.id.edt_register_longitud_puesto_control);
        edt_register_referencia_puesto_control = view.findViewById(R.id.edt_register_Referencia_puesto_control);
        btn_registrar_puesto_control = view.findViewById(R.id.btn_registrar_puesto_control);
        btn_finalizar_puesto_control = view.findViewById(R.id.btn_finalizar_puesto_control);
        layout_edt_registrar.setVisibility(View.GONE);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_puesto_control);
        mapFragment.getMapAsync(this);
        request = Volley.newRequestQueue(getContext());
        return view;
    }
    String latitud, longitud, titulo;
    private int Id_Zona;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DBHelper(getContext());

        ArrayList<Zona> zonas = dbHelper.get_all_Zonas();
        ArrayList<String> tituloZonas = new ArrayList<>();
        if (zonas != null) {
            for (Zona zona : zonas){
                tituloZonas.add(zona.getTitulo());
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tituloZonas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_zona = view.findViewById(R.id.sp_zona);
        sp_zona.setAdapter(adapter);
        sp_zona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Zona zonaselecionada = zonas.get(position);
                Id_Zona = zonaselecionada.getIdZona();
                latitud = zonaselecionada.getLatitud();
                longitud = zonaselecionada.getLongitud();
                titulo = zonaselecionada.getTitulo();
                if (mMap != null) {
                    updateMarker();
                    mMap.clear();
                    Listar_Marcadores(Id_Zona);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_registrar_puesto_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id_zon = Id_Zona;
                String title = edt_register_titulo_puesto_control.getText().toString();
                String referencia = edt_register_referencia_puesto_control.getText().toString();
                String slatitud = edt_register_latitud_puesto_control.getText().toString();
                String slongitud = edt_register_longitud_puesto_control.getText().toString();
                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(slatitud) || TextUtils.isEmpty(slongitud)) {
                    Toast.makeText(getContext(), "Por favor complete los campos", Toast.LENGTH_SHORT).show();
                } else {
                    PuestoControl puestoControl = new PuestoControl(id_zon, referencia, slatitud, slongitud, title);
                    dbHelper.Insertar_Puesto_Control(puestoControl);
                    cargaWebService(id_zon, referencia, slatitud, slongitud, title);
                    Toast.makeText(getContext(), "Registrado Correctamente", Toast.LENGTH_SHORT).show();
                    edt_register_titulo_puesto_control.setText("");
                    edt_register_latitud_puesto_control.setText("");
                    edt_register_longitud_puesto_control.setText("");
                    edt_register_referencia_puesto_control.setText("");
                }
            }
        });
        btn_finalizar_puesto_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigateUp();
                Toast.makeText(getContext(), "Registros Completados", Toast.LENGTH_SHORT).show();
            }
        });
        modoEdicion = getArguments() != null && getArguments().getBoolean("modo_edicion", false);
        if (modoEdicion) {
            layout_btn_registrar.setVisibility(View.GONE);
            layout_btn_actualizar.setVisibility(View.VISIBLE);
            layout_edt_registrar.setVisibility(View.VISIBLE);
            sp_zona.setEnabled(false);
            String AIdZona = getArguments().getString("IdZona");
            Zona zona = dbHelper.get_Zona_Puesto(AIdZona);
            String nombre = zona.getTitulo();
            int spinnerPosition = adapter.getPosition(nombre);
            sp_zona.setSelection(spinnerPosition);
            sp_zona.setPadding(16,8,16,8);
            btn_eliminar_puesto_control.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (idPuestoControlSeleccionado != 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                        builder.setTitle("Confirmar Eliminación")
                                .setMessage("¿Estás seguro de eliminar este Puesto?")
                                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        PuestoControl puestoControl = dbHelper.get_Puesto_Control(idPuestoControlSeleccionado);
                                        dbHelper.Eliminar_Puesto_Control(puestoControl);
                                        EliminarWebService(puestoControl.getIdPuestoControl());
                                        ArrayList<PuestoControl> comprobacion = dbHelper.get_all_Puesto_Controls_Zona(Integer.parseInt(AIdZona));

                                        // Navegar hacia arriba si la lista está vacía
                                        if (comprobacion.isEmpty()) {
                                            if (isAdded()) {
                                                try {
                                                    NavController navController = Navigation.findNavController(v);
                                                    navController.navigateUp();
                                                } catch (Exception e){

                                                }
                                            }

                                            Toast.makeText(requireContext(), "Puestos de la Zona eliminados correctamente", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(requireContext(), "Puesto eliminado correctamente", Toast.LENGTH_SHORT).show();
                                            idPuestoControlSeleccionado = 0;
                                            mMap.clear();
                                            Listar_Marcadores(Integer.parseInt(AIdZona));
                                            edt_register_titulo_puesto_control.setText("");
                                            edt_register_latitud_puesto_control.setText("");
                                            edt_register_longitud_puesto_control.setText("");
                                            edt_register_referencia_puesto_control.setText("");
                                        }
                                    }
                                })
                                .setNegativeButton("Cancelar", null)
                                .create().show();
                    } else {
                        Toast.makeText(getContext(), "Seleccione un Marcador", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            btn_actualizar_puesto_control.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (idPuestoControlSeleccionado != 0) {
                        String Atitle = edt_register_titulo_puesto_control.getText().toString();
                        String Areferencia = edt_register_referencia_puesto_control.getText().toString();
                        String Aslatitud = edt_register_latitud_puesto_control.getText().toString();
                        String Aslongitud = edt_register_longitud_puesto_control.getText().toString();
                        PuestoControl puestoControl = dbHelper.get_Puesto_Control(idPuestoControlSeleccionado);
                        int AIdZona = puestoControl.getIdZona();
                        if (puestoControl != null) {
                            puestoControl.setIdZona(AIdZona);
                            puestoControl.setReferencia(Areferencia);
                            puestoControl.setLatitud(Aslatitud);
                            puestoControl.setLongitud(Aslongitud);
                            puestoControl.setTitulo(Atitle);
                            dbHelper.Actualizar_Puesto_Control(puestoControl);
                            ActualizarWebService(AIdZona, Areferencia, Aslatitud, Aslongitud, Atitle, puestoControl.getIdPuestoControl());
                            puestoControl = null;
                            edt_register_titulo_puesto_control.setText("");
                            edt_register_latitud_puesto_control.setText("");
                            edt_register_longitud_puesto_control.setText("");
                            edt_register_referencia_puesto_control.setText("");
                            idPuestoControlSeleccionado = 0;
                            Listar_Marcadores(AIdZona); // tener cuidado
                            Toast.makeText(getContext(), "Puesto Actualizado", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Puesto no Actualizado", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Seleccione un Marcador", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            btn_eliminar_puesto_control.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setTitle("Confirmar Eliminación")
                            .setMessage("¿Estás seguro de eliminar todos los Puestos de la Zona?")
                            .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dbHelper.Eliminar_Puesto_Control_Zona(Integer.parseInt(AIdZona));
                                    Eliminar_ZonaWebService(Integer.parseInt(AIdZona));
                                    if (isAdded()) {
                                        try {
                                            NavController navController = Navigation.findNavController(v);
                                            navController.navigateUp();
                                        } catch (Exception e){

                                        }
                                    }
                                    Toast.makeText(requireContext(), "Puestos de la Zona eliminados correctamente", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Cancelar", null)
                            .create().show();
                    return false;
                }
            });
            btn_finalizar_puesto_control_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavController navController = Navigation.findNavController(v);
                    navController.navigateUp();
                    Toast.makeText(getContext(), "Cambios Completados", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        dbHelper = new DBHelper(getContext());
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        //mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(16);
        mMap.setMaxZoomPreference(17);
        Listar_Marcadores(Id_Zona);
        if (latitud != null && longitud != null) {
            updateMarker();
        }

        if (modoEdicion) {
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    String mlatitud = String.valueOf(marker.getPosition().latitude);
                    String mlongitud = String.valueOf(marker.getPosition().longitude);
                    PuestoControl puestoControl = dbHelper.get_Puesto_Control_Marker(mlatitud,mlongitud);
                    if (puestoControl != null) {
                        edt_register_titulo_puesto_control.setText(puestoControl.getTitulo());
                        edt_register_referencia_puesto_control.setText(String.valueOf(puestoControl.getReferencia()));
                        edt_register_latitud_puesto_control.setText(puestoControl.getLatitud());
                        edt_register_longitud_puesto_control.setText(puestoControl.getLongitud());
                        idPuestoControlSeleccionado = puestoControl.getIdPuestoControl(); // Guarda el IdPuestoControl
                    }
                    return false;
                }
            });
        } else {
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(@NonNull LatLng latLng) {
                    edt_register_latitud_puesto_control.setText(String.valueOf(latLng.latitude));
                    edt_register_longitud_puesto_control.setText(String.valueOf(latLng.longitude));
                    mMap.clear();
                    Listar_Marcadores(Id_Zona);
                    LatLng puestoCon = new LatLng(latLng.latitude,latLng.longitude);
                    mMap.addMarker(new MarkerOptions().position(puestoCon));
                }
            });
        }

    }

    private void updateMarker() {
        if (latitud != null && longitud != null && titulo != null) {
            double zlatitud = Double.parseDouble(latitud.trim());
            double zlongitud = Double.parseDouble(longitud.trim());
            LatLng zona = new LatLng(zlatitud, zlongitud);
            if (currentMarker != null) {
                currentMarker.remove();
            }
            String formatoLatitud = String.format("%.3f",zlatitud);
            String formatoLongitud = String.format("%.3f",zlongitud);
            double limlatitud = Double.parseDouble(formatoLatitud) +0.004;
            double limlongitud = Double.parseDouble(formatoLongitud) +0.004;
            LatLngBounds ADELAIDE = new LatLngBounds(
                    new LatLng(limlatitud - 0.005, limlongitud - 0.005), // Sudoeste
                    new LatLng(limlatitud - 0.002, limlongitud - 0.002)  // Noreste
            );
            mMap.setLatLngBoundsForCameraTarget(ADELAIDE);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(zona));

        }
    }

    public void Listar_Marcadores(int idZona){
        dbHelper = new DBHelper(getContext());
        ArrayList<PuestoControl> puestoControls = dbHelper.get_all_Puesto_Controls_Zona(idZona);
        for (PuestoControl puestoControl : puestoControls) {
            LatLng latLng = new LatLng(Double.parseDouble(puestoControl.getLatitud()), Double.parseDouble(puestoControl.getLongitud()));
            mMap.addMarker(new MarkerOptions().position(latLng).title(puestoControl.getTitulo()));
        }
    }

    private void cargaWebService(int idzona, String refeencia, String latitud, String longitud, String titulo) {
        progreso = new ProgressDialog(requireActivity());
        progreso.setMessage("Cargando...");
        progreso.show();
        // Puedes añadir más IPs según sea necesario
        String selectedIp = "";

        ArrayList<Usuario> usuarios = dbHelper.get_all_Usuarios();
        for (Usuario usuario : usuarios) {
            selectedIp = IPUtilizada.getInstance().getSelectedIP(usuario.getUsername());
            if (selectedIp != null) {
                break;
            }
        }

        String url="http://" + selectedIp + "/db_grupo_05_tarea_16_ejercicio_01/PuestoControlRegistro.php?" +
                "idzona=" + idzona +
                "&referencia=" + refeencia +
                "&latitud=" + latitud +
                "&longitud=" + longitud +
                "&titulo=" + titulo;
        url = url.replace(" ","%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (isAdded()) {  // Verifica si el fragmento está adjunto antes de interactuar con la UI

                    Toast.makeText(requireActivity(), "Puesto registrado correctamente", Toast.LENGTH_SHORT).show();
                    // preguntarle a chagua del runable
                }
                progreso.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (isAdded()) {  // Verifica si el fragmento está adjunto antes de interactuar con la UI

                    Toast.makeText(requireActivity(), "No se puede conectar: " + error.toString(), Toast.LENGTH_LONG).show();
                }
                progreso.hide();
            }
        });

        request.add(jsonObjectRequest);
    }

    private void ActualizarWebService(int AId_Zona, String areferencia, String alatitud, String alongitud, String atitulo, int AId_PuestoControl) {
        progreso = new ProgressDialog(requireActivity());
        progreso.setMessage("Actualizando...");
        progreso.show();

        // Puedes añadir más IPs según sea necesario
        String selectedIp = "";

        ArrayList<Usuario> usuarios = dbHelper.get_all_Usuarios();
        for (Usuario usuario : usuarios) {
            selectedIp = IPUtilizada.getInstance().getSelectedIP(usuario.getUsername());
            if (selectedIp != null) {
                break;
            }
        }

        String urlWS = "http://" + selectedIp + "/db_grupo_05_tarea_16_ejercicio_01/PuestoControlActualizar.php";

        stringRequest = new StringRequest(Request.Method.POST, urlWS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progreso.hide();
                if (isAdded()) {
                    if (response.trim().equalsIgnoreCase("actualiza")) {
                        Toast.makeText(requireActivity(), "Puesto actualizado correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireActivity(), "Puesto no se pudo actualizar", Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ", "" + response);
                    }
                    progreso.hide();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (isAdded()) {
                    Toast.makeText(requireActivity(), "No se ha podido conectar", Toast.LENGTH_SHORT).show();
                    progreso.hide();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                int idpuescontrol = AId_PuestoControl;
                int idzona = AId_Zona;
                String referencia = areferencia;
                String latitud = alatitud;
                String longitud = alongitud;
                String titulo = atitulo;


                Map<String,String> parametros = new HashMap<>();
                parametros.put("idzona",String.valueOf(idzona));
                parametros.put("referencia", referencia);
                parametros.put("latitud",latitud);
                parametros.put("longitud",longitud);
                parametros.put("titulo",titulo);
                parametros.put("idpuescontrol",String.valueOf(idpuescontrol));

                Log.d("Params", "idzona: " + idzona + ", referencia: " + referencia + ", " +
                        "latitud: " + latitud+ ", longitud: " + longitud+ ", " +
                        "titulo: " + titulo + "idpuescontrol: " + idpuescontrol);
                return parametros;
            }
        };
        request.add(stringRequest);
    }

    private void EliminarWebService(int idPuestoControl){
        progreso = new ProgressDialog(requireActivity());
        progreso.setMessage("Eliminando...");
        progreso.show();

        // Puedes añadir más IPs según sea necesario
        String selectedIp = "";

        ArrayList<Usuario> usuarios = dbHelper.get_all_Usuarios();
        for (Usuario usuario : usuarios) {
            selectedIp = IPUtilizada.getInstance().getSelectedIP(usuario.getUsername());
            if (selectedIp != null) {
                break;
            }
        }

        String urlWS = "http://" + selectedIp + "/db_grupo_05_tarea_16_ejercicio_01/PuestoControlEliminar.php?" +
                "idpuescontrol="+idPuestoControl;

        stringRequest = new StringRequest(Request.Method.GET, urlWS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progreso.hide();
                if (isAdded()) {
                    try {
                        if (response.trim().equalsIgnoreCase("elimina")) {
                            Toast.makeText(requireActivity(), "Puesto eliminado correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            if (response.trim().equalsIgnoreCase("noExiste")) {
                                Toast.makeText(requireActivity(), "No se encuentra el Puesto", Toast.LENGTH_SHORT).show();
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
                        progreso.hide();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        });
        request.add(stringRequest);
    }

    private void Eliminar_ZonaWebService(int idzona){
        progreso = new ProgressDialog(requireActivity());
        progreso.setMessage("Eliminando...");
        progreso.show();

        // Puedes añadir más IPs según sea necesario
        String selectedIp = "";

        ArrayList<Usuario> usuarios = dbHelper.get_all_Usuarios();
        for (Usuario usuario : usuarios) {
            selectedIp = IPUtilizada.getInstance().getSelectedIP(usuario.getUsername());
            if (selectedIp != null) {
                break;
            }
        }

        String urlWS = "http://" + selectedIp + "/db_grupo_05_tarea_16_ejercicio_01/PuestoControlEliminarZona.php?" +
                "idzona="+idzona;

        stringRequest = new StringRequest(Request.Method.GET, urlWS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progreso.hide();
                if (isAdded()) {
                    try {
                        if (response.trim().equalsIgnoreCase("elimina")) {
                            Toast.makeText(requireActivity(), "Puesto eliminado correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            if (response.trim().equalsIgnoreCase("no Existe")) {
                                Toast.makeText(requireActivity(), "No se encuentra el Puesto", Toast.LENGTH_SHORT).show();
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
                        progreso.hide();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        });
        request.add(stringRequest);
    }
}