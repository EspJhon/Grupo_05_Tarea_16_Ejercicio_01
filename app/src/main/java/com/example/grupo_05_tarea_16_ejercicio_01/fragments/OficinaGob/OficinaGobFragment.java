package com.example.grupo_05_tarea_16_ejercicio_01.fragments.OficinaGob;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.grupo_05_tarea_16_ejercicio_01.R;
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.AccidenteAdapter;
import com.example.grupo_05_tarea_16_ejercicio_01.adapter.OficinaAdapter;
import com.example.grupo_05_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Accidente;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.OficinaGob;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Usuario;
import com.example.grupo_05_tarea_16_ejercicio_01.modelo.Vehiculo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OficinaGobFragment extends Fragment {

    private ListView lv_oficinas;
    private Button btn_nuevaOficina;
    private DBHelper dbHelper;

    private ProgressDialog progressDialog;
    RequestQueue request;
    StringRequest stringRequest;

    public OficinaGobFragment() {
        // Required empty public constructor
    }

    public static OficinaGobFragment newInstance(String param1, String param2) {
        OficinaGobFragment fragment = new OficinaGobFragment();
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
        View view = inflater.inflate(R.layout.fragment_oficina_gob, container, false);

        dbHelper=new DBHelper(getActivity());

        lv_oficinas=view.findViewById(R.id.lv_oficinas);
        btn_nuevaOficina=view.findViewById(R.id.btn_nuevaOficina);

        request = Volley.newRequestQueue(getContext());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DBHelper(getContext());
        view.findViewById(R.id.btn_nuevaOficina).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Vehiculo> prueba = dbHelper.get_all_Vehiculos();
                if (prueba.isEmpty()) {
                    Toast.makeText(getContext(), "No Existe un Vehiculo", Toast.LENGTH_SHORT).show();
                } else {
                    NavController navController = Navigation.findNavController(v);
                    navController.navigate(R.id.action_oficinaGobFragment_to_agregarOficinaFragment);
                }
            }
        });

        lv_oficinas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OficinaGob oficinaGob = (OficinaGob) parent.getItemAtPosition(position);
                OpcionesDialog(oficinaGob);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        ListarOficinas();
    }

    public void ListarOficinas() {
        ArrayList<OficinaGob> oficinas = dbHelper.get_all_Oficinas();
        OficinaAdapter adapter = new OficinaAdapter(getActivity(),oficinas);
        lv_oficinas.setAdapter(adapter);
    }

    public void OpcionesDialog(OficinaGob oficinaGob){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Seleccione una opción")
                .setItems(new String[]{"Editar", "Eliminar"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("id", oficinaGob.getIdOficina());
                                NavController navController = Navigation.findNavController(getView());
                                navController.navigate(R.id.action_oficinaGobFragment_to_actualizarOficinaFragment,bundle);
                                break;
                            case 1:
                                dbHelper.Eliminar_Oficina(oficinaGob);
                                EliminarWebService(oficinaGob.getIdOficina());
                                ListarOficinas();
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    private void EliminarWebService(int idoficinagobE){
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

        String urlWS = "http://" + selectedIp + "/db_grupo_05_tarea_16_ejercicio_01/OficinaEliminar.php?" +
                "idoficinagob="+idoficinagobE;

        stringRequest = new StringRequest(Request.Method.GET, urlWS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();

                if (response.trim().equalsIgnoreCase("elimina")) {
                    Toast.makeText(requireActivity(), "Oficina eliminada correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    if (response.trim().equalsIgnoreCase("noExiste")) {
                        Toast.makeText(requireActivity(), "No se encuentra la oficina", Toast.LENGTH_SHORT).show();
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