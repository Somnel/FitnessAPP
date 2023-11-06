package com.example.fitnessapp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.example.fitnessapp.db.classes.Exercicio;

import java.time.LocalTime;
import java.util.ArrayList;

public class Atividades extends AppCompatActivity {

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividades);

        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init() {
        aviso = findViewById(R.id.atividades_aviso);

        Button exercSequencia = findViewById(R.id.atividades_btnExercs);
        Context context = this;
        exercSequencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FragmentExercicio.class);
                startActivity(intent);
                finish();
            }
        });

        ArrayList<Exercicio> exercicios = setAtividadesStatic();

        configureRow(exercicios.get(0), R.id.atividades_row1, null, 0);
        configureRow(exercicios.get(1), R.id.atividades_row2, null, 1);
    }




    private void configureRow(Exercicio exercicio, int viewID, Integer videorawID, int index) {
        LinearLayout layout = findViewById(viewID);
        String title = exercicio.getNome();
        String description = exercicio.getDescricao();

        PlayerView videoView = layout.findViewById(R.id.exerc_rowIlustracao_video);
        TextView titulo = layout.findViewById(R.id.exerc_rowTitulo);
        titulo.setText(title);
        TextView descricao = layout.findViewById(R.id.exerc_rowTexto);
        descricao.setText(description);

        if(videorawID == null) {
            videoView.setEnabled(false);
            videoView.setVisibility(View.INVISIBLE);
            videoView.getLayoutParams().height = 0;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<Exercicio> setAtividadesStatic() {
        Exercicio exerc1 = new Exercicio();
        exerc1.setNome("Dança");
        // exerc1.setIllustracao("danca.gif");
        exerc1.setTipo('1');
        exerc1.setDescricao("A dança é uma atividade que combina movimento físico com elementos cognitivos, como ritmo, coordenação e expressão. Praticar dança é uma ótima maneira de melhorar a condição física, enquanto estimula a mente e a criatividade. Escolha seu estilo de dança favorito e divirta-se!");
        exerc1.setDuracao(LocalTime.of(0, 45)); // Defina a duração desejada, aqui definimos 45 minutos.
        exerc1.setLimite_semanal(3); // Defina o limite semanal desejado, aqui definimos como 3.

        Exercicio exerc2 = new Exercicio();
        exerc2.setNome("Palavras Cruzadas");
        // exerc2.setIllustracao("palavras-cruzadas.jpg"); // Você pode definir uma ilustração adequada para palavras cruzadas.
        exerc2.setTipo('1');
        exerc2.setDescricao("As palavras cruzadas são um desafio cognitivo divertido e estimulante. Resolvê-las envolve a dedução de palavras e o preenchimento de um quebra-cabeça de palavras. Este exercício é uma ótima maneira de exercitar o seu vocabulário, pensamento lógico e habilidades de resolução de quebra-cabeças.");
        exerc2.setDuracao(LocalTime.of(0, 30)); // Defina a duração desejada, aqui definimos 30 minutos.
        exerc2.setLimite_semanal(7); // Defina o limite semanal desejado, aqui definimos como 7.

        ArrayList<Exercicio> exercicios = new ArrayList<>();
        exercicios.add(exerc1);
        exercicios.add(exerc2);

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