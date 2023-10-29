package com.example.fitnessapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.MonitoreClasses.Monitore_RecyclerViewAdapter;
import com.example.fitnessapp.db.classes.ExercicioDao;
import com.example.fitnessapp.db.classes.HistoricoExercicio;
import com.example.fitnessapp.db.classes.UsuarioSession;

import java.util.List;

public class Monitore extends AppCompatActivity {


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
        setContentView(R.layout.activity_monitore);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init() {
        int idUsuario = UsuarioSession.getInstance(this).getUsuario().getID();
        ExercicioDao dao = new ExercicioDao(this);

        List<HistoricoExercicio> historico = dao.buscarHistorico(idUsuario, 0);
        if(!historico.isEmpty()) {
            RecyclerView recyclerView = findViewById(R.id.monitore_RViewHistorico);
            Monitore_RecyclerViewAdapter adapter = new Monitore_RecyclerViewAdapter(this, historico);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            TextView aviso = findViewById(R.id.monitore_avisoHistoricoVazio);
            aviso.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            aviso.setVisibility(View.VISIBLE);
        }



    }
}