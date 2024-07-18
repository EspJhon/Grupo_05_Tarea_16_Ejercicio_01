package com.example.grupo_05_tarea_16_ejercicio_01.fragments.Accidente;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.grupo_05_tarea_16_ejercicio_01.MainActivity;
import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.AccidenteAdapter;
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.IPUtilizada;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Accidente;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Acta;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Agente;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Usuario;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Vehiculo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccidenteFragment extends Fragment {

    private ListView lv_accidentes;
    private Button btn_nuevoAccidente;
    private DBHelper dbHelper;

    private ProgressDialog progressDialog;
    RequestQueue request;
    StringRequest stringRequest;

    public AccidenteFragment() {
        // Required empty public constructor
    }

    public static AccidenteFragment newInstance(String param1, String param2) {
        AccidenteFragment fragment = new AccidenteFragment();
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

        View view = inflater.inflate(R.layout.fragment_accidente, container, false);

        lv_accidentes = view.findViewById(R.id.lv_accidentes);
        btn_nuevoAccidente = view.findViewById(R.id.btn_nuevoAccidente);

        request = Volley.newRequestQueue(getContext());
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
        view.findViewById(R.id.btn_nuevoAccidente).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Vehiculo> prueba01 = dbHelper.get_all_Vehiculos();
                ArrayList<Agente> prueba02 = dbHelper.getAllAgentes();
                if (prueba01.isEmpty()) {
                    Toast.makeText(getContext(), "No Existe Vehiculo", Toast.LENGTH_SHORT).show();
                } else if (prueba02.isEmpty()) {
                    Toast.makeText(getContext(), "No Existe Agente", Toast.LENGTH_SHORT).show();
                } else {
                    NavController navController = Navigation.findNavController(v);
                    navController.navigate(R.id.action_accidenteFragment_to_agregarAccidenteFragment);
                }
            }
        });

        lv_accidentes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Accidente accidente = (Accidente) parent.getItemAtPosition(position);
                OpcionesDialog(accidente);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        ListarAccidentes();
    }

    public void ListarAccidentes() {
        ArrayList<Accidente> accidentes = dbHelper.get_all_Accidentes();
        AccidenteAdapter adapter = new AccidenteAdapter(getActivity(), accidentes);
        lv_accidentes.setAdapter(adapter);
    }

    public void OpcionesDialog(Accidente accidente) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Seleccione una opción")
                .setItems(new String[]{"Editar", "Eliminar"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("id", accidente.getIdaccidente());
                                NavController navController = Navigation.findNavController(getView());
                                navController.navigate(R.id.action_accidenteFragment_to_actualizarAccidenteFragment, bundle);
                                break;
                            case 1:
                                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                                builder.setTitle("Confirmar Eliminación")
                                        .setMessage("¿Estás seguro de eliminar este Puesto?")
                                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                ArrayList<Acta> comprobar01 = dbHelper.getAllActas();

                                                boolean exists = doesIdActaExist(comprobar01, accidente.getIdaccidente());
                                                if (exists) {
                                                    Toast.makeText(getContext(), "Existen Registros Dependientes", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    dbHelper.Eliminar_Accidente(accidente);
                                                    EliminarWebService(accidente.getIdaccidente());
                                                    ListarAccidentes();
                                                }
                                            }
                                        })
                                                .

                                        setNegativeButton("Cancelar", null)
                                                .

                                        create().

                                        show();
                                break;
                        }
                    }
                });
        builder.create().

                show();
    }

    private void EliminarWebService(int idaccidenteE) {
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Eliminando...");
        progressDialog.show();

        String selectedIp = "";

        ArrayList<Usuario> usuarios = dbHelper.get_all_Usuarios();
        for (Usuario usuario : usuarios) {
            selectedIp = IPUtilizada.getInstance().getSelectedIP(usuario.getUsername());
            if (selectedIp != null) {
                break;
            }
        }

        String urlWS = "http://" + selectedIp + "/db_grupo_05_tarea_16_ejercicio_01/AccidenteEliminar.php?" +
                "idaccidente=" + idaccidenteE;

        stringRequest = new StringRequest(Request.Method.GET, urlWS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();

                if (response.trim().equalsIgnoreCase("elimina")) {
                    Toast.makeText(requireActivity(), "Accidente eliminado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    if (response.trim().equalsIgnoreCase("noExiste")) {
                        Toast.makeText(requireActivity(), "No se encuentra el accidente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireActivity(), "No se ha eliminado", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorM = error.getMessage();
                if (error.networkResponse != null) {
                    errorM += " Estado: " + error.networkResponse.statusCode;
                }
                Log.e("EliminarWebService", "Error: " + errorM);
                Toast.makeText(requireActivity(), "No se ha podido conectar: " + errorM, Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        });
        request.add(stringRequest);

    }

    public static boolean doesIdActaExist
            (ArrayList<Acta> actas, int idZonaToCheck) {
        for (Acta acta : actas) {
            if (acta.getIdaccidente() == idZonaToCheck) {
                return true;
            }
        }
        return false;
    }

}