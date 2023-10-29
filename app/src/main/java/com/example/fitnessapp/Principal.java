package com.example.fitnessapp;

import static com.example.fitnessapp.db.ListaExercicios.getListaExercicios;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.fitnessapp.db.classes.UsuarioSession;

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

        try {
            getListaExercicios(this);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this,"Lista de Exercicios inválido " + e, Toast.LENGTH_LONG).show();
        }



        defbottomnavbar(R.id.button1, "Meus Treinos", Treinos.class);
        defbottomnavbar(R.id.button2, "Minhas Atividades", Atividades.class);
        defbottomnavbar(R.id.button3, "Monitore", Monitore.class);
        defbottomnavbar(R.id.button4, "Análise", Analise.class);
        defbottomnavbar(R.id.button5, "Conteúdo", Conteudo.class);
    }

    private void defbottomnavbar(int viewById, String meusTreinos, Class pag) {
        Context context = this;
        Button btn = layout.findViewById(viewById);
        btn.setText(meusTreinos);

        btn.setOnClickListener(new View.OnClickListener() {
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