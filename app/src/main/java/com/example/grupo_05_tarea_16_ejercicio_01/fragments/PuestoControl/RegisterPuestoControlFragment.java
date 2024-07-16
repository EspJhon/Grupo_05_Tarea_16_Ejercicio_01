package com.example.grupo_05_tarea_16_ejercicio_01.fragments.PuestoControl;

import android.app.AlertDialog;
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

import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Infraccion;
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
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterPuestoControlFragment extends Fragment implements OnMapReadyCallback{

    private EditText edt_register_titulo_puesto_control, edt_register_latitud_puesto_control,
            edt_register_longitud_puesto_control, edt_register_referencia_puesto_control;
    private Button btn_registrar_puesto_control, btn_finalizar_puesto_control;
    DBHelper dbHelper;
    Spinner sp_zona;
    GoogleMap mMap;
    Marker currentMarker;
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
        return view;
    }
    String latitud, longitud, titulo;
    private int IdZona;
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
                IdZona = zonaselecionada.getIdZona();
                latitud = zonaselecionada.getLatitud();
                longitud = zonaselecionada.getLongitud();
                titulo = zonaselecionada.getTitulo();
                if (mMap != null) {
                    updateMarker();
                    mMap.clear();
                    Listar_Marcadores(IdZona);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_registrar_puesto_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id_zon = IdZona;
                String title = edt_register_titulo_puesto_control.getText().toString();
                String referencia = edt_register_referencia_puesto_control.getText().toString();
                String slatitud = edt_register_latitud_puesto_control.getText().toString();
                String slongitud = edt_register_longitud_puesto_control.getText().toString();
                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(slatitud) || TextUtils.isEmpty(slongitud)) {
                    Toast.makeText(getContext(), "Por favor complete los campos", Toast.LENGTH_SHORT).show();
                } else {
                    PuestoControl puestoControl = new PuestoControl(id_zon, referencia, slatitud, slongitud, title);
                    dbHelper.Insertar_Puesto_Control(puestoControl);
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
            String IdZona = getArguments().getString("IdZona");
            Zona zona = dbHelper.get_Zona_Puesto(IdZona);
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
                                        ArrayList<PuestoControl> comprobacion = dbHelper.get_all_Puesto_Controls_Zona(Integer.parseInt(IdZona));

                                        // Navegar hacia arriba si la lista está vacía
                                        if (comprobacion.isEmpty()) {
                                            NavController navController = Navigation.findNavController(v);
                                            navController.navigateUp();
                                            Toast.makeText(requireContext(), "Puestos de la Zona eliminados correctamente", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(requireContext(), "Puesto eliminado correctamente", Toast.LENGTH_SHORT).show();
                                            idPuestoControlSeleccionado = 0;
                                            mMap.clear();
                                            Listar_Marcadores(Integer.parseInt(IdZona));
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
                            puestoControl = null;
                            edt_register_titulo_puesto_control.setText("");
                            edt_register_latitud_puesto_control.setText("");
                            edt_register_longitud_puesto_control.setText("");
                            edt_register_referencia_puesto_control.setText("");
                            idPuestoControlSeleccionado = 0;
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
                                    PuestoControl puestoControl = dbHelper.get_Puesto_Control(Integer.parseInt(IdZona));
                                    dbHelper.Eliminar_Puesto_Control_Zona(puestoControl.getIdZona());
                                    NavController navController = Navigation.findNavController(v);
                                    navController.navigateUp();
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
        Listar_Marcadores(IdZona);
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
                    Listar_Marcadores(IdZona);
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
}