package com.example.fitnessapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MimeTypes;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.example.fitnessapp.db.classes.Exercicio;

import java.time.LocalTime;
import java.util.ArrayList;

public class Treinos extends AppCompatActivity {

    private TextView aviso;
    private ArrayList<ExoPlayer> players = new ArrayList<>();
    private int currentPlayingIndex = -1;


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, Principal.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release the ExoPlayer instance
        if (players != null) {
            for(ExoPlayer player : players) {
                player.release();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treinos);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init() {
        aviso = findViewById(R.id.treinos_aviso);
        setAviso(false);

        Button exercSequencia = findViewById(R.id.treinos_btnExercs);
        Context context = this;
        exercSequencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FragmentExercicio.class);
                startActivity(intent);
                finish();
            }
        });

        ArrayList<Exercicio> exercicios = setExercicioStatic();

        configureRow(exercicios.get(0), R.id.treinos_row1, R.raw.elevacaofrontal, 0);
        configureRow(exercicios.get(1), R.id.treinos_row2, R.raw.abdominalobliquo, 1);
        configureRow(exercicios.get(2), R.id.treinos_row3, R.raw.elevacaolateral, 2);
        configureRow(exercicios.get(3), R.id.treinos_row4, R.raw.flexaocotovelo, 3);
    }

    private void setupExoPlayerRow(PlayerView videoView, Uri videoUri) {
        ExoPlayer player = new ExoPlayer.Builder(this).build();
        players.add(player); 
        videoView.setPlayer(player);
        
        MediaItem mediaItem = new MediaItem.Builder()
                .setUri(videoUri)
                .setMimeType(MimeTypes.APPLICATION_MP4)
                .build();

        player.setMediaItem(mediaItem);
        player.setPlayWhenReady(true);
    }

    private void configureRow(Exercicio exercicio, int viewID, int videorawID, int index) {
        LinearLayout layout = findViewById(viewID);
        String title = exercicio.getNome();
        String description = exercicio.getDescricao();

        PlayerView videoView = layout.findViewById(R.id.exerc_rowIlustracao_video);
        videoView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT
        ));
        TextView titulo = layout.findViewById(R.id.exerc_rowTitulo);
        titulo.setText(title);
        TextView descricao = layout.findViewById(R.id.exerc_rowTexto);
        descricao.setText(description);

        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + videorawID);
        setupExoPlayerRow(videoView, videoUri);

        // Configurar um clique para iniciar a reprodução do vídeo
        videoView.setOnClickListener(v -> {
            if (currentPlayingIndex != index) {
                // Se outro vídeo estava sendo reproduzido, pare-o primeiro
                if (currentPlayingIndex != -1) {
                    ExoPlayer currentPlayer = players.get(currentPlayingIndex);
                    if (currentPlayer != null) {
                        currentPlayer.stop();
                    }
                }
                // Inicie a reprodução do novo vídeo
                ExoPlayer newPlayer = players.get(index);
                if (newPlayer != null) {
                    newPlayer.setPlayWhenReady(true);
                    currentPlayingIndex = index;
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<Exercicio> setExercicioStatic() {


        // > 1
        Exercicio exerc1 = new Exercicio();
            exerc1.setNome("Elevação Frontal");
            exerc1.setIllustracao("elevacaofrontal.mp4");
            exerc1.setTipo('0');
            exerc1.setDescricao("A elevação frontal é um exercício de fortalecimento dos músculos deltoides, localizados nos ombros. Ele envolve levantar pesos na frente do corpo, direcionando o esforço para a parte anterior dos ombros. É um exercício eficaz para desenvolver a força e a definição dos deltoides, sendo comumente incluído em treinamentos para ombros.");
            exerc1.setDuracao(LocalTime.of(0, 30));
            exerc1.setLimite_semanal(5);

        // > 2
        Exercicio exerc2 = new Exercicio();
            exerc2.setNome("Abdominal Oblíquo");
            exerc2.setIllustracao("abdominalobliquo.mp4");
            exerc2.setTipo('0');
            exerc2.setDescricao("O abdominal oblíquo é um exercício eficaz para fortalecer os músculos oblíquos do abdômen, que ajudam a criar uma cintura mais definida. Ao executar este exercício, você trabalha os músculos laterais do abdômen, contribuindo para um núcleo mais forte e equilibrado. Lembre-se de manter uma postura adequada durante a execução e respirar regularmente para obter os melhores resultados.");
            exerc2.setDuracao(LocalTime.of(0, 15)); // Defina a duração desejada, aqui definimos 15 minutos.
            exerc2.setLimite_semanal(4); // Defina o limite semanal desejado, aqui definimos como 4.

        // > 3
        Exercicio exerc3 = new Exercicio();
            exerc3.setNome("Elevação Lateral");
            exerc3.setIllustracao("elevacaolateral.mp4");
            exerc3.setTipo('0');
            exerc3.setDescricao("A elevação lateral é um exercício que visa fortalecer os músculos deltoides laterais, localizados nos ombros. Ao elevar os braços para os lados, você trabalha a parte externa dos ombros, contribuindo para o desenvolvimento de ombros mais amplos e definidos. Mantenha uma postura adequada durante a execução e use pesos adequados para evitar lesões.");
            exerc3.setDuracao(LocalTime.of(0, 20)); // Defina a duração desejada, aqui definimos 20 minutos.
            exerc3.setLimite_semanal(3); // Defina o limite semanal desejado, aqui definimos como 3.

        // > 4
        Exercicio exerc4 = new Exercicio();
            exerc4.setNome("Flexão de Cotovelo");
            exerc4.setIllustracao("flexaocotovelo.mp4");
            exerc4.setTipo('0');
            exerc4.setDescricao("A flexão de cotovelo, também conhecida como flexão de braço, é um exercício fundamental para fortalecer os músculos do peito, ombros e tríceps. Ela pode ser realizada em diferentes variações para atender às necessidades individuais. Ao fazer flexões de cotovelo regularmente, você desenvolverá força nos membros superiores e aumentará sua resistência. Certifique-se de manter uma postura correta durante a execução e respire de maneira controlada.");
            exerc4.setDuracao(LocalTime.of(0, 25)); // Defina a duração desejada, aqui definimos 25 minutos.
            exerc4.setLimite_semanal(5); // Defina o limite semanal desejado, aqui definimos como 5.

        ArrayList<Exercicio> exercicios = new ArrayList<>();
        exercicios.add(exerc1);
        exercicios.add(exerc2);
        exercicios.add(exerc3);
        exercicios.add(exerc4);
        return exercicios;
    }


    private void setAviso(boolean ativo) {
        if(ativo) {
            aviso.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            aviso.setVisibility(View.VISIBLE);
            aviso.setEnabled(true);
        } else {
            aviso.getLayoutParams().height = 0;
            aviso.setVisibility(View.INVISIBLE);
            aviso.setEnabled(false);
        }
    }


}