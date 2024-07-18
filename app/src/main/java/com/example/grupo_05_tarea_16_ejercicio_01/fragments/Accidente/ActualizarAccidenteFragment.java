package com.example.grupo_05_tarea_16_ejercicio_01.fragments.Accidente;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
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
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.MapMoveFragment;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Accidente;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Agente;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Usuario;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Vehiculo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ActualizarAccidenteFragment extends Fragment implements OnMapReadyCallback {

    private EditText et_horaA, et_fechaA, et_descripcionA_accidente, et_tituloA_accidente,
            et_nombreLugarA;
    private String latitud = null, longitud = null;
    private Spinner sp_placaA_accidente, sp_agenteA_accidente;
    private ImageView iv_imagenAccidenteA;
    private DBHelper dbHelper;
    private String URL;
    private GoogleMap mMap;
    private Accidente accidente;
    private Marker marker;
    private ScrollView scrollView;
    ArrayAdapter<String> adapter_agente;
    ArrayAdapter<String> adapter_placa;


    private ProgressDialog progressDialog;
    RequestQueue request;
    StringRequest stringRequest;

    public ActualizarAccidenteFragment() {
        // Required empty public constructor
    }

    public static ActualizarAccidenteFragment newInstance(String param1, String param2) {
        ActualizarAccidenteFragment fragment = new ActualizarAccidenteFragment();
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

        View view = inflater.inflate(R.layout.fragment_actualizar_accidente, container, false);

        scrollView = view.findViewById(R.id.sv_accidenteA);

        sp_placaA_accidente = view.findViewById(R.id.sp_placaA_accidente);
        sp_agenteA_accidente = view.findViewById(R.id.sp_agenteA_accidente);
        et_horaA = view.findViewById(R.id.et_horaA);
        et_fechaA = view.findViewById(R.id.et_fechaA);
        et_tituloA_accidente = view.findViewById(R.id.et_tituloA_accidente);
        et_descripcionA_accidente = view.findViewById(R.id.et_descripcionA_accidente);
        et_nombreLugarA = view.findViewById(R.id.et_nombreLugarA);
        iv_imagenAccidenteA = view.findViewById(R.id.iv_imagenAccidenteA);

        et_fechaA.setOnClickListener(v -> showDatePickerDialog());
        et_horaA.setOnClickListener(v -> showTimePickerDialog());

        request = Volley.newRequestQueue(getContext());

        MapMoveFragment mapMoveFragment = (MapMoveFragment) getChildFragmentManager().findFragmentById(R.id.fr_ubicacionAccidenteA);
        if (mapMoveFragment != null) {
            mapMoveFragment.getMapAsync(this);
            mapMoveFragment.setListener(new MapMoveFragment.OnTouchListener() {
                @Override
                public void onTouch() {
                    scrollView.requestDisallowInterceptTouchEvent(true);
                }
            });
        }

        return view;
    }

    private int AIdAgente, AIdVehiculo;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DBHelper(getContext());
        ArrayList<Agente> agentes = dbHelper.getAllAgentes();
        ArrayList<String> tituloAgentes = new ArrayList<>();
        if (agentes != null) {
            for (Agente agente : agentes) {
                String item = agente.getCedulaa() + " - " + agente.getNombre(); // Concatenar ID y nombre
                tituloAgentes.add(item);
            }
        }
        adapter_agente = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tituloAgentes);
        adapter_agente.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_agenteA_accidente.setAdapter(adapter_agente);

        ArrayList<Vehiculo> vehiculos = dbHelper.get_all_Vehiculos();
        ArrayList<String> tituloVehiculos = new ArrayList<>();
        if (vehiculos != null) {
            for (Vehiculo vehiculo : vehiculos) {
                tituloVehiculos.add(String.valueOf(vehiculo.getNumplaca()));
            }
        }
        adapter_placa = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tituloVehiculos);
        adapter_placa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_placaA_accidente.setAdapter(adapter_placa);
        if (getArguments() != null && getArguments().getSerializable("id") != null) {
            int id = getArguments().getInt("id");
            accidente = dbHelper.get_Accidente(id);
            CargarDatosRegistrados(accidente);
        }
        sp_agenteA_accidente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Agente agente = agentes.get(position);
                AIdAgente = agente.getIdagente();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_placaA_accidente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Vehiculo vehiculo = vehiculos.get(position);
                AIdVehiculo = vehiculo.getIdVehiculo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        view.findViewById(R.id.btn_tomarFotoA).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivarCam();
            }
        });
        view.findViewById(R.id.btn_actualizarAccidente).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActualizarAccidente();
            }
        });
        view.findViewById(R.id.btn_subirFotoA).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubirFoto();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111111 && resultCode == requireActivity().RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            URL = GuardarURL(imageBitmap);
            iv_imagenAccidenteA.setImageBitmap(imageBitmap);
        } else if (requestCode == 222222 && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                URL = GuardarURL(selectedImage);
                iv_imagenAccidenteA.setImageBitmap(selectedImage);
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if (accidente != null) {
            double latitudActualizada = Double.parseDouble(accidente.getLatitud());
            double longitudActualizada = Double.parseDouble(accidente.getLongitud());
            LatLng lugarAccidente = new LatLng(latitudActualizada, longitudActualizada);
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(lugarAccidente).title("Ubicación del accidente"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lugarAccidente, 15));
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                latitud = String.valueOf(latLng.latitude);
                longitud = String.valueOf(latLng.longitude);
                if (marker != null) {
                    marker.setPosition(latLng);
                } else {
                    marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Nueva ubicación del accidente"));
                }
            }
        });
    }

    private void ActualizarAccidente() {

        if (et_horaA.getText().toString().trim().isEmpty() || et_fechaA.getText().toString().trim().isEmpty() ||
                et_descripcionA_accidente.getText().toString().trim().isEmpty() ||
                et_tituloA_accidente.getText().toString().trim().isEmpty() ||
                et_nombreLugarA.getText().toString().trim().isEmpty()) {

            Toast.makeText(getActivity(), "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int placaA = AIdVehiculo;
        int agenteA = AIdAgente;
        String horaA = et_horaA.getText().toString().trim();
        String fechaA = et_fechaA.getText().toString().trim();
        String tituloA = et_tituloA_accidente.getText().toString().trim();
        String descripcionA = et_descripcionA_accidente.getText().toString().trim();
        String lugarA = et_nombreLugarA.getText().toString().trim();

        accidente.setIdVehiculo(placaA);
        accidente.setIdagente(agenteA);
        accidente.setHora(horaA);
        accidente.setFecha(fechaA);
        accidente.setTitulo(tituloA);
        accidente.setDescripcion(descripcionA);
        accidente.setURLimagen(URL);
        accidente.setNombreLugar(lugarA);
        if (latitud != null || longitud != null) {
            accidente.setLatitud(latitud);
            accidente.setLongitud(longitud);
        }

        dbHelper.Actualizar_Accidente(accidente);

        ActualizarWebService(placaA,agenteA,horaA,fechaA,tituloA,descripcionA,URL,lugarA,latitud,longitud,accidente.getIdaccidente());
    }

    private void ActivarCam() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(intent, 111111);
        }
    }

    private void SubirFoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(intent, 222222);
        }
    }

    private String GuardarURL(Bitmap bitmap) {
        ContextWrapper wrapper = new ContextWrapper(getActivity());

        File directory = wrapper.getDir("images", Context.MODE_PRIVATE);
        String fileName = "imagen_" + System.currentTimeMillis() + ".jpg";
        File file = new File(directory, fileName);
        try (OutputStream stream = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
        } catch (Exception e) {
            Log.e("IMPORTANTE", "Error al guardar la imagen", e);
        }

        return file.getAbsolutePath();
    }

    private void CargarDatosRegistrados(Accidente accidente) {
        Agente agente = dbHelper.get_Agente(accidente.getIdagente());
        Vehiculo vehiculo = dbHelper.get_Vehiculo(accidente.getIdVehiculo());
        String agenteItem = agente.getCedulaa() + " - " + agente.getNombre();
        int spinnerPosition_agente = adapter_agente.getPosition(agenteItem);
        sp_agenteA_accidente.setSelection(spinnerPosition_agente);
        sp_agenteA_accidente.setPadding(16, 8, 16, 8);

        int spinnerPosition_vehiculo = adapter_placa.getPosition(String.valueOf(vehiculo.getNumplaca()));
        sp_placaA_accidente.setSelection(spinnerPosition_vehiculo);
        sp_placaA_accidente.setPadding(16, 8, 16, 8);
        et_horaA.setText(accidente.getHora());
        et_fechaA.setText(accidente.getFecha());
        et_tituloA_accidente.setText(accidente.getTitulo());
        et_descripcionA_accidente.setText(accidente.getDescripcion());
        et_nombreLugarA.setText(accidente.getNombreLugar());
        latitud = accidente.getLatitud();
        longitud = accidente.getLongitud();
        URL = accidente.getURLimagen();

        Bitmap bitmap = BitmapFactory.decodeFile(URL);
        iv_imagenAccidenteA.setImageBitmap(bitmap);

        if (mMap != null) {
            double latitudObtenida = Double.parseDouble(latitud);
            double longitudObtenida = Double.parseDouble(longitud);
            LatLng lugarAccidente = new LatLng(latitudObtenida, longitudObtenida);
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(lugarAccidente).title("Ubicación del accidente"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lugarAccidente, 15));
        }
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, year1, month1, dayOfMonth) -> {
            String selectedDate = year1 + "-" + String.format("%02d", (month1 + 1)) + "-" + String.format("%02d", dayOfMonth);
            et_fechaA.setText(selectedDate);
        }, year, month, day);

        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), (view, hourOfDay, minuteOfDay) -> {
            String selectedTime = String.format("%02d:%02d", hourOfDay, minuteOfDay);
            et_horaA.setText(selectedTime);
        }, hour, minute, true);

        timePickerDialog.show();
    }

    private void ActualizarWebService(int idvehiculoA, int idagenteA, String horaA, String fechaA,String tituloA, String descripcionA,
                                      String urlA, String lugarA, String latitudA, String longitudA, int idaccidenteA) {
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Actualizando...");
        progressDialog.show();

        String selectedIp = "";

        ArrayList<Usuario> usuarios = dbHelper.get_all_Usuarios();
        for (Usuario usuario : usuarios) {
            selectedIp = IPUtilizada.getInstance().getSelectedIP(usuario.getUsername());
            if (selectedIp != null) {
                break;
            }
        }

        String urlWS = "http://" + selectedIp + "/db_grupo_05_tarea_16_ejercicio_01/AccidenteActualizar.php";

        stringRequest = new StringRequest(Request.Method.POST, urlWS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();
                if (response.trim().equalsIgnoreCase("actualiza")) {
                    Toast.makeText(requireActivity(), "Accidente actualizado correctamente", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(requireActivity(), "Accidente no se pudo actualizar", Toast.LENGTH_SHORT).show();
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
                int idvehiculo = idvehiculoA;
                int idagente = idagenteA;
                String hora = horaA;
                String fecha = fechaA;
                String titulo = tituloA;
                String descripcion = descripcionA;
                String url = urlA;
                String lugar = lugarA;
                String latitud = latitudA;
                String longitud = longitudA;
                int idaccidente = idaccidenteA;

                Map<String,String> parametros = new HashMap<>();
                parametros.put("idvehiculo",String.valueOf(idvehiculo));
                parametros.put("idagente", String.valueOf(idagente));
                parametros.put("hora", hora);
                parametros.put("fecha",fecha);
                parametros.put("titulo",titulo);
                parametros.put("descripcion",descripcion);
                parametros.put("url", url);
                parametros.put("lugar", lugar);
                parametros.put("latitud", latitud);
                parametros.put("longitud", longitud);
                parametros.put("idaccidente", String.valueOf(idaccidente));

                return parametros;
            }
        };
        request.add(stringRequest);
    }

}
