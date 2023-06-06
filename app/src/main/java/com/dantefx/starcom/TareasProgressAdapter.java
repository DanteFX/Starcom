package com.dantefx.starcom;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dantefx.db.Administra;
import com.dantefx.db.BDManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TareasProgressAdapter extends RecyclerView.Adapter<TareasProgressAdapter.ViewHolder> implements AdapterView.OnItemSelectedListener {

    private Cursor cursor;
    private Context context;


    public TareasProgressAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNombre;
        public ProgressBar progreso;
        public Spinner etapa;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.rowFirstText);
            progreso = itemView.findViewById(R.id.activeProgress);
            etapa = itemView.findViewById(R.id.spinnerEtapa);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_first, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (cursor != null && cursor.moveToPosition(position)) {

            String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));

            holder.tvNombre.setText(nombre);

            // Obtener el progreso de la base de datos
            int progreso = cursor.getInt(cursor.getColumnIndexOrThrow("progreso"));

            // Establecer el progreso en la barra de progreso
            holder.progreso.setProgress(progreso, true);

        }
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.etapa, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.etapa.setAdapter(adapter);
        holder.etapa.setTag(holder);
        holder.etapa.setOnItemSelectedListener(this);

    }


    private long calcularTiempoTranscurridoEnDias(String fechaInicio, String fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            return 0;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date fechaInicioObj = dateFormat.parse(fechaInicio);
            Date fechaFinObj = dateFormat.parse(fechaFin);
            long diferencia = fechaFinObj.getTime() - fechaInicioObj.getTime();
            long diasTranscurridos = TimeUnit.MILLISECONDS.toDays(diferencia);
            return diasTranscurridos;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void progressChange(int pos, ViewHolder holder) {
        int progreso = calcularProgreso(pos);
        holder.progreso.setProgress(progreso, true);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
        ViewHolder holder = (ViewHolder) parent.getTag();
        progressChange(pos, holder);

        // Obtener el ID de la tarea actual
        int idTarea = cursor.getInt(cursor.getColumnIndexOrThrow("id"));

        // Calcular el progreso en función de las etapas
        int progreso = calcularProgreso(pos);

        // Obtener el progreso actual en la base de datos
        int progresoActual = obtenerProgresoDesdeBaseDeDatos(idTarea);

        // Verificar si el progreso ha cambiado
        if (progreso != progresoActual) {
            // Actualizar el campo de progreso en la tabla "tareas" en la base de datos
            ContentValues progresoValues = new ContentValues();
            progresoValues.put("progreso", progreso);

            String whereClause = "id=?";
            String[] whereArgs = new String[]{String.valueOf(idTarea)};
            SQLiteDatabase db = getWritableDatabase(context);
            db.update("TAREA", progresoValues, whereClause, whereArgs);

            if (pos == 3) {
                // La tarea ha llegado a la etapa de "Cierre"
                String fechaActual = obtenerFechaActual();

                // Actualizar la columna "fechaFin" en la base de datos
                ContentValues values = new ContentValues();
                values.put("fechaFin", fechaActual);
                values.put("estado", 1);
                db.update("TAREA", values, whereClause, whereArgs);

                // Obtener los datos de la tarea actual
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String fechaInicio = cursor.getString(cursor.getColumnIndexOrThrow("fechaInicio"));
                String fechaFin = cursor.getString(cursor.getColumnIndexOrThrow("fechaFin"));
                long tiempoTranscurrido = calcularTiempoTranscurridoEnDias(fechaInicio, fechaFin);

                // Mostrar un mensaje con el tiempo transcurrido en días
                String mensaje = "La tarea '" + nombre + "' ha sido completada en " + tiempoTranscurrido + " días.";
                Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
            } else {
                // Actualizar el campo de progreso en la tabla "tareas" en la base de datos
                ContentValues values = new ContentValues();
                values.put("progreso", progreso);
                db.update("TAREA", values, whereClause, whereArgs);
            }
        }
    }

    private int obtenerProgresoDesdeBaseDeDatos(int idTarea) {
        // Realizar una consulta a la base de datos para obtener el progreso actual de la tarea
        SQLiteDatabase db = getReadableDatabase(context);
        String[] columns = {"progreso"};
        String selection = "id=?";
        String[] selectionArgs = new String[]{String.valueOf(idTarea)};
        Cursor cursor = db.query("TAREA", columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            int progreso = cursor.getInt(cursor.getColumnIndexOrThrow("progreso"));
            cursor.close();
            return progreso;
        } else {
            cursor.close();
            return 0; // Valor predeterminado si no se encuentra el progreso en la base de datos
        }
    }


    private int calcularProgreso(int pos) {
        int progreso = 0;
        switch (pos) {
            case 0:
                progreso = 25;
                break;
            case 1:
                progreso = 50;
                break;
            case 2:
                progreso = 75;
                break;
            case 3:
                progreso = 100;
                break;
        }
        return progreso;
    }

    private String obtenerFechaActual() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date fechaActual = new Date();
        return dateFormat.format(fechaActual);
    }

    private SQLiteDatabase getWritableDatabase(Context context) {
        BDManager dbHelper = new BDManager(context);
        return dbHelper.getWritableDatabase();
    }

    private SQLiteDatabase getReadableDatabase(Context context) {
        BDManager dbHelper = new BDManager(context);
        return dbHelper.getReadableDatabase();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

}

