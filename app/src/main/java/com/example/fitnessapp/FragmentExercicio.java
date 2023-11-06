package com.example.fitnessapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class FragmentExercicio extends AppCompatActivity {

    AppCompatButton btnAvancar;
    SeekBar seekAvancar;

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, Principal.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercicio_fragment);

        init();

        btnAvancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(seekAvancar.getProgress() < seekAvancar.getMax()) {
                    seekAvancar.setProgress(seekAvancar.getProgress() + 1);
                }
            }
        });
    }

    private void init(){
        seekAvancar = findViewById(R.id.exercfrag_seekbarAvanco);
        btnAvancar = findViewById(R.id.exercfrag_btnAvancar);


        TextView titulo = findViewById(R.id.exerc_fragment_titulo);
        titulo.setText("Elevação Frontal");
        TextView duracao = findViewById(R.id.exerc_fragment_duracao);
        duracao.setText("30 Minutos");
        VideoView video = findViewById(R.id.exerc_fragment_video);

        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.elevacaofrontal;

        Uri uri = Uri.parse(videoPath);
        video.setVideoURI(uri);

        MediaController mediaController = new MediaController(this);
        video.setMediaController(mediaController);
        mediaController.setAnchorView(video);
    }
}
