package com.dantefx.starcom;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.widget.Toast;

public class AudioPlayer {
    private Context context;
    private int path;
    public static MediaPlayer mp = new MediaPlayer();

    public  AudioPlayer (Context context, int path) {
        this.context=context;
        this.path=path;
        mp = MediaPlayer.create(this.context, this.path);
    }

    public void playPulse(){
        if (!isPlaying()) {
            mp.start();
            Toast.makeText(context, "Puedes apagar la pantalla y seguir relajandote", Toast.LENGTH_SHORT).show();
        }
    }
    public void stopPulse(){
        mp.stop();
        mp.seekTo(0);

    }

    public boolean isPlaying(){
        AudioManager manager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        if(manager.isMusicActive()){
            Toast.makeText(context, "No se puede reproducir mientras haya audio en reproducción", Toast.LENGTH_SHORT).show();
        }
        return manager.isMusicActive();
    }
}
