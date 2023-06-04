package com.dantefx.db;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.dantefx.starcom.CreateActivityPresenter;

public class Administra extends BDManager {
    Context context;

    public Administra(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long insertarTarea(String nombre, String descripcion, int estado , String prioridad, String fechaEntrega, String fechaInicio){
        long id = 0;
        try {
            BDManager BDManager = new BDManager(context.getApplicationContext());
            SQLiteDatabase db = BDManager.getWritableDatabase();

            ContentValues values = new ContentValues();
            if (nombre != null && descripcion != null && prioridad != null && fechaEntrega != null && fechaInicio != null) {
                values.put("nombre", nombre);
                values.put("descripcion", descripcion);
                values.put("estado", estado);
                values.put("prioridad", prioridad);
                values.put("fechaEntrega", fechaEntrega);
                values.put("fechaInicio", fechaInicio); // Agrega la fecha de inicio
                id = db.insert(TABLE_TAREA, null, values);
            }
        }catch (Exception ex){
            ex.toString();
        }
        return id;
    }

    public Cursor obtenerTareas() {
        SQLiteDatabase db = getReadableDatabase();
        String[] columnas = {"id as _id","nombre", "descripcion", "estado", "prioridad", "fechaEntrega"};
        Cursor cursor = db.query(TABLE_TAREA, columnas, null, null, null, null, null);
        return cursor;
    }

    public Cursor obtenerNombreTarea(){
        SQLiteDatabase db = getReadableDatabase();
        String[] columnas = {"id as _id", "nombre"};
        Cursor cursor = db.query(TABLE_TAREA, columnas, null, null, null, null, null);
        return cursor;
    }

    public Cursor obtenerTarea(long id){
        SQLiteDatabase db = getReadableDatabase();
        String[] columnas = {"id as _id","nombre", "descripcion", "estado", "prioridad", "fechaEntrega"};
        Cursor cursor = db.query(TABLE_TAREA, columnas, null, null, null, null, null);
        return cursor;
    }
    public void borrarTarea(int id){
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "id=?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        db.delete(TABLE_TAREA, whereClause, whereArgs);
    }

    public void borrarTodasLasTareas() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TAREA, null, null);
    }

    public boolean actualizarTarea(int id, String nombre, String descripcion, String prioridad, String fechaEntrega) {
        BDManager dbHelper = new BDManager(context.getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put("nombre", nombre);
            values.put("descripcion", descripcion);
            values.put("prioridad", prioridad);
            values.put("fechaEntrega", fechaEntrega);

            String whereClause = "id=?";
            String[] whereArgs = new String[]{String.valueOf(id)};

            int numRowsUpdated = db.update(TABLE_TAREA, values, whereClause, whereArgs);

            return numRowsUpdated > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }


    public long obtenerIdRegistroActual(ListView listView) {
        int position = listView.getCheckedItemPosition();
        if (position != ListView.INVALID_POSITION) {
            return listView.getItemIdAtPosition(position);
        }
        return -1; // Devuelve un valor adecuado según tu implementación
    }

    private void crearNotificacion(long tareaId, String nombreTarea, String recordatorio) {
        // Definir el identificador del canal de notificación
        String CHANNEL_ID = "my_channel_id";

        // Obtener el tiempo de recordatorio en milisegundos (suponiendo que está en minutos)
        long tiempoRecordatorio = Long.parseLong(recordatorio) * 60 * 1000;

        // Crear una intención para la notificación
        Intent intent = new Intent(context, CreateActivityPresenter.class);
        intent.putExtra("tarea_id", tareaId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Crear un canal de notificación (solo es necesario hacerlo una vez)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "My Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Construir la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Recordatorio de tarea")
                .setContentText("La tarea '" + nombreTarea + "' está pendiente")
                //.setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Programar la notificación para el tiempo de recordatorio
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + tiempoRecordatorio, pendingIntent);

        // Mostrar la notificación
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) tareaId, builder.build());
    }


}