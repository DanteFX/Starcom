package com.dantefx.starcom;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

import android.widget.TextView;

import com.dantefx.db.Administra;
import com.google.android.material.textfield.TextInputLayout;


public class EditActivityPresenter extends Fragment {

    Button btn_time, btn_date, btn_done;
    ImageView btn_record;
    EditText editext_message;
    String timeTonotify;
    //DatabaseClass databaseClass;

    private ImageButton pickDateBtn;
    private TextView selectedDateTV;
    private ImageButton guardar;
    private TextInputLayout campoNombre;
    private TextInputLayout campoDescripcion;
    private Spinner spinner;
    int estado = 1;


    private int position1;
    public static final String ARG = "POS";

    private Bundle bundle = new Bundle();

    private TareasAdapter tareasAdapter;

    public EditActivityPresenter(){}

    public static EditActivityPresenter newInstance (Integer position1){
        EditActivityPresenter fragment = new EditActivityPresenter();
        Bundle args = new Bundle();

        args.putInt(ARG, position1);
        fragment.setArguments(args);
        return fragment;
    }
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        if(getArguments() != null){
            position1 = getArguments().getInt(ARG);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_activity_view, container, false);

        // Asignar el listener del botón guardar aquí.
        guardar = view.findViewById(R.id.idBtnActualizar);
        guardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String nombre = campoNombre.getEditText().getText().toString();
                String descripcion = campoDescripcion.getEditText().getText().toString();
                String prioridad = spinner.getSelectedItem().toString();
                String fechaEntrega = selectedDateTV.getText().toString();





                // Obtener el ID del registro que se va a actualizar
                Administra bdTareas = new Administra(getContext());


                boolean actualizacionExitosa = bdTareas.actualizarTarea(position1, nombre, descripcion, prioridad, fechaEntrega);

                System.out.println(actualizacionExitosa);

                try {
                    if (actualizacionExitosa) {
                        Toast.makeText(getContext(), "REGISTRO ACTUALIZADO" + position1, Toast.LENGTH_SHORT).show();
                        limpiar();

                        Cursor nuevoCursor = bdTareas.obtenerTareas();

                        // Actualizar el adaptador con el nuevo Cursor
                        tareasAdapter.swapCursor(nuevoCursor);
                    } else {
                        Toast.makeText(getContext(), "ERROR AL ACTUALIZAR EL REGISTRO"  + position1, Toast.LENGTH_LONG).show() ;
                    }
                }catch (Exception e) {
                    e.printStackTrace();
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
                        EditActivityPresenter.this.getContext(),
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