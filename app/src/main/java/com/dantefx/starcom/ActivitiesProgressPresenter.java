package com.dantefx.starcom;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.Date;

import com.dantefx.db.Administra;
import com.dantefx.starcom.databinding.ActivitiesViewBinding;
public class ActivitiesProgressPresenter extends Fragment {

    private ActivitiesViewBinding binding;
    private Administra administra;
    private SimpleCursorAdapter adaptador;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {


        binding = ActivitiesViewBinding.inflate(inflater, container, false);

        TextView dateView = binding.fecha;
        setDate(dateView);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        binding.buttonVerTareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ActivitiesProgressPresenter.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
        binding.buttonAgregarTareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ActivitiesProgressPresenter.this)
                        .navigate(R.id.action_FirstFragment_to_agregarTareaFragment);
            }
        });

        administra = new Administra(getContext());
        Cursor cursor = administra.obtenerNombreTarea();

        //Se debe mostrar solo el nombre pero me aparecen lso nombres de las demas columnas
        String[] desde = {"nombre"};
        int[] a = {R.id.rowFirstText};
        adaptador = new SimpleCursorAdapter(requireContext(), R.layout.row_first, cursor, desde, a, 0);

        ListView listaTareas = binding.listTareasFirst;
        listaTareas.setAdapter(adaptador);

       /* Spinner spinnerEtapa = view.findViewById(R.id.spinnerPrioridad);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.etapa, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEtapa.setAdapter(adapter);*/



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void setDate (TextView view){
        String str = String.format("%tc", new Date());
        view.setText(str.substring(0,10));
    }


}