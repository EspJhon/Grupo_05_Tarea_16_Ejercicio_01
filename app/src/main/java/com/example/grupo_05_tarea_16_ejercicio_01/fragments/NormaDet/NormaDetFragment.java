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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.NormaDetalleAdapter;
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.ZonaAdapter;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Agente;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Infraccion;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.NormasDet;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Usuario;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Vehiculo;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Zona;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NormaDetFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    DBHelper dbHelper;
    private Button btn_anadir_norma;
    private RecyclerView lvl_lista_norma;
    private NormaDetalleAdapter adapter;
    ProgressDialog progreso;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    private ProgressDialog progressDialog;
    StringRequest stringRequest;
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
                Dialog_Norma(1, null);
            }
        });
    }

    public void Listar_Normas() {
        ArrayList<NormasDet> normasDets = dbHelper.get_all_Normas_Detalle();
        adapter = new NormaDetalleAdapter(getActivity(), normasDets, new NormaDetalleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NormasDet normasDet) {
                Dialog_Norma(2, normasDet);
            }
        });
        lvl_lista_norma.setAdapter(adapter);
    }

    public void Dialog_Norma(int tipo, NormasDet normasDet) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_norma, null);

        final EditText n_norma = dialogView.findViewById(R.id.edt_register_numero_norma);
        final EditText descripcion = dialogView.findViewById(R.id.edt_register_descripcion_norma);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        if (tipo == 1 && normasDet == null) {
            builder.setTitle("Agregar Norma");
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
        } else if (tipo == 2 && normasDet != null) {
            builder.setTitle("Actualizar Norma");
            builder.setView(dialogView);
            n_norma.setText(String.valueOf(normasDet.getNumnorma()));
            descripcion.setText(String.valueOf(normasDet.getDescripcion()));
            int IdNorma = normasDet.getIdnomra();
            builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String cnnorma = n_norma.getText().toString();
                    String cdescripcion = descripcion.getText().toString();
                    NormasDet normasDet = dbHelper.get_Norma_Detalle(IdNorma);
                    if (normasDet != null) {
                        normasDet.setNumnorma(cnnorma);
                        normasDet.setDescripcion(cdescripcion);
                        dbHelper.Actualizar_Norma_Detalle(normasDet);

                        ActualizarWebService(cnnorma,cdescripcion,IdNorma);
                        normasDet = null;
                        Toast.makeText(getContext(), "Norma Actualizada", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Norma no Actualizada", Toast.LENGTH_SHORT).show();
                    }
                    Listar_Normas();
                }
            });
            builder.setNegativeButton("Eliminar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setTitle("Confirmar Eliminación")
                            .setMessage("¿Estás seguro de eliminar este Norma?")
                            .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    NormasDet normasDet = dbHelper.get_Norma_Detalle(IdNorma);
                                    ArrayList<Infraccion> comprobar = dbHelper.get_all_Infracciones();
                                    boolean exists = doesIdNormaExist(comprobar, normasDet.getIdnomra());
                                    if (exists) {
                                        Toast.makeText(getContext(), "Existen Registros Dependientes", Toast.LENGTH_SHORT).show();
                                    } else {
                                        dbHelper.Eliminar_Norma_Detalle(normasDet);
                                        EliminarWebService(normasDet.getIdnomra());
                                        Listar_Normas();

                                        Toast.makeText(requireContext(), "Norma eliminado correctamente", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("Cancelar", null)
                            .create().show();
                }
            });
        }

        builder.show();
    }

    public static boolean doesIdNormaExist(ArrayList<Infraccion> infracciones, int idInfraccionToCheck) {
        for (Infraccion infraccion : infracciones) {
            if (infraccion.getIdnomra() == idInfraccionToCheck) {
                return true;
            }
        }
        return false;
    }
    private void cargaWebService(String numnorma, String descripcion) {
        progreso = new ProgressDialog(requireActivity());
        progreso.setMessage("Cargando...");
        progreso.show();

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
                break; // Exit loop after finding a match
            }
        }

        String url = "http://" + selectedIp + "/db_grupo_05_tarea_16_ejercicio_01/NormaRegistro.php?numnorma=" + numnorma + "&descripcion=" + descripcion;
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

    private void ActualizarWebService(String numnorma, String descripcion, int idnorma) {
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Actualizando...");
        progressDialog.show();

        List<String> ips = Arrays.asList("192.168.100.15", "192.168.10.106", "192.168.1.6");
        String selectedIp = "";
        Map<String, String> userIpMap = new HashMap<>();
        userIpMap.put("jhon", ips.get(0));
        userIpMap.put("chagua", ips.get(1));
        userIpMap.put("matias", ips.get(2));

        ArrayList<Usuario> usuarios = dbHelper.get_all_Usuarios();
        for (Usuario usuario : usuarios) {
            selectedIp = userIpMap.get(usuario.getUsername());
            if (selectedIp != null) {
                break;
            }
        }

        if (selectedIp.isEmpty()) {
            progressDialog.hide();
            Toast.makeText(requireActivity(), "No se pudo determinar la IP", Toast.LENGTH_SHORT).show();
            return;
        }

        String urlWS = "http://" + selectedIp + "/db_grupo_05_tarea_16_ejercicio_01/NormaActualizar.php";
        Log.d("URLWebService", "URL: " + urlWS);

        stringRequest = new StringRequest(Request.Method.POST, urlWS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();
                if (response.equalsIgnoreCase("actualiza")) {
                    Toast.makeText(requireActivity(), "Norma Actualizada con éxito", Toast.LENGTH_SHORT).show();
                } else {
                    // Mostrar el error en un AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                    builder.setTitle("Error")
                            .setMessage(response)
                            .setPositiveButton("OK", null)
                            .show();

                    // Registrar en Logcat para más detalles
                    Log.i("RESPUESTA: ", "" + response);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    String errorResponse = new String(error.networkResponse.data);
                    Log.e("VolleyError", errorResponse);
                } else {
                    Log.e("VolleyError", error.toString());
                }
                Toast.makeText(requireActivity(), "No se ha podido conectar", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<>();
                parametros.put("numnorma", numnorma);
                parametros.put("descripcion", descripcion);
                parametros.put("idnorma", String.valueOf(idnorma));

                Log.d("Params", "numnorma: " + numnorma + ", descripcion: " + descripcion);
                return parametros;
            }
        };
        request.add(stringRequest);
    }

    private void EliminarWebService(int idnorma){
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Eliminando...");
        progressDialog.show();

        List<String> ips = Arrays.asList("192.168.100.15", "192.168.10.106", "192.168.1.6");
        // Puedes añadir más IPs según sea necesario
        String selectedIp = "";
        Map<String, String> userIpMap = new HashMap<>();
        userIpMap.put("jhon", ips.get(0));
        userIpMap.put("chagua", ips.get(1));
        userIpMap.put("matias", ips.get(2));

        ArrayList<Usuario> usuarios = dbHelper.get_all_Usuarios();
        for (Usuario usuario : usuarios) {
            selectedIp = userIpMap.get(usuario.getUsername());
            if (selectedIp != null) {
                break;
            }
        }

        String urlWS = "http://" + selectedIp + "/db_grupo_05_tarea_16_ejercicio_01/NormaEliminar.php?" + "idnorma="+idnorma;

        stringRequest = new StringRequest(Request.Method.GET, urlWS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();

                if (response.trim().equalsIgnoreCase("elimina")) {
                    Toast.makeText(requireActivity(), "Norma eliminada correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    if (response.trim().equalsIgnoreCase("noExiste")) {
                        Toast.makeText(requireActivity(), "No se encuentra la norma", Toast.LENGTH_SHORT).show();
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
                //Toast.makeText(requireActivity(), "No se ha podido conectar: " + errorM, Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        });
        request.add(stringRequest);
    }


}