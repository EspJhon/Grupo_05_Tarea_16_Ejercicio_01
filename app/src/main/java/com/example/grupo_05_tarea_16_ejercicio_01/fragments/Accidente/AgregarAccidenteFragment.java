package com.example.grupo_05_tarea_16_ejercicio_01.fragments.Accidente;

import android.app.Activity;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

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

public class AgregarAccidenteFragment extends Fragment implements OnMapReadyCallback, Response.Listener<JSONObject>, Response.ErrorListener {

    private EditText et_hora, et_fecha, et_descripcion_accidente, et_titulo_accidente,
            et_nombreLugar;
    private String latitud = null, longitud = null;
    private Spinner sp_placa_accidente, sp_agente_accidente;
    private ImageView iv_imagenAccidente;
    private DBHelper dbHelper;
    private String URL;
    private GoogleMap mMap;
    private ScrollView scrollView;
    private ProgressDialog progressDialog;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;


    public AgregarAccidenteFragment() {
        // Required empty public constructor
    }

    public static AgregarAccidenteFragment newInstance(String param1, String param2) {
        AgregarAccidenteFragment fragment = new AgregarAccidenteFragment();
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

        View view = inflater.inflate(R.layout.fragment_agregar_accidente, container, false);

        dbHelper = new DBHelper(getActivity());

        scrollView = view.findViewById(R.id.sv_accidente);

        sp_placa_accidente = view.findViewById(R.id.sp_placa_accidente);
        sp_agente_accidente = view.findViewById(R.id.sp_agente_accidente);
        et_hora = view.findViewById(R.id.et_hora);
        et_fecha = view.findViewById(R.id.et_fecha);
        et_descripcion_accidente = view.findViewById(R.id.et_descripcion_accidente);
        et_titulo_accidente = view.findViewById(R.id.et_titulo_accidente);
        et_nombreLugar = view.findViewById(R.id.et_nombreLugar);
        iv_imagenAccidente = view.findViewById(R.id.iv_imagenAccidente);

        request = Volley.newRequestQueue(getContext());

        et_fecha.setOnClickListener(v -> showDatePickerDialog());
        et_hora.setOnClickListener(v -> showTimePickerDialog());

        MapMoveFragment mapMoveFragment = (MapMoveFragment) getChildFragmentManager().findFragmentById(R.id.fr_ubicacionAccidente);
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

    private int IdAgente, IdVehiculo;

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
        ArrayAdapter<String> adapter_agente = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tituloAgentes);
        adapter_agente.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_agente_accidente.setAdapter(adapter_agente);

        ArrayList<Vehiculo> vehiculos = dbHelper.get_all_Vehiculos();
        ArrayList<String> tituloVehiculos = new ArrayList<>();
        if (vehiculos != null) {
            for (Vehiculo vehiculo : vehiculos) {
                tituloVehiculos.add(String.valueOf(vehiculo.getNumplaca()));
            }
        }
        ArrayAdapter<String> adapter_placa = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tituloVehiculos);
        adapter_placa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_placa_accidente.setAdapter(adapter_placa);

        sp_agente_accidente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Agente agente = agentes.get(position);
                IdAgente = agente.getIdagente();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_placa_accidente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Vehiculo vehiculo = vehiculos.get(position);
                IdVehiculo = vehiculo.getIdVehiculo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        view.findViewById(R.id.btn_tomarFoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivarCam();
            }
        });
        view.findViewById(R.id.btn_agregarAccidente).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarAccidente();
            }
        });
        view.findViewById(R.id.btn_subirFoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubirFoto();
            }
        });
    }

    public void AgregarAccidente() {

        if (et_hora.getText().toString().trim().isEmpty() || et_fecha.getText().toString().trim().isEmpty() ||
                et_descripcion_accidente.getText().toString().trim().isEmpty() ||
                et_titulo_accidente.getText().toString().trim().isEmpty() ||
                et_nombreLugar.getText().toString().trim().isEmpty()) {
            Toast.makeText(getActivity(), "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (URL == null || URL.isEmpty()) {
            Toast.makeText(getActivity(), "Debe subir una imagen", Toast.LENGTH_SHORT).show();
            return;
        }

        if (latitud == null && longitud == null) {
            Toast.makeText(getActivity(), "Debe seleccionar una ubicación", Toast.LENGTH_SHORT).show();
            return;
        }

        int placa = IdVehiculo;
        int agente = IdAgente;
        String hora = et_hora.getText().toString().trim();
        String fecha = et_fecha.getText().toString().trim();
        String titulo = et_titulo_accidente.getText().toString().trim();
        String descripcion = et_descripcion_accidente.getText().toString().trim();

        String lugar = et_nombreLugar.getText().toString().trim();

        Accidente accidente = new Accidente(placa, agente, hora, fecha, titulo, descripcion, URL, lugar, latitud, longitud);
        dbHelper.Insertar_Accidente(accidente);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (isAdded()) { // Check if the fragment is attached
                    requireActivity().getSupportFragmentManager().popBackStack();
                }
            }
        };

        CargarWebService(placa,agente,hora,fecha,titulo,descripcion,URL,lugar,latitud,longitud,runnable);
    }


    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, year1, month1, dayOfMonth) -> {
            String selectedDate = year1 + "-" + String.format("%02d", (month1 + 1)) + "-" + String.format("%02d", dayOfMonth);
            et_fecha.setText(selectedDate);
        }, year, month, day);

        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), (view, hourOfDay, minuteOfDay) -> {
            String selectedTime = String.format("%02d:%02d", hourOfDay, minuteOfDay);
            et_hora.setText(selectedTime);
        }, hour, minute, true);

        timePickerDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99999999 && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap foto = (Bitmap) extras.get("data");

            URL = GuardarURL(foto);

            iv_imagenAccidente.setImageBitmap(foto);
        } else if (requestCode == 88888 && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                Bitmap imgSubida = BitmapFactory.decodeStream(imageStream);
                URL = GuardarURL(imgSubida);
                iv_imagenAccidente.setImageBitmap(imgSubida);
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        }
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

    private void ActivarCam() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(intent, 99999999);
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

    private void SubirFoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(intent, 88888);
        }
    }

    private void CargarWebService(int idvehiculo, int idagente, String hora, String fecha, String titulo, String descripcion,
                                  String urlimagen, String nombreLugar, String latitud, String longitud,Runnable runnable) {

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


        String urlWS = "http://"+ selectedIp + "/db_grupo_05_tarea_16_ejercicio_01/AccidenteRegistro.php?" +
                "idvehiculo=" + idvehiculo +
                "&idagente="  + idagente +
                "&hora=" + hora +
                "&fecha=" + fecha +
                "&titulo=" + titulo +
                "&descripcion=" + descripcion +
                "&url=" + urlimagen +
                "&lugar=" + nombreLugar +
                "&latitud=" + latitud +
                "&longitud=" + longitud;

        urlWS=urlWS.replace(" ","%20");

        //Log.d("URLWebService", "URL: " + urlWS);

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlWS, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.hide();
                Toast.makeText(requireActivity(), "Accidente registrado correctamente", Toast.LENGTH_SHORT).show();
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