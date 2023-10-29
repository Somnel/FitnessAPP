package com.example.fitnessapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnessapp.db.classes.Usuario;
import com.example.fitnessapp.db.classes.UsuarioSession;

import java.time.format.DateTimeFormatter;

public class Perfil extends AppCompatActivity {


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
        setContentView(R.layout.activity_perfil);

        UsuarioSession usuarioSession = UsuarioSession.getInstance(this);
        if(!usuarioSession.isLogged()) { UsuarioSession.logOut(this); }

        Usuario usuario = UsuarioSession.getInstance(this).getUsuario();
        init(usuario);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init(Usuario usuario) {
        final DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        defCampoPerfil(findViewById(R.id.layoutPerfil_nome), "Nome : ", usuario.getNome());
        defCampoPerfil(findViewById(R.id.layoutPerfil_sexo), "Sexo : ", String.valueOf(usuario.getSexo()).equals("M") ? "Masculino" : "Feminino");
        defCampoPerfil(findViewById(R.id.layoutPerfil_email), "Email : ", usuario.getEmail());
        defCampoPerfil(findViewById(R.id.layoutPerfil_datanasc), "Data de Nascimento : ", usuario.getDatanasc().format(formatDate));
        defCampoPerfil(findViewById(R.id.layoutPerfil_datacadastro), "Data de Cadastro : ", usuario.getDatacadastro().format(formatDate));
    }

    private void defCampoPerfil(LinearLayout layout, String tituloNome, String valor) {
        TextView titulo = layout.findViewById(R.id.perfilCampo_titulo);
        titulo.setText(tituloNome);

        TextView campo = layout.findViewById(R.id.perfilCampo_valor);
        campo.setText(valor);
    }
}