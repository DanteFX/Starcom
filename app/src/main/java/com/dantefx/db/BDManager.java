package com.dantefx.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

//Base de datos
public class BDManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NOMBRE = "starcom.db";
    public static final String TABLE_TAREA = "TAREA";

    public BDManager(@Nullable Context context) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_TAREA + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT ," +
                "nombre TEXT NOT NULL," +
                "descripcion TEXT NOT NULL," +
                "estado BOOLEAN(1) NOT NULL," +
                "fechaEntrega TEXT NOT NULL," +
                "prioridad TEXT NOT NULL," +
                "usuario TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion < 4) {
            // Agregar los nuevos campos a la tabla de tareas
            sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_TAREA + " ADD COLUMN fechaInicio TEXT");
            sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_TAREA + " ADD COLUMN fechaFin TEXT");
            sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_TAREA + " ADD COLUMN progreso INTEGER");
        }


    }

}

