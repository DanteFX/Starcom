package com.dantefx.starcom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationActionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case "SILENCIAR":
                    // Acción para silenciar la notificación
                    break;
                case "IGNORAR":
                    // Acción para ignorar la notificación
                    break;
                case "QUITAR_SONIDO":
                    // Acción para quitar el sonido de la notificación
                    break;
            }
        }
    }
}
