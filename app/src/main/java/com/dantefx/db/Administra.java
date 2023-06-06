package com.dantefx.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;

import androidx.annotation.Nullable;

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




}