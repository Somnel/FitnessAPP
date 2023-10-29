package com.example.fitnessapp;

import static com.example.fitnessapp.db.ListaExercicios.getListaExercicios;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnessapp.db.classes.Exercicio;

import java.util.List;

public class Atividades extends AppCompatActivity {

    private TextView aviso;

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

        try {
            List<Exercicio> exercicioList = getListaExercicios(this);
            if(!exercicioList.isEmpty()) {
                setAviso(true);
            } else {
                aviso.setText(R.string.treinosAviso_completo);
                setAviso(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this,"Lista de Exercicios inválido " + e.toString(), Toast.LENGTH_LONG).show();
            aviso.setText(R.string.treinosAviso_erro);
            setAviso(true);
        }
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