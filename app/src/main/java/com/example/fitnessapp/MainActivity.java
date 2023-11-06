package com.example.fitnessapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.fitnessapp.db.classes.Usuario;
import com.example.fitnessapp.db.classes.UsuarioSession;

import java.time.LocalDate;
import java.time.LocalTime;

public class MainActivity extends AppCompatActivity {

    private Button btn_logar;
    private EditText field_email;
    private EditText field_senha;
    private TextView txtview_cadastro;
    private Class login_redirect;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.p3));
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.p3));

        btn_logar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String email = field_email.getText().toString();
                String senha = field_senha.getText().toString();

                if( !(TextUtils.isEmpty(email) || TextUtils.isEmpty(senha)) )
                    login(email, senha);
            }
        });

        txtview_cadastro.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                cadastro_activity();
            }
        });

    }

    private void init() {
        btn_logar = findViewById(R.id.btn_logar);
        field_email = findViewById(R.id.field_email);
        field_senha = findViewById(R.id.field_senha);
        txtview_cadastro = findViewById(R.id.textClick_cadastro);
        login_redirect = Principal.class;

        Log.d("Main", "Iniciou");
    }

    private void login(String email, String senha) {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                UsuarioSession usuarioSession = UsuarioSession.getInstance(this);

                if(usuarioSession.isEmailFree("daddyissuesissexy@total.com")) {
                    Usuario usuario = new Usuario();
                    usuario.setNome("Willian Afton");
                    usuario.setEmail("daddyissuesissexy@total.com");
                    usuario.setDatanasc(LocalDate.now());
                    usuario.setSexo('M');
                    usuario.setCondicao("Sedentário");

                    LocalTime lctime = LocalTime.of(18, 0, 0);
                    usuario.setDisponibilidade(lctime);

                    usuarioSession.cadastrar(usuario, "123456");
                }

                if(usuarioSession.iniciar(email, senha)) {
                    Intent intent = new Intent(this, login_redirect);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Usuário não existente", Toast.LENGTH_SHORT).show();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void cadastro_activity() {
        Intent intent = new Intent(this, Cadastro.class);
        startActivity(intent);
        finish();
    }
}