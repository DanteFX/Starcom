package com.dantefx.starcom;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
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

    private Spinner spinnerRec;
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
                int recordatorio = Integer.parseInt(spinnerRec.getSelectedItem().toString());


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
                    Toast.makeText(getContext(), "TAREA GUARDADO", Toast.LENGTH_SHORT).show();
                    limpiar();
                    crearNotificacion(id, nombre, String.valueOf(recordatorio));
                    Cursor nuevoCursor = bdTareas.obtenerTareas();

                    // Actualizar el adaptador con el nuevo Cursor
                    //tareasAdapter.swapCursor(nuevoCursor);
                } else {
                    Toast.makeText(getContext(), "ERROR AL GUARDAR LA TAREA", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    private void crearNotificacion(long tareaId, String nombreTarea, String recordatorio) {
        // Definir el identificador del canal de notificación
        String CHANNEL_ID = "my_channel_id";

        // Obtener el tiempo de recordatorio en milisegundos (suponiendo que está en minutos)
        long tiempoRecordatorio = Long.parseLong(recordatorio) * 60 *60 * 1000;

        // Crear una intención para la notificación
        Intent intent = new Intent(getContext(), CreateActivityPresenter.class);
        intent.putExtra("tarea_id", tareaId);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        // Crear un canal de notificación (solo es necesario hacerlo una vez)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "My Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Construir la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setContentTitle("Recordatorio de tarea")
                .setContentText("La tarea '" + nombreTarea + "' está pendiente")
                .setSmallIcon(R.drawable.yoga48)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Programar la notificación para el tiempo de recordatorio
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + tiempoRecordatorio, pendingIntent);

        // Mostrar la notificación
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) tareaId, builder.build());
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

        spinnerRec = view.findViewById(R.id.idSpinnerRecordatorio);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this.getContext(), R.array.horas_array,
                android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRec.setAdapter(adapter1);

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
        spinnerRec.setSelection(0);
        selectedDateTV.setText("");
    }


}