package com.example.grupo_05_tarea_16_ejercicio_01.fragments.NormaDet;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.NormaDetalleAdapter;
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.ZonaAdapter;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.NormasDet;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Zona;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.ArrayList;

public class NormaDetFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    DBHelper dbHelper;
    private Button btn_anadir_norma;
    private RecyclerView lvl_lista_norma;
    private NormaDetalleAdapter adapter;
    ProgressDialog progreso;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public NormaDetFragment() {
        // Required empty public constructor
    }

    public static NormaDetFragment newInstance(String param1, String param2) {
        NormaDetFragment fragment = new NormaDetFragment();
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
        View view = inflater.inflate(R.layout.fragment_norma_det, container, false);
        dbHelper = new DBHelper(getActivity());
        request = Volley.newRequestQueue(requireActivity());
        btn_anadir_norma = view.findViewById(R.id.btn_anadir_norma);
        lvl_lista_norma = view.findViewById(R.id.lvl_lista_norma);
        lvl_lista_norma.setLayoutManager(new LinearLayoutManager(getContext()));
        Listar_Normas();
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // No hacer nada para deshabilitar el botón de retroceso
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DBHelper(getContext());
        btn_anadir_norma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View dialogView = inflater.inflate(R.layout.dialog_norma, null);

                final EditText n_norma = dialogView.findViewById(R.id.edt_register_numero_norma);
                final EditText descripcion = dialogView.findViewById(R.id.edt_register_descripcion_norma);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Añadir Norma");
                builder.setView(dialogView);
                // Set up the buttons
                builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String cnnorma = n_norma.getText().toString();
                        String cdescripcion = descripcion.getText().toString();


                        if (cnnorma.isEmpty() || cdescripcion.isEmpty()) {
                            Toast.makeText(getContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        NormasDet normasDet = new NormasDet(cnnorma, cdescripcion);
                        dbHelper.Insertar_Normas_Detalle(normasDet);

                        Log.d("RegistroNorma", "N° de Norma: " + cnnorma + ", Nombre: " + cdescripcion);
                        cargaWebService(cnnorma, cdescripcion);
                        Toast.makeText(getContext(), "Norma registrada", Toast.LENGTH_SHORT).show();
                        Listar_Normas();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }
    public void Listar_Normas() {
        ArrayList<NormasDet> normasDets = dbHelper.get_all_Normas_Detalle();
        adapter = new NormaDetalleAdapter(getActivity(), normasDets, new NormaDetalleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NormasDet normasDet) {
            }
        });
        lvl_lista_norma.setAdapter(adapter);
    }

    //WEB SERVICE
    private void cargaWebService(String numnorma, String descripcion) {
        progreso = new ProgressDialog(requireActivity());
        progreso.setMessage("Cargando...");
        progreso.show();

        String url = "http://192.168.1.6/db_grupo_05_tarea_16_ejercicio_01/NormaRegistro.php?numnorma=" + numnorma + "&descripcion=" + descripcion;
        url = url.replace(" ", "%20");

        Log.d("URLWebService", "URL: " + url);

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.dismiss();
        Toast.makeText(requireActivity(), "No se puede conectar: " + error.toString(), Toast.LENGTH_LONG).show();
        Log.d("ERROR: ", error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        progreso.dismiss();
        Toast.makeText(requireActivity(), "Mensaje: " + response.toString(), Toast.LENGTH_SHORT).show();
    }
}