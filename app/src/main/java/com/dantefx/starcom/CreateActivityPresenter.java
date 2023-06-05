package com.dantefx.starcom;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.widget.TextView;

import com.dantefx.db.Administra;
import com.google.android.material.textfield.TextInputLayout;


public class CreateActivityPresenter extends Fragment {

    private ImageButton pickDateBtn;
    private TextView selectedDateTV;
    private ImageButton guardar;
    private TextInputLayout campoNombre;
    private TextInputLayout campoDescripcion;
    private Spinner spinner;
    int estado = 0;

    private TareasAdapter tareasAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_activity_view, container, false);

        // Asignar el listener del botón guardar aquí.
        guardar = view.findViewById(R.id.idBtnAgregar);
        guardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String nombre = campoNombre.getEditText().getText().toString();
                String descripcion = campoDescripcion.getEditText().getText().toString();
                String prioridad = spinner.getSelectedItem().toString();
                String fechaEntrega = selectedDateTV.getText().toString();
                int recordatorio = 1;


                // Agregar la fecha de inicio automaticamente desde el sistema
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String fechaInicio = dateFormat.format(calendar.getTime());


                // Verificar que los campos no sean nulos o vacíos
                if (nombre.isEmpty() || descripcion.isEmpty() || prioridad.isEmpty() || fechaEntrega.isEmpty()) {
                    Toast.makeText(getContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                    return; // Detener el flujo de ejecución
                }

                Administra bdTareas = new Administra(getContext());
                long id = bdTareas.insertarTarea(nombre, descripcion, estado, prioridad, fechaEntrega, fechaInicio, recordatorio);

                if (id > 0) {
                    Toast.makeText(getContext(), "REGISTRO GUARDADO", Toast.LENGTH_SHORT).show();
                    limpiar();

                    Cursor nuevoCursor = bdTareas.obtenerTareas();

                    // Actualizar el adaptador con el nuevo Cursor
                    //tareasAdapter.swapCursor(nuevoCursor);
                } else {
                    Toast.makeText(getContext(), "ERROR AL GUARDAR EL REGISTRO", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {



        pickDateBtn = view.findViewById(R.id.idBtnPickDate);
        selectedDateTV = view.findViewById(R.id.idTVSelectedDate);
        campoNombre = view.findViewById(R.id.campoTareaLayout);
        campoDescripcion = view.findViewById(R.id.campoDescripcionLayout);
        spinner = view.findViewById(R.id.idSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.prioridades_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        pickDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        CreateActivityPresenter.this.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                selectedDateTV.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },
                        year, month,day);

                datePickerDialog.show();
            }
        });
    }

    private void limpiar() {
        campoNombre.getEditText().setText("");
        campoDescripcion.getEditText().setText("");
        spinner.setSelection(0);
        selectedDateTV.setText("");
    }


}