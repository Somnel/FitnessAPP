package com.example.fitnessapp;

import static com.example.fitnessapp.db.ListaExercicios.getListaExercicios;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnessapp.db.classes.UsuarioSession;

public class Principal extends AppCompatActivity {

    private TextView perfilText;
    private TextView configText;

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

        try {
            getListaExercicios(this);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    // Redirecionamento
    private void toPerfil() {
        Intent intent = new Intent(this, Perfil.class);
        startActivity(intent);
        finish();
    }

    private void toConfig() {}


    private void deslog() { UsuarioSession.logOut(this); }


}