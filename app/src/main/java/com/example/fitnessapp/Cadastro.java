package com.example.fitnessapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.CadastroClasses.AddSpinner;
import com.example.fitnessapp.CadastroClasses.CampoData;
import com.example.fitnessapp.db.classes.Usuario;
import com.example.fitnessapp.db.classes.UsuarioSession;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class Cadastro extends AppCompatActivity {

    private TextView cadastroDisponibilidadeText;
    private TextView cadastroFreqExerciciosText;


    private EditText cadastroNome;
    private CampoData cadastroDatanasc;
    private EditText cadastroEmail;
    private EditText cadastroSenha;
    private RadioGroup cadastroSexo;
    private SeekBar cadastroFreqExercicios;
    private SeekBar cadastroDisponibilidade;

    private Button btnCadastrar;

    // Spinners
    private AddSpinner cadastroExercicioRealizado;
    private AddSpinner cadastroFocoTreino;
    private AddSpinner cadastroObjetivo;




    @SuppressWarnings("deprecation")
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        init();

        if(UsuarioSession.getInstance(this).isLogged()) {
            Intent intent = new Intent(this, Principal.class);
            startActivity(intent);
            finish();
        }

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                cadastrar();
            }
        });

        cadastroFreqExercicios.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress >= 4) cadastroFreqExerciciosText.setText(R.string.condicaoAlta);
                else if(progress >= 2) cadastroFreqExerciciosText.setText(R.string.condicaoModerado);
                else cadastroFreqExerciciosText.setText(R.string.condicaoLeve);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        cadastroDisponibilidade.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                final String separador = ":";
                final int minsPAdicao = 30;
                final int horas = minsPAdicao + progress * minsPAdicao;
                final int minutos = (horas % 60);
                cadastroDisponibilidadeText.setText(String.format("%d%s%s", progress > 0 ? horas / 60 : 0, separador, minutos == 0 ? "00" : minutos));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    @SuppressLint("SetTextI18n")
    private void init() {
        cadastroNome = findViewById(R.id.cadastroNome);
        cadastroEmail = findViewById(R.id.cadastroEmail);
        cadastroSenha = findViewById(R.id.cadastroSenha);
        cadastroSexo = findViewById(R.id.cadastroSexo);
        cadastroFreqExercicios = findViewById(R.id.sbarFreqExerc);
        cadastroDisponibilidade = findViewById(R.id.sbarDisponibilidade);

        btnCadastrar = findViewById(R.id.cadastroBtn);

        // Data
        LinearLayout date = findViewById(R.id.cadastroDate);
        cadastroDatanasc = new CampoData(getSupportFragmentManager(), date.findViewById(R.id.dateText), date.findViewById(R.id.dateBtn));


        // Spinners
        cadastroExercicioRealizado = createAddSpinner(findViewById(R.id.cadastroExercsRealizados), R.array.ExercRealizado);
        cadastroFocoTreino = createAddSpinner(findViewById(R.id.cadastroFoco), R.array.FocoTreino);
        cadastroObjetivo = createAddSpinner(findViewById(R.id.cadastroObjetivo), R.array.ObjTreino);

        // init-txtHelpers
        cadastroDisponibilidadeText = findViewById(R.id.textHelperDisponibilidade);
            cadastroDisponibilidadeText.setText("00:30");
            cadastroDisponibilidade.setProgress(0);

        cadastroFreqExerciciosText = findViewById(R.id.textHelperFreqExerc);
            cadastroFreqExerciciosText.setText(R.string.condicaoLeve);
            cadastroFreqExercicios.setProgress(0);
    }

    private AddSpinner createAddSpinner(LinearLayout layout, int ArrayID) {
        ArrayList<String> itens = new ArrayList<>(Arrays.asList(getResources().getStringArray(ArrayID)));
        RecyclerView view = layout.findViewById(R.id.viewAddspinner);
        return new AddSpinner(this, layout.findViewById(R.id.addspinnerID), layout.findViewById(R.id.btnAddspinner), view, itens);
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private void cadastrar() {

        // Validação dos campos
        if(TextUtils.isEmpty(cadastroNome.getText()) || TextUtils.isEmpty(cadastroEmail.getText()) || TextUtils.isEmpty(cadastroSenha.getText())) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }



        // Validação do cadastro
        if(!UsuarioSession.getInstance(this).isEmailFree(cadastroEmail.getText().toString())) {
            Toast.makeText(this, "Email já cadastrado", Toast.LENGTH_SHORT).show();
            return;
        }

        EditText confirm = findViewById(R.id.cadastroSenhaConfirm);
        if(!cadastroSenha.getText().toString().equals(confirm.getText().toString())) {
            Toast.makeText(this, "Senha Errada", Toast.LENGTH_SHORT).show();
            return;
        }

        // Registro
        try {
            UsuarioSession usuS = UsuarioSession.getInstance(this);
            DateTimeFormatter timeformat = DateTimeFormatter.ofPattern("H:mm:ss");

            Usuario usuario = new Usuario();
                usuario.setNome(cadastroNome.getText().toString());
                usuario.setEmail(cadastroEmail.getText().toString());
                usuario.setDatanasc(cadastroDatanasc.getDate());
                usuario.setSexo(cadastroSexo.getCheckedRadioButtonId() == R.id.cadastroMasc ? 'M' : 'F');
                usuario.setCondicao(cadastroFreqExerciciosText.getText().toString());

                usuario.setFoco(cadastroFocoTreino.reachSizeLista() ? null : cadastroFocoTreino.getSelectedItem());
                usuario.setExercsRealizados(cadastroExercicioRealizado.getSelectedItens());
                usuario.setObjetivos(cadastroObjetivo.getSelectedItens());

                usuario.setDisponibilidade(LocalTime.parse(cadastroDisponibilidadeText.getText().toString() + ":00", timeformat));

            boolean concluido = usuS.cadastrar(usuario, cadastroSenha.getText().toString());

            if(concluido) {
                Intent intent = new Intent(this, Principal.class);
                startActivity(intent);
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
