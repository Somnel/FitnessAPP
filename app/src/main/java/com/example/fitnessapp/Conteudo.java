package com.example.fitnessapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.ConteudoClasses.Conteudo_RecyclerViewAdapter;
import com.example.fitnessapp.db.classes.Exercicio;
import com.example.fitnessapp.db.classes.ExercicioDao;
import com.example.fitnessapp.db.classes.UsuarioSession;

import java.util.ArrayList;

public class Conteudo extends AppCompatActivity {


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
        setContentView(R.layout.activity_conteudo);
        if(!UsuarioSession.getInstance(this).isLogged()) { UsuarioSession.logOut(this); }

        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init() {
        ExercicioDao dao = new ExercicioDao(this);
        ArrayList<Exercicio> exercicios = new ArrayList<>();
        TextView aviso = findViewById(R.id.monitore_avisoHistoricoVazio);

        try {
            exercicios.addAll( dao.buscar('0', null, null, null) );
            exercicios.addAll( dao.buscar('1', null, null, null) );
            exercicios.addAll( dao.buscar('2', null, null, null) );
        } catch (NullPointerException e) {
            if(aviso != null) {
                aviso.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                aviso.setVisibility(View.VISIBLE);
            }
            return;
        }

        if(aviso != null) {
            aviso.getLayoutParams().height = 0;
            aviso.setVisibility(View.INVISIBLE);

            RecyclerView recyclerView = findViewById(R.id.conteudo_recyclerview);
            Conteudo_RecyclerViewAdapter adapter = new Conteudo_RecyclerViewAdapter(this, exercicios);
            recyclerView.setAdapter(adapter);
        }
    }
}