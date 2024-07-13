package com.example.grupo_05_tarea_16_ejercicio_01.fragments.Audiencia;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.grupo_05_tarea_16_ejercicio_01.R;

public class AudienciaFragment extends Fragment {

    public AudienciaFragment() {
        // Required empty public constructor
    }


    public static AudienciaFragment newInstance(String param1, String param2) {
        AudienciaFragment fragment = new AudienciaFragment();
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
        View view = inflater.inflate(R.layout.fragment_audiencia, container, false);


        return view;
    }
}