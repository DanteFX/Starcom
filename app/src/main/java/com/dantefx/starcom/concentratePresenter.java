package com.dantefx.starcom;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dantefx.starcom.databinding.ConcentrateViewBinding;

public class concentratePresenter extends Fragment {

    private ConcentrateViewBinding binding;
    public MediaPlayer mp;
    private int num=0;
    private AudioPlayer ap;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = ConcentrateViewBinding.inflate(inflater, container, false);
        //mp = MediaPlayer.create(this.getContext(), R.raw.concentracion);
        ap=new AudioPlayer(this.getContext(), R.raw.concentracion);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                num+=1;
                isPlayingButton();
            }
        });
    }

    @Override
    public void onDestroyView() {
        ap.stopPulse();
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onResume() {
        super.onResume();
    }


    public void isPlayingButton(){
        if (ap.isPlaying()) {
            ap.stopPulse();
            binding.buttonSecond.setImageResource(android.R.drawable.ic_media_play);

        }else{
            ap.playPulse();
            binding.buttonSecond.setImageResource(android.R.drawable.ic_media_pause);
        }
    }

}