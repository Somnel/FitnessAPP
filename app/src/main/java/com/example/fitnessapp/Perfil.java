package com.example.fitnessapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnessapp.db.classes.UsuarioSession;

public class Perfil extends AppCompatActivity {

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
        setContentView(R.layout.activity_perfil);

        UsuarioSession usuarioSession = UsuarioSession.getInstance(this);
        if(!usuarioSession.isLogged()) { UsuarioSession.logOut(this); }

    }
}