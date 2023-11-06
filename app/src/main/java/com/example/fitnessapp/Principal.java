package com.example.fitnessapp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.fitnessapp.db.classes.UsuarioSession;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Principal extends AppCompatActivity {

    private TextView perfilText;
    private TextView configText;
    private LinearLayout layout;

    private static final int TEMPO_LIMITE_SAIDA = 2000; // Tempo limite para pressionar o botão "Back" novamente (em milissegundos)
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            deslog();
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Pressione novamente para sair", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false; // Reseta o contador após o tempo limite
            }
        }, TEMPO_LIMITE_SAIDA);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        UsuarioSession usuarioSession = UsuarioSession.getInstance(this);
        if(!usuarioSession.isLogged()) { deslog(); }
        init();

        // Altera a cor da barra de status (Android 5.0 e posterior)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.p1));
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.p1));
        }



        perfilText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toPerfil();
            }
        });

        configText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toConfig();
            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init() {
        perfilText = findViewById(R.id.btnPerfil);
        configText = findViewById(R.id.btnConfig);
        layout = findViewById(R.id.principal_bottomnav);
        TextView txtProgress = findViewById(R.id.Principal_progressBar_text);
        TextView txtProgressDate = findViewById(R.id.Principal_progressBar_text_date);

        txtProgressDate.setText( LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) );
        txtProgress.setText("AINDA NÃO CONCLUIDO");

        ProgressBar progressBar = findViewById(R.id.Principal_progressBar);
        progressBar.setIndeterminate(false);
        progressBar.setProgress(40);

        defbottomnavbar(R.id.principal_nav_item1, R.drawable.icontreino,  "Meus Treinos", Treinos.class);
        defbottomnavbar(R.id.principal_nav_item2, R.drawable.iconeatividade, "Minhas Atividades", Atividades.class);
        defbottomnavbar(R.id.principal_nav_item3, R.drawable.iconemonitore, "Monitore", Monitore.class);
        defbottomnavbar(R.id.principal_nav_item4, R.drawable.iconeanalise, "Análise", Analise.class);
        defbottomnavbar(R.id.principal_nav_item5, R.drawable.iconeconteudo, "Conteúdo", Conteudo.class);
    }

    private void defbottomnavbar(int layoutID, int imageID_draw, String meusTreinos, Class pag) {
        Context context = this;

        LinearLayout layout = findViewById(layoutID);

        TextView text = layout.findViewById(R.id.principal_nav_text);
        text.setText(meusTreinos);

        ImageView img = layout.findViewById(R.id.principal_nav_image);
        img.setImageResource(imageID_draw);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, pag);
                startActivity(intent);
                finish();
            }
        });
    }

    // Redirecionamento
    private void toPerfil() {
        Intent intent = new Intent(this, Perfil.class);
        startActivity(intent);
        finish();
    }

    private void toConfig() {
        Intent intent = new Intent(this, Configuracoes.class);
        startActivity(intent);
        finish();
    }


    private void deslog() { UsuarioSession.logOut(this); }


}