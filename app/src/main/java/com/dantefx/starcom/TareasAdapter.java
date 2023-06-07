package com.dantefx.starcom;

import static com.dantefx.starcom.EditActivityPresenter.ARG;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.dantefx.db.Administra;

public class TareasAdapter extends RecyclerView.Adapter<TareasAdapter.ViewHolder> {
    private Cursor mCursor;

    public TareasAdapter(Cursor cursor) {
        mCursor = cursor;
    }
    private static Context context;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNombre;
        public TextView tvDescripcion;
        public CheckBox tvEstado;
        public TextView tvPrioridad;
        public TextView tvFechaEntrega;
        public ImageButton buttonEditar;

        
        public ImageButton buttonEliminar;


        public ViewHolder(View itemView) {
            super(itemView);

            tvNombre = itemView.findViewById(R.id.texViewNombreTarea);
            tvDescripcion = itemView.findViewById(R.id.texViewDescripcionTarea);
            // Agrega el OnClickListener al TextView
            tvDescripcion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Obtén el texto completo de la descripción de la tarea
                    String descripcionCompleta = tvDescripcion.getText().toString();

                    // Crea un AlertDialog para mostrar el texto completo
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Descripción completa");
                    builder.setMessage(descripcionCompleta);
                    builder.setPositiveButton("Cerrar", null);

                    // Muestra el cuadro de diálogo
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

            tvEstado = itemView.findViewById(R.id.checkboxTarea);
            tvPrioridad = itemView.findViewById(R.id.textViewPrioridad);
            tvFechaEntrega = itemView.findViewById(R.id.textViewFechaEntrega);
            buttonEliminar = itemView.findViewById(R.id.imageButtonEliminar);
            buttonEditar = itemView.findViewById(R.id.imageButtonEditar);

            tvEstado.setText("");
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_add, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull TareasAdapter.ViewHolder holder, int position) {
        if (mCursor != null && mCursor.moveToPosition(position)) {
            String nombre = mCursor.getString(mCursor.getColumnIndexOrThrow("nombre"));
            String descripcion = mCursor.getString(mCursor.getColumnIndexOrThrow("descripcion"));
            int estado = mCursor.getInt(mCursor.getColumnIndexOrThrow("estado"));
            String prioridad = mCursor.getString(mCursor.getColumnIndexOrThrow("prioridad"));
            String fechaEntrega = mCursor.getString(mCursor.getColumnIndexOrThrow("fechaEntrega"));

            holder.tvNombre.setText(nombre);
            holder.tvDescripcion.setText(descripcion);

            holder.tvEstado.setClickable(false);

            if(estado == 0){
                holder.tvEstado.setChecked(false);
            }else {
                holder.tvEstado.setChecked(true);
            }
            holder.tvPrioridad.setText(prioridad);
            holder.tvFechaEntrega.setText(fechaEntrega);

            holder.buttonEliminar.setOnClickListener(view -> {
                int position1 = holder.getAdapterPosition();
                if (position1 != RecyclerView.NO_POSITION) {
                    Cursor cursor = mCursor;
                    if (cursor != null && cursor.moveToPosition(position1)) {
                        int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                        Administra bdTareas = new Administra(context);
                        bdTareas.borrarTarea(id);
                    }
                }
            });

            holder.buttonEditar.setOnClickListener(view -> {
                int position1 = holder.getAdapterPosition(); // Obtén la posición del registro actual
                if (position1 != RecyclerView.NO_POSITION) {
                    Cursor cursor = mCursor; // Obtén el cursor actual
                    if (cursor != null && cursor.moveToPosition(position1)) {
                        int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id")); // Obtén el ID del registro actual

                        Bundle bundle = new Bundle();
                        bundle.putInt(ARG, id); // Pasa el ID en lugar de la posición al fragmento

                        Navigation.findNavController(view).navigate(R.id.editarTareaFragment, bundle);
                    }
                }
            });



        }
    }



    @Override
    public int getItemCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    }


    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = newCursor;
        notifyDataSetChanged();
    }


}
